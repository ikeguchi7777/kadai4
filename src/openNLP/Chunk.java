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
}
