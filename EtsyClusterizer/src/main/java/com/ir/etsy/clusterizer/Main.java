package com.ir.etsy.clusterizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;


public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    System.out.println("I am the Clusterizer's main class!");

    JSONImporter importer = new JSONImporter(args[0]);
    System.out.println("Pasrsing JSON file.");
    importer.parseJSONFile();

    System.out.println("Good bye!");
  }
}
