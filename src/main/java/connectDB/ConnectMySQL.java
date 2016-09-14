package connectDB;

import opennlp.tools.util.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ConnectMySQL {
	private static Connection conn;
	private static ConnectMySQL instance;
	// private static String eol = System.getProperty("line.separator");
	private ArrayList<String> listNews;

	public ArrayList<String> getList() {
		return listNews;
	}

	public void setList(ArrayList<String> list) {
		this.listNews = list;
	}

	public ConnectMySQL() throws ClassNotFoundException, SQLException {
		System.out.println(Name.userName + " || " + Name.password + "||" + Name.hostName + " || " + Name.dbName);
		conn = getMySQLConnection(Name.hostName, Name.dbName, Name.userName, Name.password);
	}

	public ConnectMySQL(String s) throws ClassNotFoundException, SQLException {
//		System.out.println(Name.userName + " || " + Name.password + "||" + Name.hostName + " || " + Name.dbName);
		conn = getMySQLConnection(Name.hostName, Name.dbName, Name.userName, Name.password);
	}

	public ArrayList<String> getNewNews(){
		ArrayList<String> newsids = new ArrayList<String>();
//		d.setDate(d.getHours() - 24);
		String sql = "SELECT newsId, is_deleted FROM  `news`.`news_resource` where publishDate BETWEEN DATE_SUB(NOW(), INTERVAL 2 DAY) AND NOW() " +
				"and is_deleted=0 and sourceNews= 'Soha';";
		ResultSet rs= null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				newsids.add(rs.getString("newsId"));
			}
		}catch (Exception e){

		}finally {

		}
		return newsids;
	}

	public Map<Date, String> getNewNewsInOneDay(){
		Map<Date, String> newsids = new HashMap<>();
//		d.setDate(d.getHours() - 24);
		String sql = "SELECT newsId, publishDate FROM  `news`.`news_resource` where publishDate BETWEEN DATE_SUB(NOW(), INTERVAL 1 DAY) AND NOW() " +
				"and is_deleted=0 ;";
		ResultSet rs= null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				newsids.put(rs.getDate("publishDate"), rs.getString("newsId"));
			}
		}catch (Exception e){

		}finally {

		}
		return newsids;
	}

	public List< String> getNewNewsInNumDay(int T, String source){
		List< String> newsids = new ArrayList<>();
//		d.setDate(d.getHours() - 24);
		String sql = "SELECT newsId, publishDate FROM  `news`.`news_resource` where publishDate BETWEEN DATE_SUB(NOW(), INTERVAL "+T+" DAY) AND NOW() " +
				"and sourceNews= '"+source+"' and is_deleted=0 ;";
		ResultSet rs= null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				newsids.add( rs.getString("newsId"));
			}
		}catch (Exception e){

		}finally {

		}
		return newsids;
	}

	public List< String> getNewNewsInNumDay(int T){
		List< String> newsids = new ArrayList<>();
//		d.setDate(d.getHours() - 24);
		String sql = "SELECT newsId, publishDate FROM  `news`.`news_resource` where publishDate BETWEEN DATE_SUB(NOW(), INTERVAL "+T+" DAY) AND NOW() " +
				"and is_deleted=0 ;";
		ResultSet rs= null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				newsids.add( rs.getString("newsId"));
			}
		}catch (Exception e){

		}finally {

		}
		return newsids;
	}

	public Connection getConn() {
		return conn;
	}

