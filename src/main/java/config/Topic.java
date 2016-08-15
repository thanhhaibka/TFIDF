package config;

/**
 * Created by pc on 04/08/2016.
 */
public class Topic {
    private String topicID;
    private String topicName;
    private String weight;

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

    public String toString(){
        return "("+topicName+" ,"+ weight+")";
    }
}
