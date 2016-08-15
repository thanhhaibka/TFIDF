package app;

import clustering.KMean;
import config.Document;
import config.User;
import connectDB.Cassandra;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import stemmer.StopWords;
import user.UserProfiling;
import vn.hus.nlp.tokenizer.VietTokenizer;
import wvtNew.WVToolNew;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by pc on 29/07/2016.
 */
public class Token {
    private static String normalize(String var1) {
        String var2 = var1.toLowerCase();
        return var2;
    }

    public User getUserVector(List<String> stringList) throws WVToolException, IOException {
        User user = new User();
        String sentences;
        ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
        StopWords stopWords = new StopWords();
        VietTokenizer vietTokenizer = new VietTokenizer();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            sentences = stringList.get(i).replaceAll("\\<.*?>","") ;
            String[] var1 = vietTokenizer.tokenize(sentences);
            for (String var2 : var1) {
                String[] var3 = var2.split("\\s+");
                for (String var4 : var3) {
                    if (!stopWords.isStopword(var4)) {
                        t.add(normalize(var4));
                    }
                }
            }
            tokens.add(t);
        }
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn = new WVToolNew(false);
        WVTWordList wordList = wvtn.createWordList(tokens, config);
        wordList.pruneByFrequency(2, 10);
        user.setWordList(wordList);
        wordList.storePlain(new FileWriter("/home/pc/Documents/TFIDF/resources/result/wordlistTest.txt"));
        user.setWordVectors(wvtn.createVector(tokens, config, wordList));
//        user.setTfVector(wvtn.createVector1(tokens,config, wordList));
        return user;
    }

    public static void main(String args[]) throws WVToolException, IOException {
        Token token= new Token();
        UserProfiling userProfiling= new UserProfiling("0");
        List<String> stringList= new ArrayList<String>();
        userProfiling.setLongTerm(10);
        int i=0;
        for(Document d: userProfiling.getLongTerm()){
            d.setContent();
            System.out.println(i+" "+d.getNewsID());
            stringList.add(d.getContent());
            ++i;
        }
        User user= token.getUserVector(stringList);
        KMean kMean= new KMean(user, 3, 10000, 0.01);
        System.out.println(kMean.getUserProfile());
        System.out.println("Done");
    }
}
