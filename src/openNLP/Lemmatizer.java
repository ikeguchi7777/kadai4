package openNLP;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;

public class Lemmatizer {
	
	private static DictionaryLemmatizer lemmatizer;

	public static String Lemmatize(String token,String posTag) {
		if(lemmatizer==null)
			Init();
		String[] tokens = {token};
		String[] posTags = {posTag};
		return lemmatizer.lemmatize(tokens, posTags)[0];
	}
	
	private static void Init() {
		try {
			InputStream dictLemmatizer = new FileInputStream("model/en-lemmatizer.txt");
			lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        
	}
}
