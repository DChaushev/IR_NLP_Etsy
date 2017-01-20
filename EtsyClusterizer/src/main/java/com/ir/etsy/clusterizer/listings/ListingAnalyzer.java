
package com.ir.etsy.clusterizer.listings;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


public class ListingAnalyzer {

  private static PerFieldAnalyzerWrapper analyzer = null;

  private static void init() {
    Map<String, Analyzer> analyzersPerField = new HashMap<>();
    /*
     * Put any specific per field analyzers here
     */
    analyzersPerField.put("category0", new KeywordAnalyzer());
    analyzersPerField.put("category1", new KeywordAnalyzer());
    analyzersPerField.put("category2", new KeywordAnalyzer());
    analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzersPerField);
  }

  public static Analyzer getAnalyzer() {
    if (null == analyzer){
      init();
    }
    return analyzer;
  }
}

