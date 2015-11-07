import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * You can modify this class as you see fit, as long as you correctly update the
 * global centroids.
 */
public class ClusterToPointReducer extends Reducer<IntWritable, Point, IntWritable, Point> {
    protected void reduce(IntWritable key, Iterable<Point> values, Context context)
            throws IOException, InterruptedException {
    }
}
