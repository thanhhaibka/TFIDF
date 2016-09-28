package hadoop;

import app.NewToken;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import config.User;
import connectDB.Cassandra;
import connectDB.Name;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import stemmer.StopWords;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;


public class Reduce extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> valuesIt = values.iterator();
        String tm[] = key.toString().split(Name.regex);
        String guid = tm[0];
        if (guid == "0" || guid == "-1")
            return;
        String domain = tm[1];
        Map<String, Double> lt=updateKeyWord(guid, domain);

        context.write(new Text(guid + "_" + domain), new Text());
    }

    public Map<String, Double> updateKeyWord(String guid, String domain) {
        Map<String, Double> longTermWords = new HashMap<>();
        Date date= new Date();
        if (!Cassandra.getInstance().getMapTFIDF(guid, domain).isEmpty()) {
            Date dd= Cassandra.getInstance().getTimeInsert(guid, domain);
            if((date.getTime()- dd.getTime())/(60*60*1000*24)>=5) {
                java.util.Map<String, Double> mapTFIDF = Cassandra.getInstance().getMapTFIDF(guid, domain);
                java.util.Map<String, Double> shortTerm = Cassandra.getInstance().getShortTerm(guid, domain);
                Set<String> words = new HashSet<>();
                for (String s : mapTFIDF.keySet()) {
                    words.add(s);
                }
                for (String s : shortTerm.keySet()) {
                    if(!StopWords.getInstance().isStopword(s)) {
                        words.add(s);
                    }
                }
                for (String s : words) {
                    double sum = 0.0;
                    if (mapTFIDF.containsKey(s)) sum += mapTFIDF.get(s) * 0.95;
                    if (shortTerm.containsKey(s)) sum += shortTerm.get(s) * 1;
                    longTermWords.put(s, sum / 2);
                }
                com.datastax.driver.core.Statement exampleQuery = QueryBuilder.update("othernews", "guid_long_term")
                        .with(QueryBuilder.set("keywords", NewToken.getInstance().getTop1002(longTermWords))).and(QueryBuilder.set("time", date))
                        .where(QueryBuilder.eq("guid_domain", guid + "_" + domain));
                Cassandra.getInstance().getSession().execute(exampleQuery);
            }else {

            }
        } else {
//            System.out.println(2);
        int M = 5;
        double[] weights = {1, 0.9, 0.8, 0.7, 0.6, 0.5};
        Map<String, Double>[] temp = new Map[6];
//        User []user= new User[N];
        for (int i = 0; i < 6; i++) {
            User user = NewToken.getInstance().setUser(guid, domain, M * i, M * (i + 1));
            if (user.getMapTFIDF().isEmpty()) {
                temp[i] = new HashMap<>();
            } else {
                temp[i] = NewToken.getInstance().getTopN(user.getMapTFIDF(), 100);
            }
        }
        Set<String> words = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            words.addAll(temp[i].keySet());
        }

        for (String s : words) {
            double sum = 0, sum2 = 0;
            for (int i = 0; i < 6; i++) {
                if (temp[i].containsKey(s)) {
                    sum += temp[i].get(s) * weights[i];
                    sum2 += 1 * weights[i];
                }
            }
            longTermWords.put(s, sum / 6 * sum2);
            longTermWords= NewToken.getTopN(longTermWords, 100);
        }
            com.datastax.driver.core.Statement exampleQuery = QueryBuilder.insertInto("othernews", "long_term").value("guid_domain", guid + "_" + domain)
                .value("keywords", longTermWords).value("time", date);
            Cassandra.getInstance().getSession().execute(exampleQuery);
        }
        return longTermWords;
    }
}
