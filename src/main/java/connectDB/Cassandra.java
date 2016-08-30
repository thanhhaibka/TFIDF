package connectDB;

import com.datastax.driver.core.*;
import config.Document;
import config.Topic;

import java.util.*;

import static java.lang.System.out;

public class Cassandra {
    /**
     * Cassandra Cluster.
     */
    private Cluster cluster;
    /**
     * Cassandra Session.
     */
    private Session session;

    private BatchStatement batchStatemet;

    public BatchStatement getBatchStatement() {
        return batchStatemet;
    }

    public void setBatchStatement(BatchStatement batchStatement) {
        this.batchStatemet = batchStatement;
    }

    private static Cassandra instance;

    public Cassandra() {
        connect(Config.node, Config.portConn, Config.keyspaceDAO);
        batchStatemet = new BatchStatement();
    }

    public static Cassandra getInstance() {
        if (instance == null) {
            instance = new Cassandra();
        }
        return instance;
    }

    public void connect(final String node, final int port, String keyspace) {
        cluster = Cluster.builder().addContactPoint(node).withCredentials(Config.usernameCass, Config.passwordCass).build();
//        System.out.println();
        final Metadata metadata = cluster.getMetadata();
//        out.printf("Connected to memOfCluster: %s\n", metadata.getClusterName());
//        for (final Host host : metadata.getAllHosts()) {
//            out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
//        }
        this.session = cluster.connect(keyspace);
//        out.println("connected!");
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        cluster.closeAsync();
        session.closeAsync();
        System.out.println("closed!");
    }

    public void truncate(String table) {
        session.execute("TRUNCATE " + table + ";");
        System.out.println("Truncated!");
    }

