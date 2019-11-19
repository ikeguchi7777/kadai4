import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class OpenNlpTest {
	public static void main(String[] args) throws Exception {
		/*// モデルファイル
		final String MODEL_FILE_PATH = "model/en-pos-maxent.bin";
		// タグ付けする文
		//final String SENTENCE="The Apache OpenNLP library is a machine learning based toolkit for the processing of natural language text.";
		final String SENTENCE="What is it?";
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
		}*/
		
		try{
            // test sentence
            String[] tokens = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
                    "morning", "and", "afternoon", "newspapers", "."};
 
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
            // chunking the given sentence : chunking requires sentence to be tokenized and pos tagged
            String[] chunks = chunker.chunk(tokens,tags);
 
            // printing the results
            System.out.println("\nChunker Example in Apache OpenNLP\nPrinting chunks for the given sentence...");
            System.out.println("\nTOKEN - POS_TAG - CHUNK_ID\n-------------------------");
            for(int i=0;i< chunks.length;i++){
                System.out.println(tokens[i]+" - "+tags[i]+" - "+chunks[i]);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}