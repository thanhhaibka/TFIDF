package connectDB;

import com.datastax.driver.core.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Cassandra {
	/** Cassandra Cluster. */
	private Cluster cluster;
	/** Cassandra Session. */
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
		System.out.println();
		final Metadata metadata = cluster.getMetadata();
		out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		for (final Host host : metadata.getAllHosts()) {
			out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		this.session = cluster.connect(keyspace);
		out.println("connected!");
	}

	public Cluster getCluster() {
		return cluster;
	}

	public Session getSession() {
		return this.session;
	}

	/** Close cluster. */
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
		// Cluster cluster =
		// Cluster.builder().addContactPoint("10.3.24.154").build();
		//
		// // Creating Session object
		// Session session = cluster.connect("retargetlog");

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
	
	public static void main(String[] args) {
		String sql = "select guid from  othernews.map_guid_domain limit 10;";
		List<Row> ar = Cassandra.getInstance().getSession().execute(sql).all();
		System.out.println(ar.size());
		for (Row row : ar) {
			System.out.println(row.getLong("guid"));
		}
	}
}
