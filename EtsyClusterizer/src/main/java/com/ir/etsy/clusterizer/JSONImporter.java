package com.ir.etsy.clusterizer;

import java.io.Reader;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;


public class JSONImporter {
  private String filePath;

  public JSONImporter(String filePath) {
    this.filePath = filePath;
  }

  public JSONArray parseJSONFile() throws FileNotFoundException {
    Reader readerJson = new FileReader(this.filePath);
    // Parse the JSON file using simple-json library
    Object fileObjects = JSONValue.parse(readerJson);
    JSONArray arrayObjects = (JSONArray)fileObjects;
    return arrayObjects;
  }
}