package config;

import edu.udo.cs.wvtool.main.WVTWordVector;

import java.util.ArrayList;

/**
 * Created by pc on 28/07/2016.
 */
public class UserProfile extends User {
    public Cluster[] clusters;

    public UserProfile(User user){
        clusters= new Cluster[2];
        ArrayList<WVTWordVector> wordVectors= user.getWordVectors();
        for(int i=0; i<2; i++){
            Cluster cluster= new Cluster();
            cluster.centroid = wordVectors.get(i);
            cluster.cluster.add(wordVectors.get(i));
            clusters[i]=cluster;
        }
    }

    public String toString(){
        String s="";
        for(int i=0; i<10; i++){
            s+=i+":"+clusters[i].getSize()+ "; ";
        }
        return s;
    }
}