    public void creatTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user_log(STT bigint,ActionId int,ActionIdParse int, BrowserCode int,BrowserCodeParse int, BrowserName varchar, BrowserNameParse varchar,Domain varchar,DomainParse varchar, Guid bigint,PRIMARY KEY(STT));";
        session.execute(sql);
        System.out.println("Creat table ctest sucessfully;");
    }

    public void creatTable1() {
        String sql = "CREATE TABLE IF NOT EXISTS user_log(STT bigint,ActionId int,ActionIdParse int, BrowserCode int,BrowserCodeParse int, BrowserName varchar, BrowserNameParse varchar,Domain varchar,DomainParse varchar, Guid bigint,PRIMARY KEY(stt,guid)) WITH CLUSTERING ORDER BY (guid DESC) AND caching = 'ALL';";
        session.execute(sql);
        System.out.println("Creat table user_log sucessfully;");
    }

    public void drop(String table) {
        String sql = "DROP TABLE IF EXISTS " + table + ";";
        session.execute(sql);
        System.out.println("Dropped table!");
    }

    public long numberRecode(String table) {
        long tStart = System.currentTimeMillis();
        String sql = "Select count(*) from " + table + " limit 100000;";
        // Cluster memOfCluster =
        // Cluster.builder().addContactPoint("10.3.24.154").build();
        //
        // // Creating Session object
        // Session session = memOfCluster.connect("retargetlog");

        // Getting the ResultSet
        ResultSet result = session.execute(sql);
        ArrayList<Row> ar = (ArrayList<Row>) result.all();
        cluster.close();
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta;
        System.out.println(elapsedSeconds);
        return ar.get(0).getLong(0);
    }

    public String getTextArticle(String newsID) {
        String sql = "select content,sapo,title from  othernews.newsurl where newsid =" + newsID + ";";
        String s = "";
        try {
            Row row = Cassandra.getInstance().getSession().execute(sql).one();
            s = row.getString(0) + " " + row.getString(1) + " " + row.getString(2)+" ";
        } catch (Exception e) {

        }
        return s;
    }

    public String getTitle(String newsID) {
        String sql = "select title from  othernews.newsurl where newsid =" + newsID + ";";
        String s = "";
        try {
            Row row = Cassandra.getInstance().getSession().execute(sql).one();
            s = row.getString(0);
        } catch (Exception e) {

        }
        return s;
    }

    public List<Topic> getTopics(String newsID){
        String sql = "select * from  othernews.newscategoryscore where newsid =" + newsID + ";";
        ArrayList<Topic> topics = new ArrayList<Topic>();
        try {
            Row row = Cassandra.getInstance().getSession().execute(sql).one();
            for (int i = 0; i < 30; i++) {
                topics.add(new Topic(i + "", Name.topics[i], row.getFloat(Name.topics[i]) + ""));
            }
        } catch (Exception e) {

        }
        return topics;
    }

    public ArrayList<Date> getAccessTime(String guid) {
        ArrayList<Date> accessTime = new ArrayList<Date>();
        String sql = "select time_insert_domain, guid from othernews.map_guid_domain where guid =" + guid + " ALLOW FILTERING;";
        List<Row> rows = Cassandra.getInstance().getSession().execute(sql).all();
        for (Row row : rows) {
            String s = row.getString(0) + "_" + row.getLong(1);
            String sql1 = "select * FROM  othernews.access_history  WHERE time_insert_domain_guid  ='" + s + "' ALLow filtering;";
            List<Row> rows2 = Cassandra.getInstance().getSession().execute(sql1).all();
            for (Row r : rows2) {
                accessTime.add((Date) r.getObject(2));
            }
        }
        return accessTime;
    }

    public List<Document> getDocsLimitT(int threshold){
        List<Document> documents = new ArrayList<Document>();
        String cql= "select newsId from othernews.newsurl limit "+ threshold;
        List<Row> rows = Cassandra.getInstance().getSession().execute(cql).all();
        rows.stream().forEach(i->
                documents.add(new Document(i.getLong(0)+""))
                );
        return documents;
    }

    public List<Document> getDocs(String guid, String domain, int N){
        List<Document> documents = new ArrayList<Document>();
        Date date= new Date();
        Date beginDate= new Date(date.getTime()- N* 1000* 60* 60*24l);
        Set<Long> newsId= new HashSet<Long>();
        String cql = "select time_insert_domain, guid from othernews.map_guid_domain where guid =" + guid + " ALLOW FILTERING;";
        List<Row> rows = Cassandra.getInstance().getSession().execute(cql).all();
        rows.stream().forEach(i->{
               String[] s1= i.getString(0).split("_");
                String[] s2= s1[0].split("-");
                if(s1[1].equals(domain)){
                    Date d= new Date(Integer.parseInt(s2[0])-1900, Integer.parseInt(s2[1]), Integer.parseInt(s2[2]));
                    if(d.after(beginDate)){
                        try{

                            String s = i.getString(0) + "_" + guid;
                            String sql1 = "select newshis, access_time FROM  othernews.access_history  WHERE time_insert_domain_guid  ='" + s + "';";
                            List<Row> rows2 = Cassandra.getInstance().getSession().execute(sql1).all();
                            for (Row r : rows2) {
                                Map<Long, Integer> map = r.getMap(0, Long.class, Integer.class);
                                for (Long l : map.keySet()) {
                                    if(!newsId.contains(l)) newsId.add(l);
                                }
                            }
                        }catch (Exception e){

                        }
                    }else{
                        System.out.println(i.getString(0));
                    }
                }
        });
        for(Long s: newsId){
            documents.add(new Document((s+"").trim()));
        }
        return documents;
    }

    public List<Document> getDocs(String guid) {
        List<Document> documents = new ArrayList<Document>();
        String sql = "select time_insert_domain, guid from othernews.map_guid_domain where guid =" + guid + " ALLOW FILTERING;";
        List<Row> rows = Cassandra.getInstance().getSession().execute(sql).all();
//        rows.stream().forEach(i->(
//
//                ));
        Set<Long> newsId= new HashSet<Long>();
        for (Row row : rows) {
            try{
                String s = row.getString(0) + "_" + row.getLong(1);
                String sql1 = "select newshis, access_time FROM  othernews.access_history  WHERE time_insert_domain_guid  ='" + s + "';";
                List<Row> rows2 = Cassandra.getInstance().getSession().execute(sql1).all();
                for (Row r : rows2) {
                    Map<Long, Integer> map = r.getMap(0, Long.class, Integer.class);
                    for (Long s1 : map.keySet()) {
                        if(!newsId.contains(s1)) newsId.add(s1);
                    }
                }
            }catch (Exception e){

            }
        }
//        System.err.println(newsId);
        for(Long s: newsId){
            documents.add(new Document((s+"").trim()));
//            System.err.println(s);
        }
        return documents;
    }

    public static void main(String[] args) {
//        System.out.print(new Cassandra().getTextArticle("20160605232141191"));
        Date dateBegin = new Date(116, 6, 26, 13, 35, 32);
        Date dateEnd = new Date(dateBegin.getTime()- 1*1000*60*60*24l);
//        System.out.print(d.getMonth());
    }

}
