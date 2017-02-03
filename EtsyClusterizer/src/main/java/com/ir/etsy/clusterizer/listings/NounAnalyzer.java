/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ir.etsy.clusterizer.listings;

/**
 *
 * @author Mimi
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

//extract nouns from a single sentence using OpenNLP
public class NounAnalyzer {

	static Set<String> nounPhrases = new HashSet<>();

	public static Set<String> findNouns(String sentence) {

            try {
                //load chunking model
                InputStream modelInParse = new FileInputStream("en-parser-chunking.bin"); //from http://opennlp.sourceforge.net/models-1.5/
                ParserModel model = new ParserModel(modelInParse);

                //create parse tree
                Parser parser = ParserFactory.create(model);
                Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

                for (Parse p : topParses) {
                    getNounPhrases(p);
                }

                //print noun phrases
                for (String s : nounPhrases)
                    System.out.println("Noun is !!!! " + s + " !!!!!");

                nounPhrases.clear();
                modelInParse.close();
            }
            catch (IOException e) {
            }
            return nounPhrases;
	}

	//recursively loop through tree, extracting nouns
	public static void getNounPhrases(Parse p) {
	    if (p.getType().equals("NN")) {
	         nounPhrases.add(p.getCoveredText());
	    }
	    for (Parse child : p.getChildren())
	         getNounPhrases(child);
	}
}
