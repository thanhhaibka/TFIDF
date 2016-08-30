package config;

import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 28/07/2016.
 */
public class User {
    private ArrayList<WVTWordVector> wordVectors;
    private WVTWordList wordList;
    private Map<String, String> mapWords;
    private ArrayList<TFVector> tfVector;

    public Map<String, String> getMapWords() {
        return mapWords;
    }

    public void setMapWords(Map<String, String> mapWords) {
        this.mapWords = mapWords;
    }

    public ArrayList<TFVector> getTfVector() {
        return tfVector;
    }

    public void setTfVector(ArrayList<TFVector> tfVector) {
        this.tfVector = tfVector;
    }

    public ArrayList<WVTWordVector> getWordVectors() {
        return wordVectors;
    }

    public void setWordVectors(ArrayList<WVTWordVector> wordVectors) {
        this.wordVectors = wordVectors;
    }

    public WVTWordList getWordList() throws IOException {
        return wordList;
    }

    public String printWordList(){
        String s="";
        for (int var=0;var< wordList.getNumWords(); var++) {
            s+=wordList.getWord(var)+" ";
        }
        return s;
    }

    public void setWordList(WVTWordList wordList) {
        this.wordList = wordList;
    }

    public Map<String, Double> getMapTFIDF(){
        Map<String, Double> m= new HashMap<String, Double>();
        for(WVTWordVector v: wordVectors){
            Map<String, Double> map= new HashMap<String, Double>();
            double[] var= v.getValues();
            for(int var1=0; var1<var.length; var1++){
                if(var[var1]!=0.0){
                    map.put(this.wordList.getWord(var1), var[var1]);
                }
            }
//            System.out.println(v.getWvtDocumentInfo().getSourceName());
            m= merge2(map, m);
        }
//        System.out.println(m);
        return m;
    }

    public Map<String, Integer> getMap(){
        Map<String, Integer> m= new HashMap<String, Integer>();
        for(TFVector v: tfVector){
            Map<String, Integer> map= new HashMap<String, Integer>();
            int[] var= v.getVector();
            for(int var1=0; var1<var.length; var1++){
                if(var[var1]!=0.0){
                    map.put(this.wordList.getWord(var1), var[var1]);
                }
            }
            m= merge(map, m);
        }
        return m;
    }

    public Map<String, Double> getWordsPopular() {
        Map<String, Double> m = new HashMap<String, Double>();
        for (TFVector tf : tfVector) {
//            Map<String, Double> map= new HashMap<String, Double>();
            int[] var = tf.getVector();
            for (int var1 = 0; var1 < var.length; var1++) {
                if (var[var1] != 0.0) {
//                    map.put(this.wordList.getWord(var1), var[var1]);
                    if (m.containsKey(this.wordList.getWord(var1))) {
                        double t = m.get(this.wordList.getWord(var1));
                        m.put(this.wordList.getWord(var1), t + 1);
                    } else {
                        m.put(this.wordList.getWord(var1), 1.0);
                    }
                }
            }
        }
        for (String s : m.keySet()) {
            m.put(s, m.get(s) / tfVector.size());
        }
        return m;
    }

    public  Map<String, Double> merge2(Map<String, Double> map1, Map<String, Double> map2){
        for (String key: map1.keySet()) {
            double value1= map1.get(key);
            if(map2.containsKey(key)){
                double value2= map2.get(key);
                map2.put(key, value1+value2);
            }else {
                map2.put(key, value1);
            }
        }
        return map2;
    }

    public  Map<String, Integer> merge(Map<String, Integer> map1, Map<String, Integer> map2){
        for (String key: map1.keySet()) {
            int value1= map1.get(key);
            if(map2.containsKey(key)){
                int value2= map2.get(key);
                map2.put(key, value1+value2);
            }else {
                map2.put(key, value1);
            }
        }
        return map2;
    }
    public String toString(){
        String s="";
        for(WVTWordVector v: wordVectors){
            double[] var= v.getValues();
            for(int var1=0; var1<var.length; var1++){
                if(var[var1]!=0.0){
                    s+=" "+var1+":"+var[var1];
                }
            }
            s+="\n";
        }
        return s;
    }

    public void printTF() {
        String s = "";
        for (TFVector v : tfVector) {
            int[] var = v.getVector();
            for (int var1 = 0; var1 < var.length; var1++) {
                if (var[var1] != 0.0) {
                    s += " " + var1 + ":" + var[var1];
                }
            }
            s += "\n";
        }
        System.out.println(s);
    }
}
