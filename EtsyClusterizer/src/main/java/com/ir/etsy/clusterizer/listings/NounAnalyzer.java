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

public class NounAnalyzer {

	static Set<String> nounPhrases = new HashSet<>();

	public static void findNouns(String sentence) {
            System.out.print("%%%%%%%%%Sentence is " + sentence);

	}
}
