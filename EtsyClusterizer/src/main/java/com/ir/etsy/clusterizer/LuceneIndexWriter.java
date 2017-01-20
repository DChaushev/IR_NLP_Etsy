package com.ir.etsy.clusterizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class LuceneIndexWriter {

  Path indexPath;
  String jsonFilePath;
  IndexWriter indexWriter = null;

  public LuceneIndexWriter(Path indexPath, String jsonFilePath) {
    this.indexPath = indexPath;
    this.jsonFilePath = jsonFilePath;
  }
  
   public JSONArray parseJSONFile() {
    Reader readerJson;
    try {
      readerJson = new FileReader(this.jsonFilePath);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(LuceneIndexWriter.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
    // Parse the JSON file using simple-json library
    Object fileObjects = JSONValue.parse(readerJson);
    JSONArray arrayObjects = (JSONArray)fileObjects;
    return arrayObjects;
  }

  public void createIndex() {
    JSONArray jsonObjects = parseJSONFile();
    if (jsonObjects != null) {
      if(openIndex()) {
        addDocuments(jsonObjects);
        finish();
      }
    }
  }

  public boolean openIndex() {
    try {
      Directory dir = FSDirectory.open(indexPath);
      Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      // Always overwrite the directory
      iwc.setOpenMode(OpenMode.CREATE);
      indexWriter = new IndexWriter(dir, iwc);
      return true;
    } catch (Exception e) {
      System.err.println("Error opening the index. " + e.getMessage());
    }
    return false;
  }

  /**
   * Add documents to the index
   */
  public void addDocuments(JSONArray jsonObjects) {
    for (JSONObject object : (List<JSONObject>) jsonObjects) {
      Document doc = EtsyDocumentCreator.createForJSON(object);
      
      try {
        indexWriter.addDocument(doc);
      } catch (IOException ex) {
        System.err.println("Error adding documents to the index. " + ex.getMessage());
      }
    }
  }

  /**
   * Write the document to the index and close it
   */
  public void finish() {
    try {
      indexWriter.commit();
      indexWriter.close();
    } catch (IOException ex) {
      System.err.println("We had a problem closing the index: " + ex.getMessage());
    }
  }
}