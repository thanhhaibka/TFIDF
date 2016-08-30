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
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pc on 29/07/2016.
 */
public class Token {
    static VietTokenizer vietTokenizer = new VietTokenizer();
    private static String normalize(String var1) {
        String var2 = var1.toLowerCase();
        return var2;
    }

    public User getUserVector(List<String> stringList) throws WVToolException, IOException {
        User user = new User();
        String sentences;
        ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
        StopWords stopWords = new StopWords();
        Map<String, String> mapWords= new HashMap<String, String>();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            sentences = stringList.get(i).replaceAll("\\<.*?>",""); //remove html
            System.out.println(sentences);
            if(sentences!=null) {
                String[] var1 = vietTokenizer.tokenize(sentences);
                for (String var2 : var1) {
                    String[] var3 = var2.split("\\s+");
                    for (String var4 : var3) {
                        if (!stopWords.isStopword(var4)) {
                            mapWords.put(normalize(var4), var4);
                            t.add(normalize(var4));
                        }
                    }
                }
                tokens.add(t);
            }
        }
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn = new WVToolNew(false);
        WVTWordList wordList = wvtn.createWordList(tokens, config);
        user.setWordList(wordList);
        user.setWordVectors(wvtn.createVector(tokens, config, wordList));
        user.setMapWords(mapWords);
//        user.setTfVector(wvtn.createVector1(tokens,config, wordList));
        return user;
    }

    public static void main (String[] args) throws SQLException, ClassNotFoundException, WVToolException, IOException {
        Token token= new Token();
//        ConnectMySQL.getInstance();
        Cassandra.getInstance();
        UserProfiling userProfiling= new UserProfiling("6773553201908336650");
        List<String> stringList1 = new ArrayList<String>();
//        stringList1.add(ConnectMySQL.getContentFromNewsIDByMYSQL("20160824142401973"));
        setProfile(userProfiling, token);
//        Map<String, Integer> mapNews = news.getMap();
//        System.out.println(getSimilar(setProfile(userProfiling, token), mapNews));
        System.out.println("Done");
    }

    public static Map<String, Double> setProfile(UserProfiling userProfiling, Token token) throws SQLException, ClassNotFoundException, WVToolException, IOException {
        List<String> stringList= new ArrayList<String>();
        userProfiling.setLongTerm(10);
        int i=0;
        Set<Document> docs = new HashSet(userProfiling.getLongTerm());
        for (Document d : docs) {
            d.setContent();
            System.out.println(i + " " + d.getNewsID() + " "+ d.getTitle());
            String s = d.getContent();
            if (s.isEmpty()) {
                userProfiling.removeLongTerm(d);
            } else {
                stringList.add(s);
            }
            ++i;
        }
        User user= token.getUserVector(stringList);
        System.out.println(user.getMapWords());
        Map<String, Double> mapUser = user.getMapTFIDF();
//        System.out.println(printWordList(user.getWordList()));
        System.err.println(mapUser);
        return getTop1002(mapUser);
    }

    public static String printWordList(WVTWordList v){
        String s="";
        for (int var=0;var< v.getNumWords(); var++) {
            s+=v.getWord(var)+" ";
        }
        return s;
    }


    public static Map<String, Double> getTop1002(Map<String, Double> mapUser) {
        Map<String, Double> map =
                sortByValues2(mapUser);
        Map<String, Double> top100 = new HashMap<String, Double>();
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        int i = 0;
        while (iterator2.hasNext() && i < 100) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            top100.put((String) me2.getKey(), (Double) me2.getValue());
            i++;
        }
        System.err.println(top100);
        return top100;
    }

    private static Map<String, Double> sortByValues2(Map<String, Double> map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Collections.reverse(list);
        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static Map<String, Integer> getTop100(Map<String, Integer> mapUser) {
        Map<String, Integer> map =
                sortByValues(mapUser);
        Map<String, Integer> top100 = new HashMap<String, Integer>();
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        int i = 0;
        while (iterator2.hasNext() && i < 100) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            top100.put((String) me2.getKey(), (Integer) me2.getValue());
            i++;
        }
        System.err.println(top100);
        return top100;
    }

    private static Map<String, Integer> sortByValues(Map<String, Integer> map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Collections.reverse(list);
        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    public static double getSimilar(Map<String, Integer> mapUser, Map<String, Integer> mapNews) {
        for (String s1 : mapNews.keySet()) {
            if (mapUser.containsKey(s1)) {
                //Do nothing
            } else {
                mapUser.put(s1, 0);
            }
        }
        for (String s1 : mapUser.keySet()) {
            if (mapNews.containsKey(s1)) {
                //Do nothing
            } else {
                mapNews.put(s1, 0);
            }
        }
        double s = 0;
        for (String s1 : mapUser.keySet()) {
            s += mapUser.get(s1);
        }
        for (String s1 : mapUser.keySet()) {
            s += mapNews.get(s1);
        }
        double sim = 0;
        double max = 0;
        double duplicate = 0;

        for (String s1 : mapUser.keySet()) {
            for (String s2 : mapNews.keySet()) {
                if (s1.equals(s2)) {
                    if(max<mapNews.get(s2) + mapUser.get(s1));
                    max= mapNews.get(s2) + mapUser.get(s1);
                }
            }
        }

        for (String s1 : mapUser.keySet()) {
            for (String s2 : mapNews.keySet()) {
                if (mapUser.get(s1) == 0 || mapNews.get(s2) == 0) {

                } else {
                    if (s1.equals(s2)) {
                        duplicate += (mapNews.get(s2) + mapUser.get(s1)) / 2/ max;
                        break;
                    }
                }
            }
        }
        sim = Math.sqrt(duplicate/mapNews.size());
        return sim;
    }

    public double cosin(Map<String, Integer> mapUser, Map<String, Integer> mapNews) {
        for (String s1 : mapNews.keySet()) {
            if (mapUser.containsKey(s1)) {
                //Do nothing
            } else {
                mapUser.put(s1, 0);
            }
        }
        for (String s1 : mapUser.keySet()) {
            if (mapNews.containsKey(s1)) {
                //Do nothing
            } else {
                mapNews.put(s1, 0);
            }
        }
        double product = 0, square1 = 0, square2 = 0;
        for (String s1 : mapNews.keySet()) {
            square1 += Math.pow(mapNews.get(s1), 2);
            for (String s2 : mapUser.keySet()) {
                if (s1.equals(s2)) {
                    square2 += Math.pow(mapUser.get(s2), 2);
                    product += mapNews.get(s1) * mapUser.get(s2);
                }
            }
        }
        return product / Math.sqrt(square1 * square2);
    }
}
