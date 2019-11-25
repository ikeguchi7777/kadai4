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
	
	@Override
	public String toString() {
		String result ="---";
		for (int i = 0; i < size(); i++) {
			result+=get(i);
			if(i<size()-1) {
				result+="\n    |  |\n"+
				          "    |  --";
			}
		}
		return result;
	}
}
