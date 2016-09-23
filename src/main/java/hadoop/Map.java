package hadoop;

import java.io.IOException;

import app.Token;
import connectDB.Cassandra;
import connectDB.Name;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.vcc.bigdata.logprs.parquet.schema.AdsGroup;
import com.vcc.bigdata.logprs.parquet.schema.PageViewV1Log;


public class Map extends Mapper<NullWritable, AdsGroup, Text, Text> {
    private Text value_word = new Text("");
    private Text key_word = new Text("");

    public static final IntWritable one = new IntWritable(1);

    @Override
    protected void map(NullWritable key, AdsGroup value, Context context) throws IOException, InterruptedException {
        try {
            PageViewV1Log log1 = new PageViewV1Log(value);
            String path = log1.getPath();
            long timecookie = log1.getCookieCreate();
            long timecreat = log1.getTimeCreate();
            long guid = log1.getGuid();
            String newsid = splitNewsid(path);

            String domain = log1.getDomain();

            if (domain.equals(Name.domain_afamily) || domain.equals(Name.domain_autopro)
                    || domain.equals(Name.domain_cafebiz) || domain.equals(Name.domain_cafef)
                    || domain.equals(Name.domain_gamek) || domain.equals(Name.domain_genk)
                    || domain.equals(Name.domain_soha) || domain.equals(Name.domain_kenh14)) {
                key_word.set(guid + Name.regex + log1.getDomain());
                value_word.set("0");
                if (newsid.length() >= 8) {
                    context.write(key_word, value_word);
                }
            }
        } catch (Exception e) {
        }
    }

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
}