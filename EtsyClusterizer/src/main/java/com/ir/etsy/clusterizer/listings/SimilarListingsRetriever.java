package com.ir.etsy.clusterizer.listings;

import com.ir.etsy.clusterizer.utils.ListingProperties;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Dimitar
 */
public class SimilarListingsRetriever {

    //NOTE: change it to lead to your index.
    private static final String INDEX_DIR = "C:\\Mimi\\SU\\Magistratura\\ML\\Project\\IR_project_etsy_files\\index";

    public static List<Listing> findSimilar(Listing listing) throws IOException {
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(INDEX_DIR);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);

        MoreLikeThis mlt = new MoreLikeThis(indexReader);
        mlt.setFieldNames(new String[]{
            ListingProperties.TITLE,
            ListingProperties.TAGS
        });
        mlt.setAnalyzer(new StandardAnalyzer());

        TopDocs originalListingSearch = indexSearcher.search(new PhraseQuery(ListingProperties.LISTING_ID, String.valueOf(listing.getListingId())), 1);

        if (originalListingSearch.totalHits == 0) {
            //Should not happen. But if happens - execute custom similarities searcher
            return runCustomSearcher(listing, indexReader, indexSearcher);
        }

        Query query = mlt.like(originalListingSearch.scoreDocs[0].doc);

        TopDocs topHits = indexSearcher.search(query, 50);

        return prepareResult(indexSearcher, topHits);
    }

    private static List<Listing> runCustomSearcher(Listing listing, IndexReader reader, IndexSearcher indexSearcher) throws IOException {
        String title = listing.getTitle();
        NounAnalyzer analyzer = new NounAnalyzer();
        analyzer.findNouns(title);
        List<String> tags = listing.getTags();
        List<String> materials = listing.getMaterials();
        List<Long> categories = listing.getCategoryPathIds();
        BooleanQuery query = buildBooleanQuery(tags, materials, categories);
        TopDocs hits = indexSearcher.search(query, 50);
        return prepareResult(indexSearcher, hits);
    }

    public static List<Listing> runCustomSearcher(Listing listing) throws IOException {
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(INDEX_DIR);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);
        return runCustomSearcher(listing, indexReader, indexSearcher);
    }

    private static BooleanQuery buildBooleanQuery(List<String> tags, List<String> materials, List<Long> categories) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
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

        BooleanQuery query = builder.build();
        return query;
    }

    private static List<Listing> prepareResult(IndexSearcher indexSearcher, TopDocs hits) throws IOException {
        if (hits.totalHits == 0) {
            return Collections.<Listing>emptyList();
        }

        List<Listing> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            results.add(new Listing(doc));
        }

        return results;
    }

}
