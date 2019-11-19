package openNLP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * 文節のリストから成る1文．文節間の係り受け構造を保持するクラス．
 */
public class Sentence extends ArrayList<Chunk> {

	public static void main(String[] args) {
		Sentence sent = new Sentence();
		String txt = "what is it?";
		List<String> tList = splitSentences(txt);
		String[] rStrings = new String[tList.size()];
		tList.toArray(rStrings);
		sent.analysis(rStrings);
		System.out.println(tList);
		// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
		/*InputStream is;
		try {
			is = new FileInputStream("model/en-parser-chunking.bin");
			ParserModel model = new ParserModel(is);

			Parser parser = (Parser) ParserFactory.create(model);

			String sentence = "There is little coreference resolution documentation for OpenNLP .";
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			for (Parse p : topParses) {
				p.show();
			}

			is.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		*/
		/*
		 * (TOP (S (NP (EX There)) (VP (VBZ is) (NP (NP (JJ little) (NN coreference) (NN
		 * resolution) (NN documentation)) (PP (IN for) (NP (NNP OpenNLP.)))))))
		 *
		 */
	}

	public void analysis(String[] tokens) {
		try {

			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			InputStream posModelIn = new FileInputStream("model/en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			String tags[] = posTagger.tag(tokens);

			// reading the chunker model
			InputStream ins = new FileInputStream("model/en-chunker.bin");
			// loading the chunker model
			ChunkerModel chunkerModel = new ChunkerModel(ins);
			// initializing chunker(maximum entropy) with chunker model
			ChunkerME chunker = new ChunkerME(chunkerModel);
			// chunking the given sentence : chunking requires sentence to be tokenized and
			// pos tagged
			String[] chunks = chunker.chunk(tokens, tags);
			Chunk last = null;

			for (int i = 0; i < tokens.length; i++) {
				if (!tags[i].equals("IN") && chunks[i].startsWith("B")) {
					last = new Chunk(chunks[i].split("-")[1]);
					add(last);
				}
				last.add(new Morpheme(tags[i], tokens[i]));
			}

			// printing the results
			System.out.println("\nChunker Example in Apache OpenNLP\nPrinting chunks for the given sentence...");
			System.out.println("\nTOKEN - POS_TAG - CHUNK_ID\n-------------------------");
			for (int i = 0; i < chunks.length; i++) {
				System.out.println(tokens[i] + " - " + tags[i] + " - " + chunks[i]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文に区切るためのセパレータ
	 */
	static List<String> separators = Arrays.asList(new String[] { " ", "　", "！", "!", "？", "?", ",", ".", "\n" });

	/**
	 * 文に区切る
	 *
	 * @param text 複数の文を含む可能性のあるString
	 * @return 区切られた文（String）のリスト
	 */
	static List<String> splitSentences(String text) {
		List<String> sentences = new ArrayList<>();
		List<Integer> splitID = new ArrayList<>();
		// List<String> sentences = new ArrayList<String>();
		for (String separator : separators) {
			if (text.contains(separator)) {
				int index = text.indexOf(separator);
				while (index != -1) {
					splitID.add(index);
					index = text.indexOf(separator, index + 1);
				}
			}
		}
		splitID.sort(null);
		int from = 0;
		for (Integer integer : splitID) {
			sentences.add(text.substring(from, integer));
			String getChar = text.substring(integer, integer + 1);
			if (!(getChar.equals(" ") || getChar.equals("　")))
				sentences.add(getChar);

			from = integer + 1;
		}
		return sentences;
	}

}