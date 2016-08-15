package config;

import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pc on 28/07/2016.
 */
public class User {
    private ArrayList<WVTWordVector> wordVectors;
    private WVTWordList wordList;
    private ArrayList<TFVector> tfVector;

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

    public void setWordList(WVTWordList wordList) {
        this.wordList = wordList;
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
}