//	public void setConn(Connection conn) {
//		this.conn = conn;
//	}

	public static ConnectMySQL getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new ConnectMySQL();
		}
		return instance;
	}

	public String[] getOther(String newsId){
		String []s= new String[4];
		String title= "";
		String sapo="";
		String url="";
		String content = "";
		String sql = "SELECT  `title`,`content`,`sapo`,`url` FROM  `news`.`news_resource` WHERE newsId = "+ " "+ newsId;
		try {
			ResultSet rs = null;
			try{
				rs = ConnectMySQL.getInstance().getConn().createStatement().executeQuery(sql);
				while (rs.next()) {
					s[0] = rs.getString("title");
					s[1] = rs.getString("sapo");
					s[2]= rs.getString("content");
					s[3]= rs.getString("url");
				}
			}catch (Exception e){

			}finally {

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static String getContentFromNewsIDByMYSQL(String newsId) {
		String content = "";
		String sql = Name.query_getContentFromNewsID + " " +newsId;
		try {
			ResultSet rs = null;
			try{
				rs = ConnectMySQL.getInstance().getConn().createStatement().executeQuery(sql);

				while (rs.next()) {
					String title = rs.getString("title");
					String sapo = rs.getString("sapo");
//					cql += rs.getLong("newsId") + ",'";

					content += title;
					content += " .";
					content += sapo;
					List<String> ar = getStringsFromUrl(rs.getString("content"));
					for (String string : ar) {
						string = string.replace("\n", " ").trim();
						content += string + " ";
						content = content.replace("-", "").replace(".", " ");
//						cql += string.replace("-", "");
					}
//					cql += "','" + sapo + "','" + title + "','" + rs.getString("url") + "');";
				}
			}catch (Exception e){

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	public static List<String> getStringsFromUrl(String html) throws IOException {
		Document document = Jsoup.parse(html);
		Elements elements = StringUtil.isBlank(null) ? document.getElementsByTag("body")
				: document.select(null);

		List<String> strings = new ArrayList<String>();
		elements.traverse(new TextNodeExtractor(strings));
		return strings;
	}

	private static class TextNodeExtractor implements NodeVisitor {
		private final List<String> strings;

		public TextNodeExtractor(List<String> strings) {
			this.strings = strings;
		}

		public void head(Node node, int depth) {
			if (node instanceof TextNode) {
				TextNode textNode = ((TextNode) node);
				String text = textNode.getWholeText();
				if (!StringUtil.isBlank(text)) {
					strings.add(text);
				}
			}
		}

		public void tail(Node arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

//	public static ConnectMySQL getInstance(String s) throws ClassNotFoundException, SQLException {
//		if (instance == null) {
//			instance = new ConnectMySQL(s);
//		}
//		return instance;
//	}

	public static Connection getMySQLConnection(String hostName, String dbName, String userName,
														 String password) throws SQLException, ClassNotFoundException {
		// Khai báo class Driver cho DB MySQL
		// Việc này cần thiết với Java 5
		// Java6 tự động tìm kiếm Driver thích hợp.
		// Nếu bạn dùng Java6, thì ko cần dòng này cũng được.
		Class.forName("com.mysql.jdbc.Driver");

		// Cấu trúc URL Connection dành cho Oracle
		// Ví dụ: jdbc:mysql://localhost:3306/simplehr
		String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?autoReconnect=true";

		Connection conn = DriverManager.getConnection(connectionURL, userName, password);
		System.out.println("connected!");
		return conn;
	}

//	public void getAllNews() {
//		try {
//			listNews = new ArrayList<String>();
//			long d = 0;
//			ResultSet rs = conn.createStatement().executeQuery(Name.query_getDataToTrain);
//			while (rs.next()) {
//				String data = "-" + rs.getLong("newsId") + "\t" + rs.getString("title") + ". " + rs.getString("sapo");
//				List<String> ar = JSoupTest.getStringsFromUrl(rs.getString("content"));
//				String content = "";
//				for (String string : ar) {
//					string = string.replace("\n", " ").trim();
//					content += string + " ";
//					content = content.replace("-", "");
//				}
//				data += content;
//				data = new TPSegmenter("models").segment(data);
//				d++;
//				listNews.add(data);
//				System.out.println(d);
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, InvalidFormatException, IOException {
		// VietTokenizer tokenizer = new VietTokenizer();
		// System.out.println(tokenizer.tokenize("hôm nay trời thật đẹp.")[0]);;
		// ConnectMySQL.getInstance();
//		ConnectMySQL.getInstance();
//		String s = "SELECT  * FROM  `news`.`news_resource`  LIMIT 0, 10 ;";
//		ResultSet rs = new ConnectMySQL().conn.createStatement().executeQuery(s);
//		System.out.print(rs);
		ConnectMySQL connectMySQL = new ConnectMySQL();
		System.out.print(connectMySQL.getNewNews().size());
	}
}