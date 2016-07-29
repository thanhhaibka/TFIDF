package wvtNew;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.charmapper.WVTCharConverter;
import edu.udo.cs.wvtool.generic.inputfilter.WVTInputFilter;
import edu.udo.cs.wvtool.generic.loader.WVTDocumentLoader;
import edu.udo.cs.wvtool.generic.output.WVTOutputFilter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.generic.vectorcreation.WVTVectorCreator;
import edu.udo.cs.wvtool.generic.wordfilter.WVTWordFilter;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import vn.hus.nlp.tokenizer.VietTokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pc on 26/07/2016.
 */
public class WVToolNew extends WVTool {
    public WVToolNew(boolean b) {
        super(b);
    }

    public WVTWordList createWordList(ArrayList<ArrayList<String>> var1, WVTConfiguration var2) throws WVToolException {
        return this.createWordList(var1, var2, new LinkedList(), true);
    }

    public WVTWordList createWordList(ArrayList<ArrayList<String>> tokens, WVTConfiguration var2, List var3, boolean var4) throws WVToolException {
        WVTWordList var5 = new WVTWordList(var3, tokens.size());
        var5.setAppendWords(var4);
        var5.setUpdateOnlyCurrent(false);
        WVTDocumentLoader var6 = null;
        WVTInputFilter var7 = null;
        WVTCharConverter var8 = null;
        WVTTokenizer var9 = null;
        WVTWordFilter var10 = null;
        WVTStemmer var11 = null;

        for (int i = 0; i < tokens.size(); i++) {
            WVTDocumentInfo var13 = new WVTDocumentInfo(i + 1 + "", "", "", "", i);
            for (String s : tokens.get(i)
                    ) {
                var5.addWordOccurance(s);
            }
            var5.closeDocument(var13);
        }

        return var5;
    }

//    public void createVectors(ArrayList<ArrayList<String>> tokens, WVTConfiguration var2, WVTWordList var3) throws WVToolException {
//        var3.setAppendWords(false);
//        var3.setUpdateOnlyCurrent(true);
//        WVTDocumentLoader var4 = null;
//        WVTInputFilter var5 = null;
//        WVTCharConverter var6 = null;
//        VietTokenizer var7 = null;
//        WVTWordFilter var8 = null;
//        WVTStemmer var9 = null;
//        WVTVectorCreator var10 = null;
//        WVTOutputFilter var11 = null;
//        for (int i = 0; i < tokens.size(); i++) {
//            WVTDocumentInfo var13 = new WVTDocumentInfo(i + 1 + "", "", "", "", i);
//            var11 = (WVTOutputFilter) var2.getComponentForStep("output", var13);
//            var10 = (WVTVectorCreator) var2.getComponentForStep("vectorcreation", var13);
//
//            for (String s : tokens.get(i)
//                    ) {
//                var3.addWordOccurance(s);
//            }
//            var11.write(var10.createVector(var3.getFrequenciesForCurrentDocument(), var3.getTermCountForCurrentDocument(), var3, var13));
//            var3.closeDocument(var13);
//        }
//    }

    public ArrayList<WVTWordVector> createVectors(ArrayList<ArrayList<String>> tokens, WVTConfiguration var2, WVTWordList var3) throws WVToolException {
        var3.setAppendWords(false);
        var3.setUpdateOnlyCurrent(true);
        WVTDocumentLoader var4 = null;
        WVTInputFilter var5 = null;
        WVTCharConverter var6 = null;
        VietTokenizer var7 = null;
        WVTWordFilter var8 = null;
        WVTStemmer var9 = null;
        WVTVectorCreator var10 = null;
        WVTOutputFilter var11 = null;
        WVTWordVector var12= null;
        ArrayList<WVTWordVector> var15=null;

        for (int i = 0; i < tokens.size(); i++) {
            WVTDocumentInfo var13 = new WVTDocumentInfo(i + 1 + "", "", "", "", i);
//            var11 = (WVTOutputFilter) var2.getComponentForStep("output", var13);
            var10 = (WVTVectorCreator) var2.getComponentForStep("vectorcreation", var13);

            for (String s : tokens.get(i)
                    ) {
                var3.addWordOccurance(s);
            }
            var12= var10.createVector(var3.getFrequenciesForCurrentDocument(), var3.getTermCountForCurrentDocument(), var3, var13);
//            var11.write(var12);
            var3.closeDocument(var13);
            var15.add(var12);
        }
        return var15;
    }

    public ArrayList<WVTWordVector> createVector(ArrayList<ArrayList<String>> tokens, WVTConfiguration var3, WVTWordList var4) throws WVToolException {
        var4.setAppendWords(false);
        var4.setUpdateOnlyCurrent(true);
        WVTCharConverter var5 = null;
        WVTTokenizer var6 = null;
        WVTWordFilter var7 = null;
        WVTStemmer var8 = null;
        WVTVectorCreator var9 = null;
        WVTWordVector var10 = null;
        ArrayList<WVTWordVector> var15= new ArrayList<WVTWordVector>();
        for (int i = 0; i < tokens.size(); i++) {
            WVTDocumentInfo var13 = new WVTDocumentInfo(i + 1 + "", "", "", "", i);
            var9 = (WVTVectorCreator) var3.getComponentForStep("vectorcreation", var13);

            for (String s : tokens.get(i)
                    ) {
                var4.addWordOccurance(s);
            }
            var10 = var9.createVector(var4.getFrequenciesForCurrentDocument(), var4.getTermCountForCurrentDocument(), var4, var13);
            var15.add(var10);
            var4.closeDocument(var13);
        }
        return var15;
    }

    public WVTWordVector createVector(String var1, WVTWordList var2) throws WVToolException {
        WVTConfiguration var3 = new WVTConfiguration();
        var3.setConfigurationRule("vectorcreation", new WVTConfigurationFact(new TFIDF()));
        return this.createVector(var1, new WVTDocumentInfo("", "", "", ""), var3, var2);
    }

}
