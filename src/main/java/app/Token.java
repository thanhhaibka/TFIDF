package app;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import config.Document;
import config.User;
import connectDB.Cassandra;
import connectDB.ConnectMySQL;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import stemmer.StopWords;
import user.UserProfiling;
import wvtNew.WVToolNew;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pc on 29/07/2016.
 */
public class Token {

    public Token(){
        VCTokenizer.getInstance();
    }
    private static String normalize(String var1) {
        String var2 = var1.toLowerCase();
        return var2;
    }

    public User getUserVector(List<String> stringList) throws WVToolException, IOException {
        User user = new User();
        String sentences;
        ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
        StopWords stopWords = new StopWords();
        Map<String, String> mapWords = new HashMap<String, String>();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            sentences = stringList.get(i).replaceAll("\\<.*?>", "").replaceAll("\\[.*?]", " "); //remove html
//            System.out.println(sentences);
            if (sentences != null) {
                try {
                    String[] var1 = VCTokenizer.getInstance().getSegmenter().segment(sentences).split(" ");
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
                } catch (Exception e) {

                }
            }
        }
//        System.out.println(tokens);
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn = new WVToolNew(false);
        WVTWordList wordList = wvtn.createWordList(tokens, config);
        user.setWordList(wordList);
        user.setWordVectors(wvtn.createVector(tokens, config, wordList));
        user.setTfVector(wvtn.createVector1(tokens, config, wordList));
        user.setMapWords(mapWords);
        return user;
    }

    public User getUserVector1(List<Document> documentList) throws WVToolException, IOException {
        User user = new User();
        String sentences;
        ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
        StopWords stopWords = new StopWords();
        Map<String, String> mapWords= new HashMap<String, String>();
        for (int i = 0; i < documentList.size(); i++) {
            Document document = documentList.get(i);
            if (!document.getMapWords().isEmpty()) {
//                System.err.println("true");
                ArrayList<String> t = new ArrayList<String>();

                for (String s : document.getMapWords().keySet()) {
                    mapWords.put(s, document.getMapPairs().get(s));
                    for (int p = 0; p < document.getMapWords().get(s); p++) {
                        t.add(s);
                    }
                }
                tokens.add(t);
            } else {
                ArrayList<String> t = new ArrayList<String>();
                sentences = documentList.get(i).getContent().replaceAll("\\<.*?>", " ").replaceAll("\\[.*?]", " "); //remove html
//                System.out.println(document.getNewsID());
                if (sentences != null) {
                    try {
                        String[] var1 = VCTokenizer.getInstance().getSegmenter().segment(sentences).split(" ");
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
                    } catch (Exception e) {

                    }
                }
            }
        }
//        System.out.println(tokens);
        WVTConfiguration config = new WVTConfiguration();
        WVToolNew wvtn = new WVToolNew(false);
        WVTWordList wordList = wvtn.createWordList(tokens, config);
        user.setWordList(wordList);
        user.setWordVectors(wvtn.createVector(tokens, config, wordList));
        user.setTfVector(wvtn.createVector1(tokens,config, wordList));
        user.setMapWords(mapWords);
        return user;
    }

    public Map<String, Integer> getM(String newsID, Token token){
        List<String> stringList = new ArrayList<String>();
        stringList.add(Cassandra.getInstance().getTextArticle(newsID));
        Map<String, Integer> mapNews= new HashMap<String, Integer>();
        try{
            User news= token.getUserVector(stringList);
            mapNews= news.getMap();
//            news.printWordList();
        }catch (Exception e){

        }
        return mapNews;
    }

    public static void main (String[] args) throws SQLException, ClassNotFoundException, WVToolException, IOException {
        Token token= new Token();
//        ConnectMySQL.getInstance();
//        Cassandra.getInstance();
//        long t= System.currentTimeMillis();
//        UserProfiling userProfiling= new UserProfiling();
//        Map<String, Double> keys= token.getLongTerm(userProfiling, token, "2885620731906312862", "kenh14.vn", 1);
//        System.out.println(System.currentTimeMillis()- t);

//        System.out.println(Cassandra.getInstance().getMapWord("20160824121617934"));

        Map<String, Integer> getKeyWords= new HashMap<String, Integer>();
        List<String> newsIDs = ConnectMySQL.getInstance().getNewNewsInNumDay(30);
        System.out.println(newsIDs.size());
        for (String newsId : newsIDs) {
            System.out.println(newsId);
            if (Cassandra.getInstance().getMap(newsId)) {
                token.getKeyWords(newsId, token);
            }
        }
//        System.out.println(token.cosinImprove(mapUser, getKeyWords));

//        token.insertToCass("6773553201908336650", "kenh14.vn", token);
//        List<String> stringList= new ArrayList<String>();
//        System.out.println(userProfiling.getLongTerm());
//        userProfiling.setLongTerm2(2000);
//        int i=0;
//        Set<Document> docs = new HashSet(userProfiling.getLongTerm());
//        for (Document d : docs) {
//            d.setContent();
//            d.setTopics();
////            System.out.println(i);
////            String s = d.getContent();
////            if (s.isEmpty()) {
////                userProfiling.removeLongTerm(d);
////            } else {
////                stringList.add(s);
////            }
////            ++i;
//        }
//        Map<String, List<Document>> userDoc= new HashMap<>();
//        for(Document d: docs){
//
//            Topic max2= new Topic();
//            List<Topic> topics= d.getTopics();
//            Topic max1= topics.get(0);
//            for(int j=1; j<30; j++){
//                if(max1.compareTo(topics.get(j))>1){
//
//                }
//            }
//        }
//
//        User user= token.getUserVector(stringList);
////        user.printTF();
//        Map<String, Double> m= user.getWordsPopular();
////        System.out.println(m);
//        File file = new File("resources/result/stopwords.txt");
//        FileOutputStream f = new FileOutputStream(file);
//        ObjectOutputStream s = new ObjectOutputStream(f);
//        s.writeObject(getTopN(m, 200));
//        s.flush();
//        System.out.println(getTopN(m, 200));
        System.out.println("Done");
    }

