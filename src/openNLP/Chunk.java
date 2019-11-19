package openNLP;

import java.util.ArrayList;

/**
 * 文節を表すクラス
 */
public class Chunk extends ArrayList<Morpheme> {
	String CTag;

	public Chunk(String tag) {
		super();
		CTag=tag;
	}

	public String getMorphemes(){
		String line = "";
		for (Morpheme word : Chunk.this) {
			line+="".concat(word.getWord()).concat(" ");
		}
		return line;
	}
}