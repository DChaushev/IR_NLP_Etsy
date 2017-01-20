package com.ir.etsy.clusterizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Main {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    System.out.println("I am the Clusterizer's main class!");

    Path indexPath = FileSystems.getDefault().getPath("");
    
    LuceneIndexWriter indexWriter = new LuceneIndexWriter(indexPath,
            "");
    
    indexWriter.createIndex();
    
    StandardAnalyzer analyzer = new StandardAnalyzer();
    String querystr = "tags:\"wood\"";
    Query q = null;
    try {
      q = new QueryParser("title", analyzer).parse(querystr);
    } catch (ParseException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    Directory index = FSDirectory.open(indexPath);
    
    int hitsPerPage = 100;
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopDocs docs = searcher.search(q, hitsPerPage);
    ScoreDoc[] hits = docs.scoreDocs;
    
    System.out.println("Found " + hits.length + " hits.");
    for(int i=0;i<hits.length;++i) {
        int docId = hits[i].doc;
        Document d = searcher.doc(docId);
        System.out.println((i + 1) + ". " + d.get("title"));
    }

    System.out.println("Good bye!");
  }
}
