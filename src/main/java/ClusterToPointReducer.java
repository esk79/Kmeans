import com.google.common.collect.Iterators;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * You can modify this class as you see fit, as long as you correctly update the
 * global centroids.
 */
public class ClusterToPointReducer extends Reducer<IntWritable, Point, IntWritable, Point> {
    protected void reduce(IntWritable key, Iterable<Point> values, Context context)
            throws IOException, InterruptedException {

        float scalar;
        Point updatedCenter = new Point(KMeans.centroids.get(0).getDimension());
        Iterator<Point> it = values.iterator();
        int size = Iterators.size(it);
        while (it.hasNext()){
            updatedCenter = Point.addPoints(updatedCenter, it.next());
        }
        scalar = (1/(float)size);

        updatedCenter = Point.multiplyScalar(updatedCenter,scalar);

        context.write(key,updatedCenter);

        KMeans.centroids.set(key.get(),updatedCenter);
    }
}
