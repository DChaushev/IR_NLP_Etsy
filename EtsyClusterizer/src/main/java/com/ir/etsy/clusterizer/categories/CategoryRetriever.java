package com.ir.etsy.clusterizer.categories;

import com.ir.etsy.clusterizer.utils.LuceneDocumentType;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

public class CategoryRetriever {
    
    private static final int MAX_RESULT_COUNT = 3;
    
    public static List<Category> getMatchingCategories(Set<String> terms, Long notThisCategoryId) throws IOException {
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(LuceneDocumentType.CATEGORY);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);
        
        String stringCategoryid = String.valueOf(notThisCategoryId);
        
        BooleanQuery query = buildBooleanQuery(terms, stringCategoryid);
        TopDocs hits = indexSearcher.search(query, MAX_RESULT_COUNT);
        
        return prepareResult(indexSearcher, hits);
    }

    private static BooleanQuery buildBooleanQuery(Set<String> terms, String notThisCategoryId) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        
        for (String term : terms) {
            Query query = new TermQuery(new Term(CategoryProperties.META_KEYWORDS, term));
            builder.add(query, BooleanClause.Occur.SHOULD);
            Query query2 = new TermQuery(new Term(CategoryProperties.META_TITLE, term));
            builder.add(query2, BooleanClause.Occur.SHOULD);
        }

        Query sameCategoryQuery = new TermQuery(new Term(CategoryProperties.CATEGORY_ID, notThisCategoryId));
        builder.add(sameCategoryQuery, BooleanClause.Occur.MUST_NOT);

        BooleanQuery query = builder.build();
        return query;
    }

    private static List<Category> prepareResult(IndexSearcher indexSearcher, TopDocs hits) throws IOException {
        if (hits.totalHits == 0) {
            return Collections.<Category>emptyList();
        }

        List<Category> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            results.add(new Category(doc));
        }

        return results;
    }
}
