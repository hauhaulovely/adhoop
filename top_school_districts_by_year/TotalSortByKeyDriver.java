import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.lib.IdentityMapper;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TotalSortByKeyDriver extends Configured implements Tool
{

  @Override
  public int run(String[] args) throws Exception
  {

    if (args.length != 2)
    {
      System.out.printf("Usage: %s [generic options] <input dir> <output dir>\n", getClass().getSimpleName());
      ToolRunner.printGenericCommandUsage(System.out);
      return -1;
    }

    JobConf conf = new JobConf(getConf(), TotalSortByKeyDriver.class);
    conf.setJobName(this.getClass().getName());

    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));

    conf.setInputFormat(KeyValueTextInputFormat.class);

    conf.setMapperClass(IdentityMapper.class);
    conf.setOutputKeyComparatorClass(TotalSortByKeyOutputKeyComparator.class);
    conf.setPartitionerClass(TotalSortByKeyPartitioner.class);
    conf.setReducerClass(TotalSortByKeyReducer.class);

    conf.setNumReduceTasks(4);

    conf.setMapOutputKeyClass(Text.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    JobClient.runJob(conf);
    return 0;
  }

  public static void main(String[] args) throws Exception
  {
    int exitCode = ToolRunner.run(new TotalSortByKeyDriver(), args);
    System.exit(exitCode);
  }
}
