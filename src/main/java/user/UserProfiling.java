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

    public void setLongTerm(int num){
        Cassandra cassandra= new Cassandra();
        List<Document> unsorted= (List)cassandra.getDocs(userID);
        sort(unsorted);
        if(num <= unsorted.size()){
            for(int i=0; i<num; i++){
                longTerm.add(unsorted.get(i));
            }
        }else{
            longTerm.addAll(unsorted);
        }
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
