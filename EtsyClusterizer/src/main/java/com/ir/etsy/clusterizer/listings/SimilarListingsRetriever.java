package com.ir.etsy.clusterizer.listings;

import com.ir.etsy.clusterizer.utils.ListingProperties;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
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
    private static final String INDEX_DIR = "D:\\IR_project_etsy_files\\index";

    public static List<Listing> process(Listing listing) throws IOException {
        IndexReader indexReader = LuceneIndexUtils.getIndexReader(INDEX_DIR);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);

        //TODO: this is just an initial implementation. Think of something smarter.
		List<String> tags = listing.getTags();
		List<String> materials = listing.getMaterials();
		List<String> similarParams = new ArrayList<String>();
		similarParams.addAll(tags);
		similarParams.addAll(materials);

        BooleanQuery query = buildBooleanQuery(similarParams);

        TopDocs hits = indexSearcher.search(query, 50);

        return prepareResult(indexSearcher, hits);
    }

    private static BooleanQuery buildBooleanQuery(List<String> tags) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String tag : tags) {
            Query query = new PhraseQuery(ListingProperties.TAGS, tag);
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
