package config;

import edu.udo.cs.wvtool.main.WVTWordVector;

import java.util.ArrayList;

/**
 * Created by pc on 28/07/2016.
 */
public class UserProfile extends User {
    public Cluster[] clusters;
    private int num;

    public UserProfile(User user, int num){
        this.num= num;
        clusters= new Cluster[num];
        ArrayList<WVTWordVector> wordVectors= user.getWordVectors();
        for(int i=0; i<num; i++){
            Cluster cluster= new Cluster();
            cluster.centroid = wordVectors.get(i);
//            memOfCluster.memOfCluster.add(wordVectors.get(i));
            clusters[i]=cluster;
        }
    }

    public String toString(){
        String s="";
        for(int i=0; i<num; i++){
            s+=i+":";
            for(WVTWordVector w: clusters[i].memOfCluster){
                s+=w.getDocumentInfo().getClassValue()+";";
            }
            s+="\n";
        }
        return s;
    }
}
