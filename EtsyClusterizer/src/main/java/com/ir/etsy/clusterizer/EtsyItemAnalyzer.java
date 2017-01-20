package com.ir.etsy.clusterizer;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;



public class EtsyItemAnalyzer {

  private static PerFieldAnalyzerWrapper analyzer = null;

  private static void init() {
    Map<String,Analyzer> analyzersPerField = new HashMap<>();
    analyzersPerField.put("firstname", new KeywordAnalyzer());
    analyzersPerField.put("lastname", new KeywordAnalyzer());
    analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzersPerField);
  }

  public static Analyzer getAnalyzer() {
    if (null == analyzer){
      init();
    }
    return analyzer;
  }
}
