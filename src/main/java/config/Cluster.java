package config;

import edu.udo.cs.wvtool.main.WVTWordVector;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pc on 28/07/2016.
 */
public class Cluster {
    public ArrayList<WVTWordVector> cluster;
    public WVTWordVector centroid;

    public Cluster(){
        cluster= new ArrayList<WVTWordVector>();
    }

    public int getSize(){
        return cluster.size();
    }

}
