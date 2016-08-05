package clustering;

import config.User;
import config.UserProfile;
import edu.udo.cs.wvtool.main.WVTWordVector;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pc on 27/07/2016.
 */
public class KMean {
    final int numCluster;
    private User user;
    private int numIteration;
    private double logLikelihood;
    private double MDL;
    private Random rd;
    private UserProfile userProfile;

    public KMean(User user,int numCluster, int numIteration, double MDL){
        this.user= user;
        this.numCluster=numCluster;
        this.numIteration=numIteration;
        this.MDL= MDL;
        userProfile = new UserProfile(user, numCluster);
        clustering();
    }

    public void clustering(){
        int i=0;
//        ArrayList<WVTWordVector> oldCentroids= new ArrayList<WVTWordVector>();
        while (i != numIteration) {
            Double sum = 0.0;
            ArrayList<WVTWordVector> oldCentroids = new ArrayList<WVTWordVector>();
            for(int j=0; j<numCluster; j++){
                oldCentroids.add(userProfile.clusters[j].centroid);
            }
            clear();
            updateCluster();
            for(int j=0; j<numCluster; j++){
                sum+=getDistance(userProfile.clusters[j].centroid, oldCentroids.get(i));
            }
            if(sum<MDL) break;
            i++;
        }
    }

    private void clear(){
        for(int j=0; j<numCluster; j++){
            userProfile.clusters[j].clear();
        }
    }

    public void updateCentroids(){
        WVTWordVector var1= user.getWordVectors().get(0);
        int size= var1.getValues().length;
        for(int i=0; i<numCluster; i++){
            WVTWordVector var3= userProfile.clusters[i].centroid;
            for(int j=0; j<userProfile.clusters[i].getSize(); j++){
                WVTWordVector var2= userProfile.clusters[i].cluster.get(j);
                var3.setValues(sumVector(var2, var1));
            }
            if(size!=0){
//                var3.setValues(avgVector(var3, size));
                userProfile.clusters[i].centroid.setValues(avgVector(var3, size));
            }
        }

    }

    public void updateCluster(){
        for(int i=0; i<user.getWordVectors().size(); i++){
            Double min = Double.MAX_VALUE;
            int numCluster = 0;
            for(int j=0; j<this.numCluster; j++){
                double dis= getDistance(user.getWordVectors().get(i), userProfile.clusters[j].centroid);
                if(dis < min){
                    min = dis;
                    numCluster = j;
                }
            }
            userProfile.clusters[numCluster].cluster.add(user.getWordVectors().get(i));
        }
        updateCentroids();
    }

    public double getDistance(WVTWordVector var1, WVTWordVector var2) {
        double[] var3= var1.getValues();
        double[] var4= var2.getValues();
        double sum=0;
        for(int var5=0; var5< var3.length; var5++){
            sum+=(var3[var5]-var4[var5])*(var3[var5]-var4[var5]);
        }
        return Math.sqrt(sum);
    }

    public double[] sumVector(WVTWordVector var1, WVTWordVector var2){
        double[] var3= var1.getValues();
        double[] var4= var2.getValues();
        double[] var6= new double[var3.length];
//        WVTWordVector var7= new WVTWordVector();
        for(int var5=0; var5< var3.length; var5++){
            var6[var5]= var3[var5]+var4[var5];
        }
        return var6;
    }

    public double[] avgVector(WVTWordVector var1, int size){
        double[] var2= var1.getValues();
        double[] var5= new double[var2.length];
//        WVTWordVector var4= new WVTWordVector();
        for(int var3=0; var3< var2.length; var3++){
            var5[var3]= var2[var3]/size;
        }
//        var4.setValues(var5);
        return var5;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumIteration() {
        return numIteration;
    }

    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    public double getLogLikelihood() {
        return logLikelihood;
    }

    public void setLogLikelihood(double logLikelihood) {
        this.logLikelihood = logLikelihood;
    }

    public double getMDL() {
        return MDL;
    }

    public void setMDL(double MDL) {
        this.MDL = MDL;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
