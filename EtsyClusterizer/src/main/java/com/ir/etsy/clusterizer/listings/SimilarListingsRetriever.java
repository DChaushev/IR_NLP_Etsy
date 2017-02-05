package com.ir.etsy.clusterizer.listings;

import com.ir.etsy.clusterizer.categories.Category;
import com.ir.etsy.clusterizer.categories.CategoryRetriever;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import com.ir.etsy.clusterizer.nlp.NounExtractor;
import com.ir.etsy.clusterizer.utils.LuceneDocumentType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Dimitar
 */
public class SimilarListingsRetriever {

    private static final int MAX_RESULT_COUNT = 50;

    public static List<ResultListing> findSimilar(Listing listing) throws IOException {
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(LuceneDocumentType.LISTING);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);

        TopDocs similarDocs = runCustomSearcher(listing, indexReader, indexSearcher);

        MoreLikeThis mlt = new MoreLikeThis(indexReader);
        mlt.setFieldNames(new String[]{
            ListingProperties.TITLE,
            ListingProperties.TAGS
        });
        mlt.setAnalyzer(ListingAnalyzer.getAnalyzer());

        TopDocs originalListingSearch = indexSearcher.search(new PhraseQuery(ListingProperties.LISTING_ID, String.valueOf(listing.getListingId())), 1);

        if (originalListingSearch.totalHits != 0) {
            Query query = mlt.like(originalListingSearch.scoreDocs[0].doc);
            TopDocs mltDocs = indexSearcher.search(query, MAX_RESULT_COUNT);

            if (mltDocs.totalHits != 0) {
                // Will merge the results and leave the top MAX_RESULT_COUNT ones by score
                // XXX: not sure these are normalized before that
                similarDocs = TopDocs.merge(MAX_RESULT_COUNT, (new TopDocs []{similarDocs, mltDocs}));
            }
        }

        return prepareResult(indexSearcher, similarDocs);
    }
    
    // TODO: wire-in with the other logic. Currently rarely returns any results
    public static List<ResultListing> expandSearchWithSimilarCategories(Listing listing) throws IOException {
        List<Long> categoryPathIds = listing.getCategoryPathIds();
        if (categoryPathIds.isEmpty()) {
            return Collections.<ResultListing>emptyList();
        }
        
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(LuceneDocumentType.LISTING);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);

        String title = listing.getTitle();
        Set<String> titleNouns = NounExtractor.extractNouns(title);
        Long mostSpecificCategoryId = categoryPathIds.get(categoryPathIds.size() - 1);

        // Search the nouns from the title in category keywords
        List<Category> categories = CategoryRetriever.getMatchingCategories(titleNouns, mostSpecificCategoryId);
        
        if (categories.isEmpty()) {
            return Collections.<ResultListing>emptyList();
        }
        
        BooleanQuery query = buildCategoriesBooleanQuery(categories);
        TopDocs docs = indexSearcher.search(query, MAX_RESULT_COUNT);

        return prepareResult(indexSearcher, docs);
    }
    
    private static BooleanQuery buildCategoriesBooleanQuery(List<Category> categories) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        for (Category category : categories) {
            BooleanQuery.Builder categoryQueryBuilder = new BooleanQuery.Builder();
            // Level is 1-based
            Integer level = category.getLevel();
            Long categoryId = category.getCategoryId();

            Query query = new PhraseQuery(ListingProperties.CATEGORY+(level-1), String.valueOf(categoryId));
            categoryQueryBuilder.add(query, BooleanClause.Occur.MUST);
            BooleanQuery categoryQuery = categoryQueryBuilder.build();

            builder.add(categoryQuery, BooleanClause.Occur.SHOULD);
            break;
        }

        BooleanQuery query = builder.build();
        return query;
    }

    private static TopDocs runCustomSearcher(Listing listing, IndexReader reader, IndexSearcher indexSearcher) throws IOException {
        String title = listing.getTitle();
        List<String> tags = listing.getTags();
        List<String> materials = listing.getMaterials();
        List<Long> categories = listing.getCategoryPathIds();
        BooleanQuery query = buildBooleanQuery(title, tags, materials, categories);
        TopDocs hits = indexSearcher.search(query, MAX_RESULT_COUNT);
        return hits;
    }

    private static BooleanQuery buildBooleanQuery(String title, List<String> tags, List<String> materials, List<Long> categories) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Set<String> titleNouns = NounExtractor.extractNouns(title);

        for (String tag : tags) {
            Query query = new PhraseQuery(ListingProperties.TAGS, tag);
            builder.add(query, BooleanClause.Occur.SHOULD);
        }

        for (String material : materials) {
            Query query = new PhraseQuery(ListingProperties.MATERIALS, material);
            builder.add(query, BooleanClause.Occur.SHOULD);
        }

        if (!categories.isEmpty()) {
            String rootCategory = String.valueOf(categories.get(0));
            Query query = new PhraseQuery(ListingProperties.CATEGORY + "0", rootCategory);
            builder.add(query, BooleanClause.Occur.SHOULD);
        }

        // Search the nouns from the title in other listings' tags
        for (String titleNoun : titleNouns) {
            Query query = new TermQuery(new Term(ListingProperties.TAGS, titleNoun));
            builder.add(query, BooleanClause.Occur.SHOULD);
        }

        BooleanQuery query = builder.build();
        return query;
    }

    private static List<ResultListing> prepareResult(IndexSearcher indexSearcher, TopDocs hits) throws IOException {
        if (hits.totalHits == 0) {
            return Collections.<ResultListing>emptyList();
        }

        List<ResultListing> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            ResultListing res = new ResultListing(doc, scoreDoc.score);
            results.add(res);
        }

        return results;
    }
}