//    public void insertToCass(String guid, String domain, Token token) {
//        UserProfiling userProfiling = new UserProfiling();
//        User user = token.setUser(userProfiling, token, guid, domain);
//        Map<String, Double> mapIDF = getTop1002(user.getMapTFIDF());
//        Map<String, String> mapKeys = new HashMap<>();
//        Map<String, String> mapKeysTemp = user.getMapWords();
//        for (String s : mapIDF.keySet()) {
//            if (mapKeysTemp.containsKey(s)) {
//                mapKeys.put(s, mapKeysTemp.get(s));
//            }
//        }
//
//        System.out.println(mapKeys);
//        String guid_domain = guid + "_" + domain;
////        com.datastax.driver.core.Statement exampleQuery = QueryBuilder.insertInto("othernews", "guid_key_word").value("guid_domain", guid_domain)
////                .value("keyword", mapIDF).value("mapword", mapKeys).ifNotExists();
////        Cassandra.getInstance().getSession().execute(exampleQuery);
//    }

    public Map<String, Double> getLongTerm(UserProfiling userProfiling, Token token, String guid, String domain, int number) {
        int M = 30 / number;
        double[] weights = {1, 0.9, 0.8, 0.7, 0.6, 0.5};
        Map<String, Double>[] temp = new Map[number];
//        User []user= new User[N];
        for (int i = 0; i < number; i++) {
            User user = token.setUser(userProfiling, token, guid, domain, 0, 7);
            temp[i] = getTopN(user.getMapTFIDF(), 100);
        }
        Set<String> words = new HashSet<>();
        for (int i = 0; i < number; i++) {
            words.addAll(temp[i].keySet());
        }
        Map<String, Double> longTermWords = new HashMap<>();
        for (String s : words) {
            double sum = 0;
            for (int i = 0; i < number; i++) {
                if (temp[i].containsKey(s)) sum += 1 * weights[i];
            }
            longTermWords.put(s, sum / number);
        }
        System.err.println(longTermWords);
        System.out.println(getTop1002(longTermWords));
        return getTop1002(longTermWords);
    }

    public User setUser(UserProfiling userProfiling, Token token, String guid, String domain, int begin, int end) {
        List<Document> stringList = new ArrayList<Document>();
        userProfiling.setLongTerm3(guid, domain, begin, end);
        Set<Document> docs = new HashSet(userProfiling.getLongTerm());
        for (Document d : docs) {
            d.setContent();
//            System.out.println(d.getNewsID());
            String s = d.getContent();
            if (s.isEmpty()) {
                userProfiling.removeLongTerm(d);
            } else {
                d.setMapWords();
                d.setMapPairs();
                stringList.add(d);
            }
        }
        User user = new User();
        try {
            user = token.getUserVector1(stringList);
        } catch (Exception e) {

        }
        return user;
    }

    private Map<String, Double> convert(Map<String, Integer> m) {
        Map<String, Double> m1 = new HashMap<>();
        for (String s : m.keySet()
                ) {
            m1.put(s, 1.0 * m.get(s));
        }
        return m1;
    }

    public Map<String, Integer> getKeyWords(String newsId, Token token){
        if (newsId == null || newsId.length() == 0) return null;
        Map<String, Integer> getKeyWords= new HashMap<String, Integer>();
        List<String> stringList= new ArrayList<String>();
        String s= Cassandra.getInstance().getTextArticle(newsId);
        User user = new User();
        if(s==""){
            try{
                s= ConnectMySQL.getInstance().getContentFromNewsIDByMYSQL(newsId);
                stringList.add(s);
                user= token.getUserVector(stringList);
                Map<String, Integer> m= getTopNInt(user.getMap(), 100);
                Map<String, String> mapKeysTemp = user.getMapWords();
                Map<String, String> mapKeys= new HashMap<>();
                for (String s1 : m.keySet()) {
                    if (mapKeysTemp.containsKey(s1)) {
                        mapKeys.put(s1, mapKeysTemp.get(s1));
                    }
                }
                String[] s1= ConnectMySQL.getInstance().getOther(newsId);
                token.insert(newsId, convert(m), s1[2], s1[1], s1[0], s1[3], mapKeys);
            }catch (Exception e){

            }
        }else{
            try{
                stringList.add(s);
                user= token.getUserVector(stringList);
                Map<String, Integer> m= getTopNInt(user.getMap(), 100);
                Map<String, String> mapKeysTemp = user.getMapWords();
                Map<String, String> mapKeys= new HashMap<>();
                for (String s1 : m.keySet()) {
                    if (mapKeysTemp.containsKey(s1)) {
                        mapKeys.put(s1, mapKeysTemp.get(s1));
                    }
                }
//                System.out.println(m);
                token.update(newsId, convert(m), mapKeys);
            }catch (Exception e){

            }

        }
        return getKeyWords;
    }

    public void insert(String newsId, Map<String, Double> keyWords, String content, String sapo, String title, String url, Map<String, String> mapPair) {
        com.datastax.driver.core.Statement exampleQuery = QueryBuilder.insertInto("othernews", "newsurl").value("newsid", Long.parseLong(newsId))
                .value("title", title).value("url", url).value("mapword", mapPair).value("content", content).value("sapo", sapo).value("keyword",keyWords).ifNotExists();
        Cassandra.getInstance().getSession().execute(exampleQuery);
    }

    public void update(String newsId, Map<String, Double> keyWords, Map<String, String> mapPair) {
        com.datastax.driver.core.Statement exampleQuery = QueryBuilder.update("othernews", "newsurl")
                .with(QueryBuilder.set("keyword", keyWords)).and(QueryBuilder.set("mapword", mapPair)).where(QueryBuilder.eq("newsid", Long.parseLong(newsId)));
        Cassandra.getInstance().getSession().execute(exampleQuery);
    }

