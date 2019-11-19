import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
* Dictionary Lemmatizer Example in Apache OpenNLP
*/
public class DictionaryLemmatizerExample {

    public static void main(String[] args){
        try{
            // test sentence
        	final String SENTENCE="I want to do \" coreference resolution \" using OpenNLP . Documentation from Apache ( Coreference Resolution ) doesn't cover how to do \" coreference resolution \" . Does anybody have any docs or tutorial how to do this ?";
            String[] tokens = SENTENCE.split(" ",0);

            // Parts-Of-Speech Tagging
            // reading parts-of-speech model to a stream
            InputStream posModelIn = new FileInputStream("model/en-pos-maxent.bin");
            // loading the parts-of-speech model from stream
            POSModel posModel = new POSModel(posModelIn);
            // initializing the parts-of-speech tagger with model
            POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            String tags[] = posTagger.tag(tokens);

            // loading the dictionary to input stream
            InputStream dictLemmatizer = new FileInputStream("model/en-lemmatizer.txt");
            // loading the lemmatizer with dictionary
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
            
            // finding the lemmas
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);

            // printing the results
            LogArea.println("\nPrinting lemmas for the given sentence...");
            LogArea.println("WORD -POSTAG : LEMMA");
            for(int i=0;i< tokens.length;i++){
                System.out.println(tokens[i]+" -"+tags[i]+" : "+lemmas[i]);
            }
            Parse();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Parse() throws InvalidFormatException, IOException {
    	// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
    	InputStream is = new FileInputStream("model/en-parser-chunking.bin");

    	ParserModel model = new ParserModel(is);

    	Parser parser = (Parser) ParserFactory.create(model);

    	String sentence = "There is little coreference resolution documentation for OpenNLP at the moment except for a very short mention of how to run it in the readme.";
    	Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

    	for (Parse p : topParses)
    		p.show();

    	is.close();

    	/*
    	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
    	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
    	 */
    }
}