package config;

import connectDB.Cassandra;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 04/08/2016.
 */
public class Document implements Comparable<Document> {
    private String content;
    private String newsID;
    private Date accessTime;
    private String title;
    private List<Topic> topics;
    private boolean isSet;
    private Map<String, Double> mapWords;

    public void setContent(String content) {
        this.content = content;
    }

    public void setMapPairs(Map<String, String> mapPairs) {

        this.mapPairs = mapPairs;
    }

    public void setMapWords(Map<String, Double> mapWords) {
        this.mapWords = mapWords;
    }

    private Map<String, String> mapPairs;

    public Map<String, String> getMapPairs() {
        return mapPairs;
    }

    public void setMapPairs() {
        this.mapPairs = Cassandra.getInstance().getWordPair(newsID);;
    }

    public Map<String, Double> getMapWords() {
        return mapWords;
    }

    public void setMapWords() {
        this.mapWords = Cassandra.getInstance().getMapWord(newsID);
    }

    public Document(String newsID, Date accessTime){
        this.newsID= newsID;
        this.accessTime= accessTime;
        this.isSet= false;
    }

    public void setTopics(){
        this.topics= Cassandra.getInstance().getTopics(newsID);
    }

    public List<Topic> getTopics(){
        return topics;
    }

    public String getTitle() {
        return Cassandra.getInstance().getTitle(newsID);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Document(String newsID){
        this.newsID= newsID;
        this.isSet= false;
    }

    public Document(String newsID, Map<String, Double> keyword, Map<String, String> mapword){
        this.newsID= newsID;
        this.mapWords= keyword;
        this.mapPairs= mapword;
    }

    public void setContent() {
        this.content= Cassandra.getInstance().getTextArticle(newsID);
        this.isSet= true;
    }

//    public void setTi() {
//        this.content= Cassandra.getInstance().getTextArticle(newsID);
//    }

    public String getContent(){
        return content;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public int compareTo(Document doc){
        return (this.accessTime.compareTo(doc.accessTime)<0? 1:(this.accessTime.compareTo(doc.accessTime)==0?0:1));
    }

    @Override
    public boolean equals(Object o){
        if(o==null||o.getClass()!=this.getClass()) return false;
        Document d= (Document)o;
        if(d.getNewsID().equals(newsID)) return true;
        return false;
    }

    public String toString(){
        return accessTime+" "+newsID;
    }
}
