package test;

import connectDB.Cassandra;

/**
 * Created by pc on 06/09/2016.
 */
public class QueryToFile {
    public static void main(String args[]){
        System.out.println(Cassandra.getInstance().getGuids().size());
    }
}
