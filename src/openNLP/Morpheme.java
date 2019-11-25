package openNLP;

public class Morpheme {
	String POSTag;
	String word;
	String model; //原形

	public Morpheme(String tag, String word) {
		POSTag = tag;
		this.word = word;
		model = Lemmatizer.Lemmatize(word, tag);
	}

	public String getWord() {
		return word;
	}
	
	@Override
	public String toString() {
		return "["+word+","+POSTag+","+model+" ]";
	}
}