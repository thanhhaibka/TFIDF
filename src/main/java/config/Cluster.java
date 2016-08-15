package config;

import edu.udo.cs.wvtool.main.WVTWordVector;

import java.util.ArrayList;

/**
 * Created by pc on 28/07/2016.
 */
public class Cluster {
    public ArrayList<WVTWordVector> memOfCluster;
    public WVTWordVector centroid;

    public Cluster(){
        memOfCluster = new ArrayList<WVTWordVector>();
    }

    public int getSize(){
        return memOfCluster.size();
    }

    public void clear(){
        memOfCluster.clear();
    }
}