//    public static Map<String, Double> setProfile(UserProfiling userProfiling, Token token, String guid, String domain) throws SQLException, ClassNotFoundException, WVToolException, IOException {
//        List<String> stringList= new ArrayList<String>();
//        userProfiling.setLongTerm3(guid, domain, 7);
//
//        Set<Document> docs = new HashSet(userProfiling.getLongTerm());
//        for (Document d : docs) {
//            System.out.println(d.getNewsID()+" "+d.getTitle());
//            d.setContent();
//            String s = d.getContent();
//            if (s.isEmpty()) {
//                userProfiling.removeLongTerm(d);
//            } else {
//                stringList.add(s);
//            }
//        }
//        User user= token.getUserVector(stringList);
//        Map<String, Double> mUser = getTopN(user.getMapTFIDF(), 100);
//        return mUser;
//    }

    public static String printWordList(WVTWordList v){
        String s="";
        for (int var=0;var< v.getNumWords(); var++) {
            s+=v.getWord(var)+" ";
        }
        return s;
    }


    public static Map<String, Double> getTopN(Map<String, Double> mapUser, int N) {
        Map<String, Double> map =
                sortByValues2(mapUser);
        Map<String, Double> top100 = new HashMap<String, Double>();
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        int i = 0;
        while (iterator2.hasNext() && i < N) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            top100.put((String) me2.getKey(), (Double) me2.getValue());
            i++;
        }
//        System.err.println(top100);
        return top100;
    }

    public static Map<String, Integer> getTopNInt(Map<String, Integer> mapUser, int N) {
        Map<String, Integer> map =
                sortByValues(mapUser);
        Map<String, Integer> top100 = new HashMap<String, Integer>();
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        int i = 0;
        while (iterator2.hasNext() && i < N) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            top100.put((String) me2.getKey(), (Integer) me2.getValue());
            i++;
        }
        return top100;
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
//        System.err.println(top100);
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

    public double cosinImprove(Map<String, Double> mapUser, Map<String, Integer> mapNews) {

        Map<String, Double> mapNewsTemp = new HashMap<>();
        for (String s1 : mapNews.keySet()) {
            if (mapUser.containsKey(s1)) {
                //Do nothing
                System.err.print(s1+" ");
            } else {
                mapUser.put(s1, 0.0);
            }
        }
        for (String s1 : mapUser.keySet()) {
            if (mapNews.containsKey(s1)) {
                //Do nothing
            } else {
                mapNews.put(s1, 0);
            }
        }
        double sum1 = 0, sum2 = 0;
        for (String s1 : mapUser.keySet()) {
            sum1 += mapUser.get(s1);
            sum2 += mapNews.get(s1);
        }
        for (String s1 : mapUser.keySet()) {
            mapUser.put(s1, mapUser.get(s1) / sum1);
            mapNewsTemp.put(s1, mapNews.get(s1) / sum2);
        }
        System.err.println(mapUser);
        System.out.println(mapNewsTemp);
        int n = mapUser.size();
        double product = 0, square1 = 0, square2 = 0;
        for (String s1 : mapNews.keySet()) {
//            if(mapNewsTemp.get(s1)==0){
//                product += Math.cos(Math.PI/2 * (Math.abs(mapUser.get(s1)/(mapUser.get(s1)+1))));
//            }else if(){
//                product += Math.cos(Math.PI/2 * (Math.abs(mapUser.get(s1)/(mapNews.get(s1)+1))));
//            }
        }
        return product / n;
    }
}
