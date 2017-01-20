package com.ir.etsy.clusterizer;

import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.json.simple.JSONObject;

public class EtsyDocumentCreator {
  public static Document createForJSON(JSONObject object) {
    Document doc = new Document();
    
    String listingId = ((Long)object.get("listing_id")).toString();
    doc.add(new StringField("listingId", listingId, Field.Store.YES));
    
    String title = (String)object.get("title");
    doc.add(new TextField("title", title, Field.Store.YES));

    List<String> tags = (List<String>)object.get("tags");
    for (String tag : tags) {
      doc.add(new TextField("tags", tag, Field.Store.YES));
    }
    
    List<String> categories = (List<String>)object.get("category_path");
    int categoryIndex = 0;
    for (String category : categories) {
      doc.add(new StringField("category"+categoryIndex, category, Field.Store.YES));
    }
            
    long viewCount = (long)object.get("views");
    doc.add(new LongPoint("viewCount", viewCount));
    
    long favoriteCount = (long)object.get("num_favorers");
    doc.add(new LongPoint("favoriteCount", favoriteCount));
    
    return doc;
  }
}
