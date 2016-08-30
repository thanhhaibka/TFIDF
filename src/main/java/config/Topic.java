package config;

/**
 * Created by pc on 04/08/2016.
 */
public class Topic implements Comparable<Topic>{
    private String topicID;
    private String topicName;
    private String weight;

    public Topic(){

    }

    public Topic(String topicID, String topicName, String weight) {
        this.topicID = topicID;
        this.topicName = topicName;
        this.weight = weight;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weigth) {
        this.weight = weigth;
    }

    public String AddWeight(String w1, String w2){
        double sum= Double.parseDouble(w1)+ Double.parseDouble(w2);
        return sum+"";
    }

    @Override
    public boolean equals(Object o){
        if(o==null||o.getClass()!=this.getClass()) return false;
        Topic t= (Topic)o;
        if(t.getTopicName().equals(topicName)) return true;
        return false;
    }

    public String toString(){
        return "("+topicName+" ,"+ weight+")";
    }

    @Override
    public int compareTo(Topic o) {
        double w1= Double.parseDouble(this.weight);
        double w2= Double.parseDouble(o.weight);
        return w1<w2?-1:(w1==w2?0:1);
    }
}
