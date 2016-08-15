package test; /**
 * Created by pc on 22/07/2016.
 */

import config.User;
import edu.udo.cs.wvtool.util.WVToolException;
import stemmer.StopWords;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import vn.hus.nlp.tokenizer.VietTokenizer;
import vn.hus.nlp.utils.UTF8FileUtility;
import wvtNew.WVToolNew;

import java.io.*;
import java.util.*;


public class test {

    /**
     * Setting
     ***************************************************/
    final static String currentDirectory = "/home/pc/Documents/TFIDF/src/main/resources/";
    final static String[] inputFileName = {
            "0.txt","1.txt","2.txt"
    };

    private static String normalize(String var1){
        String var2= var1.toLowerCase();
        return var2;
    }

//    public WVTWordList getWordList() throws WVToolException {
//        String[] sentences;
//        ArrayList<ArrayList<String>> tokens= new ArrayList<ArrayList<String>>();
//        StopWords stopWords= new StopWords();
//        VietTokenizer vietTokenizer= new VietTokenizer();
//        for(int i=0; i<inputFileName.length; i++){
//            ArrayList<String> t= new ArrayList<String>();
//            sentences= UTF8FileUtility.getLines(currentDirectory+"input/"+inputFileName[i]);
//            for(String sentence: sentences){
//                String[] var1= vietTokenizer.tokenize(sentence);
//                for(String var2:var1){
//                    String[] var3= var2.split("\\s+");
//                    for(String var4: var3){
//                        if(!stopWords.isStopword(var4)){
//                            t.add(normalize(var4));
//                        }
//                    }
//                }
//            }
//            tokens.add(t);
//        }
//        WVTConfiguration config = new WVTConfiguration();
//        WVToolNew wvtn= new WVToolNew(false);
//        WVTWordList wordList= wvtn.createWordList(tokens, config);
//        wordList.pruneByFrequency(2,10);
//        return wordList;
//    }

    public User getUserVector() throws WVToolException, IOException {
        User user= new User();
        String[] sentences;
        ArrayList<ArrayList<String>> tokens= new ArrayList<ArrayList<String>>();
        StopWords stopWords= new StopWords();
        VietTokenizer vietTokenizer= new VietTokenizer();
        for(int i=0; i<inputFileName.length; i++){
            ArrayList<String> t= new ArrayList<String>();
            sentences= UTF8FileUtility.getLines(currentDirectory+ "resources/input/" +inputFileName[i]);
            for(String sentence: sentences){
                String[] var1= vietTokenizer.tokenize(sentence);
                for(String var2:var1){
                    String[] var3= var2.split("\\s+");
                    for(String var4: var3){
                        if(!stopWords.isStopword(var4)){
                            t.add(normalize(var4));
                        }
                    }
                }
            }
            tokens.add(t);
        }
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn= new WVToolNew(false);
        WVTWordList wordList= wvtn.createWordList(tokens, config);
        wordList.pruneByFrequency(1,10);
        user.setWordList(wordList);
        wordList.storePlain(new FileWriter("/home/pc/Documents/TFIDF/src/main/resources/result/wordlist.txt"));
        user.setWordVectors(wvtn.createVector(tokens, config, wordList));
        return user;
    }

//    public WVTWordVector getWordVector(WVTWordList wordList, WVTDocumentInfo documentInfo) throws WVToolException {
//        WVTConfiguration config = new WVTConfiguration();
//        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
//        WVToolNew wvtn= new WVToolNew(false);
//        WVTWordVector wordVector= wvtn.createVector(documentInfo, config, wordList);
//        return  wordVector;
//    }

    public static void writeVector() throws WVToolException, IOException {
        String[] sentences;
        ArrayList<ArrayList<String>> tokens= new ArrayList<ArrayList<String>>();
        StopWords stopWords= new StopWords();
        VietTokenizer vietTokenizer= new VietTokenizer();
        for(int i=0; i<inputFileName.length; i++){
            ArrayList<String> t= new ArrayList<String>();
            sentences= UTF8FileUtility.getLines(currentDirectory+ "resources/input/" +inputFileName[i]);
            for(String sentence: sentences){
                String[] var1= vietTokenizer.tokenize(sentence);
                for(String var2:var1){
                    String[] var3= var2.split("\\s+");
                    for(String var4: var3){
                        if(!stopWords.isStopword(var4)){
                            t.add(normalize(var4));
                        }
                    }
                }
            }
            tokens.add(t);
        }
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn= new WVToolNew(false);
        WVTWordList wordList= wvtn.createWordList(tokens, config);
        wordList.pruneByFrequency(2,10);

        wordList.storePlain(new FileWriter("/home/pc/Documents/TFIDF/src/main/resources/result/wordlist.txt"));
        FileWriter outFile = new FileWriter("/home/pc/Documents/TFIDF/src/main/resources/result/wv.txt");
        WordVectorWriter wvw = new WordVectorWriter(outFile, true);
        config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
        wvtn.createVectors(tokens, config, wordList);
        wvw.close();
        outFile.close();
    }

    public static void main(String[] args) throws WVToolException, IOException {
        test t= new test();
        User user= t.getUserVector();
//        user.getWordList();
//        KMean kMean= new KMean(user,2, 1000, 0.1);
//        System.err.println(kMean.getUserProfile().clusters[0].getSize());
//        System.err.println(kMean.getUserProfile().clusters[1].getSize());
        System.out.print(user);
    }
}