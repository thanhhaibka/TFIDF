package test;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pc on 22/07/2016.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        // EXAMPLE HOW TO CALL THE PROGRAM FROM JAVA

        // Initialize the WVTool
        WVTool wvt = new WVTool(false);

        // Initialize the configuration
        WVTConfiguration config = new WVTConfiguration();

        final WVTStemmer dummyStemmer = new DummyStemmer();
        final WVTStemmer porterStemmer = new PorterStemmerWrapper();

//        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationRule() {
//            public Object getMatchingComponent(WVTDocumentInfo d) {
//
//                if (d.getContentLanguage().equals("english"))
//                    return porterStemmer;
//                else
//                    return dummyStemmer;
//            }
//        });

//        WVTStemmer stemmer = new LovinsStemmerWrapper();

//        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));

        // Initialize the input list with two classes
        WVTFileInputList list = new WVTFileInputList(3);

        // Add entries
        list.addEntry(new WVTDocumentInfo("http://www.bbc.com/news/world-europe-36880606",
                "html","","english",0));
        list.addEntry(
                new WVTDocumentInfo("http://www.bbc.com/news/world-south-asia-36879072",
                        "html","","english",1));
        list.addEntry(
                new WVTDocumentInfo("http://kenh14.vn/an-nguy-tham-gia-gameshow-khong-danh-cho-nguoi-yeu-tim-20160722112956618.chn","html","","vietnam",2));
        FileWriter outFile = new FileWriter("wv.txt");

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        // Prune the word list

        wordList.pruneByFrequency(2,10);

        // Alternativ I: read an already created word list from a file
        // WVTWordList wordList2 =
        // new WVTWordList(new FileReader("/home/wurst/tmp/wordlisttest.txt"));

        // Alternative II: Use predifined dimensions
        // List dimensions = new Vector();
        // dimensions.add("atheist");
        // dimensions.add("christian");
        // wordList =
        // wvt.createWordList(list, config, dimensions, false);

        // Store the word list in a file
        wordList.storePlain(new FileWriter("/home/pc/Documents/TFIDF/src/main/resources/result/wordlisttest.txt"));

        // Create the word vectors

        // Set up an output filter (write sparse vectors to a file)
        outFile = new FileWriter("/home/pc/Documents/TFIDF/src/main/resources/result/wvtest.txt");
        WordVectorWriter wvw = new WordVectorWriter(outFile, true);

        config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));

        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));

        // Create the vectors
        wvt.createVectors(list, config, wordList);

        // Alternatively: create word list and vectors together
        // wvt.createVectors(list, config);

        // Close the output file
        wvw.close();
        outFile.close();

        // Just for demonstration: Create a vector from a String
//        WVTWordVector q = wvt.createVector("loạ trốn cũng không thoát được trong", wordList);
    }
}
