package hadoop;

/**
 * Created by pc on 07/09/2016.
 */

import com.vcc.bigdata.logprs.parquet.AdsParquetInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class UpdateCass extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new UpdateCass(), args);
        System.exit(1);
    }

    public int run(String[] arg) throws Exception {
        Configuration conf = new Configuration();
        conf.addResource(new Path("config/core-site.xml"));
        conf.addResource(new Path("config/hbase-site.xml"));
        conf.addResource(new Path("config/hdfs-site.xml"));
        conf.addResource(new Path("config/mapred-site.xml"));
        conf.addResource(new Path("config/yarn-site.xml"));

        Job job = new Job(conf, "User Long Term KeyWords");
        job.setJarByClass(UpdateCass.class);

        job.setInputFormatClass(AdsParquetInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setNumReduceTasks(24);

        FileSystem fileSystem = FileSystem.get(conf);

        // Output
        if (fileSystem.exists(new Path("/user/haint/"+arg[3]))) {
            // Delete file
            fileSystem.delete(new Path("/user/haint/"+arg[3]), true);
        }
//        Date date= new Date();
        String input = "2016_"+arg[2]+"_";
        for (int i = Integer.parseInt(arg[0]); i <= Integer.parseInt(arg[1]); i++) {
            String temp = input;
            if (i < 10)
                    AdsParquetInputFormat.addInputPath(job,
                            new Path("hdfs://" + "192.168.23.130" + ":9000/data/Parquet/PageViewV1/" + temp + "0" + i+"/*.snap"));
            else
                AdsParquetInputFormat.addInputPath(job,
                        new Path("hdfs://" + "192.168.23.130" + ":9000/data/Parquet/PageViewV1/" + temp  + i+"/*.snap"));
        }
        FileOutputFormat.setOutputPath(job, new Path("/user/haint/"+arg[3]));
        return job.waitForCompletion(true) ? 0 : 1;

    }
}