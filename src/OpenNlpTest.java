import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class OpenNlpTest {
	public static void main(String[] args) throws Exception {
		// モデルファイル
		final String MODEL_FILE_PATH = "model/en-pos-maxent.bin";
		// タグ付けする文
		final String SENTENCE="The Apache OpenNLP library is a machine learning based toolkit for the processing of natural language text.";
		
		// taggerに必要なインスタンスの初期化．少し時間がかかる．
		InputStream modelIn = new FileInputStream(MODEL_FILE_PATH);
		POSModel model = new POSModel(modelIn);
		POSTaggerME tagger = new POSTaggerME(model);
		modelIn.close();

		// タグ付けして結果を表示．
		// 文の長さにもよるがこちらは高速なので，始めの初期化以外はわりと高速に動作する．
		// 続けて別の文にタグ付けしたい場合はtagger.tag(sentence)を呼ぶだけで良い．
		String[] sentenceArray=SENTENCE.split(" ",0);
		String[] tags=tagger.tag(sentenceArray);
		for(int i=0;i<sentenceArray.length;i++){
			System.out.println(sentenceArray[i]+"\t"+tags[i]);
		}
	}
}