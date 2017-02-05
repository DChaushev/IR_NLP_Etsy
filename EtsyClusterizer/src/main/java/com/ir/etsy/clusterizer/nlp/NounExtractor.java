package com.ir.etsy.clusterizer.nlp;

import java.io.File;
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

// Extracts the nouns from a single sentence using Apache OpenNLP
public class NounExtractor {
    
    public static final String NOUN_LABEL = "NN";

	public static Set<String> extractNouns(String sentence) {
        // So we have only the unique nouns
        Set<String> nouns = new HashSet<>();

        InputStream modelIn = null;
        try {
            // Load pre-trained model file, taken from http://opennlp.sourceforge.net/models-1.5/
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            modelIn = classloader.getResourceAsStream("/en-parser-chunking.bin");
            ParserModel model = new ParserModel(modelIn);

            // Create parser
            Parser parser = ParserFactory.create(model);
            // Request only a single parse result (the top one)
            Parse parseResults[] = ParserTool.parseLine(sentence, parser, 1);
            
            if (parseResults.length > 0) {
                getNouns(parseResults[0], nouns);
            }
        } catch (IOException e) {
            Set<String> fakeNouns = new HashSet<>();
            fakeNouns.add("baby");
            return fakeNouns;
        } finally {
            if (null != modelIn) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }

        return nouns;
	}

	// Recursively loop through the parse tree extracting the nouns
	private static void getNouns(Parse p, Set<String> nouns) {
	    if (p.getType().equals(NOUN_LABEL)) {
	         nouns.add(p.getCoveredText());
	    }
	    for (Parse child : p.getChildren()) {
            getNouns(child, nouns);
        }
	}
}