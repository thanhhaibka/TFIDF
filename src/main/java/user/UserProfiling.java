package user;

import config.Document;
import config.Interest;
import config.Topic;
import config.User;
import connectDB.Cassandra;

import javax.rmi.CORBA.Util;
import java.util.*;

import static java.util.Collections.sort;

/**
 * Created by pc on 04/08/2016.
 */
public class UserProfiling extends User {
    private String userID;
    private List<Document> shortTerm;
    private List<Document> longTerm;
    private List<Interest> interests;
    private double period;

    public UserProfiling( String userID){
        shortTerm= new ArrayList<Document>();
        longTerm= new ArrayList<Document>();
//        interests= new ArrayList<Interest>();
        this.userID= userID;
    }

    public UserProfiling(){

    }

    public void setLongTerm(int num){
        List<Document> unsorted = Cassandra.getInstance().getDocs(userID);
//        if(num <= unsorted.size()){
//            sort(unsorted);
//            for(int i=0; i<num; i++){
//                longTerm.add(unsorted.get(i));
//            }
//        }else{
            longTerm.addAll(unsorted);
//        }
//        setTF();
    }

    public void setLongTerm3(String guid, String domain, int N){
        this.longTerm= Cassandra.getInstance().getDocs(guid, domain, N);
    }

    public void setLongTerm2(int n){
        this.longTerm= Cassandra.getInstance().getDocsLimitT(n);
    }

    public void removeLongTerm(Document d) {
        longTerm.remove(d);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Document> getShortTerm() {
        return shortTerm;
    }

    public void setShortTerm(List<Document> shortTerm) {
        this.shortTerm = shortTerm;
    }

    public List<Document> getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(List<Document> longTerm) {
        this.longTerm = longTerm;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public static void main(String args[]){
        new UserProfiling("0").setLongTerm(10);
    }
}
