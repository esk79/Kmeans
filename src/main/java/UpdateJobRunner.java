import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class UpdateJobRunner {
    /**
     * Create a map-reduce job to update the current centroids.
     *
     * @param jobId           Some arbitrary number so that Hadoop can create a directory "<outputDirectory>/<jobname>_<jobId>"
     *                        for storage of intermediate files.  In other words, just pass in a unique value for this
     *                        parameter.
     * @param inputDirectory  The input directory specified by the user upon executing KMeans, in which the points
     *                        to find the KMeans point files are located.
     * @param outputDirectory The output directory for which to write job results, specified by user.
     * @precondition The global centroids variable has been set.
     */
    public static Job createUpdateJob(int jobId, String inputDirectory, String outputDirectory)
            throws IOException {

        Job updateJob = new Job(new Configuration(), "UpdateJobRunner_updateJob");

        updateJob.setJarByClass(KMeans.class);
        updateJob.setMapperClass(PointToClusterMapper.class);
        updateJob.setMapOutputKeyClass(IntWritable.class);
        updateJob.setMapOutputValueClass(Point.class);
        updateJob.setReducerClass(ClusterToPointReducer.class);
        updateJob.setOutputKeyClass(IntWritable.class);
        updateJob.setOutputValueClass(Point.class);
        FileInputFormat.addInputPath(updateJob, new Path(inputDirectory));
        FileOutputFormat.setOutputPath(updateJob, new Path(outputDirectory)); //TODO output path could change
        updateJob.setInputFormatClass(KeyValueTextInputFormat.class);
        return updateJob;
    }

    /**
     * Run the jobs until the centroids stop changing.
     * Let C_old and C_new be the set of old and new centroids respectively.
     * We consider C_new to be unchanged from C_old if for every centroid, c, in
     * C_new, the L2-distance to the centroid c' in c_old is less than [epsilon].
     * <p>
     * Note that you may retrieve publically accessible variables from other classes
     * by prepending the name of the class to the variable (e.g. KMeans.one).
     *
     * @param maxIterations   The maximum number of updates we should execute before
     *                        we stop the program.  You may assume maxIterations is positive.
     * @param inputDirectory  The path to the directory from which to read the files of Points
     * @param outputDirectory The path to the directory to which to put Hadoop output files
     * @return The number of iterations that were executed.
     */
    public static int runUpdateJobs(int maxIterations, String inputDirectory,
                                    String outputDirectory) {
        boolean changed = true;
        ArrayList<Point> c_old = new ArrayList<>(KMeans.centroids);
        Job[] jobs = new Job[maxIterations];

        int i = 0;
        while (i <= maxIterations && changed) {
            try {
                jobs[i] = createUpdateJob(i, inputDirectory, outputDirectory);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
            for (int k = 0; k < c_old.size(); k++) {
                changed &= KMeans.centroids.get(k).compareTo(c_old.get(k)) != 0;
            }

            i++;
        }
        return i;

    }
}
