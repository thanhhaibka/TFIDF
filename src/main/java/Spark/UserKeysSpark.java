package Spark;

import app.Token;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.vcc.bigdata.logprs.parquet.schema.PageViewV1Log;
import config.User;
import connectDB.Cassandra;
import connectDB.Name;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// $example on:init_session$
//import WVT.UserVectorV2;

public class UserKeysSpark {

    public static String splitNewsid(String s) {
        s += "ha";
        String temp = s.split(".htm|.chn")[0];
        int t = temp.length() - 1;
        String out = "";
        while (t > 0) {
            try {
                int t1 = Integer.parseInt(temp.charAt(t) + "");
                out = t1 + "" + out;
            } catch (Exception e) {
                break;
            }
            t--;
        }

        return out;
    }

    @SuppressWarnings({"deprecation", "serial"})
    public static void main(String[] args) throws Exception {
        Token.getInstance();
        SparkConf sparkConf = new SparkConf().setAppName("userkeyslong").setMaster("local");
        // JavaSparkContext context = new JavaSparkContext(sparkConf);
        // SQLContext sqlContext = new org.apache.spark.sql.SQLContext(context);
        // // Create the DataFrame
        // sqlContext
        // .parquetFile(
        // "hdfs://" + host +
        // ":9000/user/quangnd/AudienceSet/rawdata/date=2016_07_03/domain=kenh14.vn")
        // .registerTempTable("log");
        // ;
        // JavaRDD<Long> guidRdd = sqlContext.sql("select guid from
        // log").toJavaRDD().map(r -> r.getLong(0));
        // System.out.println(guidRdd.take(20));
        JavaStreamingContext sc = new JavaStreamingContext(sparkConf, Durations.milliseconds(60000));
        Set<String> topics = new HashSet<String>();
        topics.add("log_pageview");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("metadata.broker.list", "hslave-10:9092,hslave-12:9092");
        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(sc, String.class,
                String.class, StringDecoder.class, StringDecoder.class, map, topics);
        directKafkaStream.foreachRDD(rdd -> {
            rdd.foreach(line -> {
                PageViewV1Log log = new PageViewV1Log();
                log.clear();
                boolean tr = false;
                try {
                    tr = log.offer(line._2());
                } catch (Exception a) {
                }
                if (tr) {
                    try {
                        String domain = log.getDomain();
                        if (domain.equals(Name.domain_afamily) || domain.equals(Name.domain_autopro)
                                || domain.equals(Name.domain_cafebiz) || domain.equals(Name.domain_cafef)
                                || domain.equals(Name.domain_gamek) || domain.equals(Name.domain_genk)
                                || domain.equals(Name.domain_soha) || domain.equals(Name.domain_kenh14)) {
                            String s = splitNewsid(log.getPath());
                            if (s != null && s.length() > 5) {
                                long newsid = Long.parseLong(s);
                                long guid = log.getGuid();
                                try {
                                    updateKeyWord(guid + "", domain);
                                    System.out.println(guid + "_" + domain);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        }

                    } catch (Exception e) {
                    }
                }

            });
            return null;
        });
        sc.start();
        sc.awaitTermination();
    }

    public static void updateKeyWord(String guid, String domain) {
        if (!Cassandra.getInstance().getMapTFIDF(guid, domain).isEmpty()) {
//            Map<String, Double> mapTFIDF = Cassandra.getInstance().getMapTFIDF(guid, domain);
//            Map<String, Double> shortTerm = Cassandra.getInstance().getShortTerm(guid, domain);
//            Set<String> words= new HashSet<>();
//            Map<String, Double> longTermWords = new HashMap<>();
//            for(String s: mapTFIDF.keySet()){
//                words.add(s);
//            }
//            for(String s: shortTerm.keySet()){
//                words.add(s);
//            }
//            for(String s: words){
//                double sum=0.0;
//                if(mapTFIDF.containsKey(s)) sum+=mapTFIDF.get(s)* 0.8;
//                if(shortTerm.containsKey(s)) sum+=shortTerm.get(s)* 1;
//                longTermWords.put(s, sum/2);
//            }
//            com.datastax.driver.core.Statement exampleQuery = QueryBuilder.update("othernews", "guid_long_term")
//                    .with(QueryBuilder.set("keywords", Token.getInstance().getTop1002(longTermWords))).where(QueryBuilder.eq("guid_domain", guid+"_"+domain));
//            Cassandra.getInstance().getSession().execute(exampleQuery);
        } else {
//            System.out.println(2);
            int M = 30 / 6;
            double[] weights = {1, 0.9, 0.8, 0.7, 0.6, 0.5};
            Map<String, Double>[] temp = new Map[6];
//        User []user= new User[N];
            for (int i = 0; i < 6; i++) {
                User user = Token.getInstance().setUser(guid, domain, M * i, M * (i + 1));
                temp[i] = Token.getInstance().getTopN(user.getMapTFIDF(), 100);
            }
            Set<String> words = new HashSet<>();
            for (int i = 0; i < 6; i++) {
                words.addAll(temp[i].keySet());
            }
            Map<String, Double> longTermWords = new HashMap<>();
            for (String s : words) {
                double sum = 0;
                for (int i = 0; i < 6; i++) {
                    if (temp[i].containsKey(s)) sum += temp[i].get(s) * weights[i];
                }
                longTermWords.put(s, sum / 6);
            }
            com.datastax.driver.core.Statement exampleQuery = QueryBuilder.insertInto("othernews", "guid_long_term").value("guid_domain", guid + "_" + domain)
                    .value("keywords", Token.getInstance().getTopN(longTermWords, 100));
            Cassandra.getInstance().getSession().execute(exampleQuery);
        }

    }
}