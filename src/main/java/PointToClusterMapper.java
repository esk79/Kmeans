import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * You can modify this class as you see fit.  You may assume that the global
 * centroids have been correctly initialized.
 * Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 */
public class PointToClusterMapper extends Mapper<Text, Text, IntWritable, Point> {
    public void map(Text key, Text value, Context context)
            throws IOException, InterruptedException
    {
        Point current = new Point(key.toString());
        float smallestDistance = Float.MAX_VALUE;
        int indexOfClosest = 0;
        Point closest = null;

        for (Point p : KMeans.centroids){
            float currentDistance = Point.distance(p, current);

            if (currentDistance < smallestDistance){
                closest = p;
                smallestDistance = currentDistance;
                indexOfClosest = KMeans.centroids.indexOf(p);
            }
        }
        context.write(new IntWritable(indexOfClosest), closest);
    }
}
