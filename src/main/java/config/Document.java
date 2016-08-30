package config;

import connectDB.Cassandra;

import java.util.Date;

/**
 * Created by pc on 04/08/2016.
 */
public class Document implements Comparable<Document> {
    private String content;
    private String newsID;
    private Date accessTime;
    private String title;

    public Document(String newsID, Date accessTime){
        this.newsID= newsID;
        this.accessTime= accessTime;
    }

    public String getTitle() {
        return Cassandra.getInstance().getTitle(newsID);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Document(String newsID){
        this.newsID= newsID;
    }

    public void setContent() {
        this.content= Cassandra.getInstance().getTextArticle(newsID);
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

    public String toString(){
        return accessTime+" "+newsID;
    }
}
