package hadoop;

import app.Token;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import config.Document;
import config.User;
import connectDB.Cassandra;
import connectDB.Name;
import edu.udo.cs.wvtool.util.WVToolException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;


public class Reduce extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> valuesIt = values.iterator();
        Token token= new Token();
        String tm[] = key.toString().split(Name.regex);
        long guid = Long.parseLong(tm[0]);
        if (guid == 0 || guid == -1)
            return;
        String domain = tm[1];
//        Map<Long, String> hm1 = new HashMap<Long, String>();
//        Update upd = QueryBuilder.update(Name.table);
        List<String> newsIds= new ArrayList<>();
        long acess_time = 0;
        while (valuesIt.hasNext()) {
            newsIds.add(valuesIt.next().toString());
        }
        List<Document> docs= new ArrayList<>();
        if(!newsIds.isEmpty()){
            docs= Cassandra.getInstance().getContent(newsIds);
            User user= new User();
            Map<String, Double> mapIDF= new HashMap<>();
            Map<String, String> mapKeys= new HashMap<>();
            try{
                user= token.getUserVector1(docs);
                mapIDF = Token.getTopN(user.getMapTFIDF(), 100);
                mapKeys = new HashMap<>();
                Map<String, String> mapKeysTemp = user.getMapWords();
                for (String s : mapIDF.keySet()) {
                    if (mapKeysTemp.containsKey(s)) {
                        mapKeys.put(s, mapKeysTemp.get(s));
                    }
                }
            }catch (WVToolException e){

            }
            com.datastax.driver.core.Statement exampleQuery = QueryBuilder.insertInto("othernews", "guid_long_term").value("guid_domain", guid+"_"+domain)
                    .value("keywords", mapIDF).value("mapword", mapKeys).ifNotExists();
            Cassandra.getInstance().getSession().execute(exampleQuery);
                context.write(new Text(guid + "_" + domain), new Text(mapIDF.toString()));
        }

    }
}
