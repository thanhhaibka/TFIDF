package hadoop;

import app.Token;
import com.vcc.bigdata.logprs.parquet.schema.AdsGroup;
import com.vcc.bigdata.logprs.parquet.schema.PageViewV1Log;
import connectDB.Cassandra;
import connectDB.Name;
import org.apache.cassandra.hadoop.ConfigHelper;
import org.apache.cassandra.hadoop.cql3.CqlConfigHelper;
import org.apache.cassandra.hadoop.cql3.CqlOutputFormat;
import org.apache.cassandra.hadoop.cql3.CqlPagingInputFormat;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This counts the occurrences of words in ColumnFamily
 * cql3_worldcount ( user_id text,
 * category_id text,
 * sub_category_id text,
 * title  text,
 * body  text,
 * PRIMARY KEY (user_id, category_id, sub_category_id))
 * <p>
 * For each word, we output the total number of occurrences across all body texts.
 * <p>
 * When outputting to Cassandra, we write the word counts to column family
 * output_words ( row_id1 text,
 * row_id2 text,
 * word text,
 * count_num text,
 * PRIMARY KEY ((row_id1, row_id2), word))
 * as a {word, count} to columns: word, count_num with a row key of "word sum"
 */
public class WordCount extends Configured implements Tool {
    private static final Logger logger = LoggerFactory.getLogger(WordCount.class);

    static final String KEYSPACE = "othernews";
    static final String COLUMN_FAMILY = "inputs";

    static final String OUTPUT_REDUCER_VAR = "output_reducer";
    static final String OUTPUT_COLUMN_FAMILY = "guid_long_term";

    private static final String PRIMARY_KEY = "guid_domain";

    public static void main(String[] args) throws Exception {
        // Let ToolRunner handle generic command-line options
        ToolRunner.run(new Configuration(), new WordCount(), args);
        System.exit(0);
    }

    public static class TokenizerMapper extends Mapper<NullWritable, AdsGroup, Text, Text> {
        private Text value_word = new Text("");
        private Text key_word = new Text("");

        public static final IntWritable one = new IntWritable(1);

        @Override
        protected void map(NullWritable key, AdsGroup value, Context context) throws IOException, InterruptedException {
            Cassandra.getInstance();
            Token.getInstance();
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

//    public static class ReducerToFilesystem extends Reducer<Text, IntWritable, Text, IntWritable> {
//        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
//        {
//            int sum = 0;
//            for (IntWritable val : values)
//                sum += val.get();
//            context.write(key, new IntWritable(sum));
//        }
//    }

    public static class ReducerToCassandra extends Reducer<Text, Text, Map<String, ByteBuffer>, List<ByteBuffer>> {
        private Map<String, ByteBuffer> keys;
        private ByteBuffer key;

        protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context) throws IOException, InterruptedException {
            keys = new LinkedHashMap<String, ByteBuffer>();
            String[] partitionKeys = context.getConfiguration().get(PRIMARY_KEY).split(",");
            keys.put("row_id1", ByteBufferUtil.bytes(partitionKeys[0]));
            keys.put("row_id2", ByteBufferUtil.bytes(partitionKeys[1]));
        }

        public void reduce(Text word, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values)
                sum += val.get();
            context.write(keys, getBindVariables(word, sum));
        }

        private List<ByteBuffer> getBindVariables(Text word, int sum) {
            List<ByteBuffer> variables = new ArrayList<ByteBuffer>();
            keys.put("word", ByteBufferUtil.bytes(word.toString()));
            variables.add(ByteBufferUtil.bytes(String.valueOf(sum)));
            return variables;
        }
    }

    public int run(String[] args) throws Exception {

//        logger.info("output reducer type: " + outputReducerType);

        Job job = new Job(getConf(), "wordcount");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(ReducerToCassandra.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setOutputFormatClass(CqlOutputFormat.class);

        ConfigHelper.setOutputColumnFamily(job.getConfiguration(), KEYSPACE, OUTPUT_COLUMN_FAMILY);
        job.getConfiguration().set(PRIMARY_KEY, "word,sum");
        String query = "UPDATE " + KEYSPACE + "." + OUTPUT_COLUMN_FAMILY +
                " SET count_num = ? ";
        CqlConfigHelper.setOutputCql(job.getConfiguration(), query);
        ConfigHelper.setOutputInitialAddress(job.getConfiguration(), "localhost");
        ConfigHelper.setOutputPartitioner(job.getConfiguration(), "Murmur3Partitioner");
        job.setInputFormatClass(CqlPagingInputFormat.class);

        ConfigHelper.setInputRpcPort(job.getConfiguration(), "9160");
        ConfigHelper.setInputInitialAddress(job.getConfiguration(), "localhost");
        ConfigHelper.setInputColumnFamily(job.getConfiguration(), KEYSPACE, COLUMN_FAMILY);
        ConfigHelper.setInputPartitioner(job.getConfiguration(), "Murmur3Partitioner");

        CqlConfigHelper.setInputCQLPageRowSize(job.getConfiguration(), "3");
        //this is the user defined filter clauses, you can comment it out if you want count all titles
        CqlConfigHelper.setInputWhereClauses(job.getConfiguration(), "title='A'");
        job.waitForCompletion(true);
        return 0;
    }
}
