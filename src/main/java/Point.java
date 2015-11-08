import java.io.*; // DataInput/DataOuput
import java.util.*;

import org.apache.hadoop.io.*; // Writable

/**
 * A Point is some ordered list of floats.
 * <p>
 * A Point implements WritableComparable so that Hadoop can serialize
 * and send Point objects across machines.
 * <p>
 * NOTE: This implementation is NOT complete.  As mentioned above, you need
 * to implement WritableComparable at minimum.  Modify this class as you see fit.
 */
public class Point implements WritableComparable<Point> {
    private ArrayList<Float> pointList = new ArrayList<>();
    private int dimension;

    /**
     * Construct a Point with the given dimensions [dim]. The coordinates should all be 0.
     * For example:
     * Constructing a Point(2) should create a point (x_0 = 0, x_1 = 0)
     */
    public Point(int dim) {
        dimension = dim;
        int i = 0;
        while (i <= dimension) {
            pointList.add(new Float(0));
            i++;
        }
    }

    /**
     * Construct a point from a properly formatted string (i.e. line from a test file)
     *
     * @param str A string with coordinates that are space-delimited.
     *            For example:
     *            Given the formatted string str="1 3 4 5"
     *            Produce a Point {x_0 = 1, x_1 = 3, x_2 = 4, x_3 = 5}
     */
    public Point(String str) {
        List<String> temp = Arrays.asList(str.split("\\s"));
        dimension = temp.size();
        for (String s : temp)
            pointList.add(Float.valueOf(s));
    }

    /**
     * Copy constructor
     */
    public Point(Point other) {
        dimension = other.getDimension();
        int i = 0;
        while (i <= dimension) {
            pointList.add(other.pointList.get(i));
            i++;
        }
    }

    /**
     * @return The dimension of the point.  For example, the point [x=0, y=1] has
     * a dimension of 2.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Converts a point to a string.  Note that this must be formatted EXACTLY
     * for the autograder to be able to read your answer.
     * Example:
     * Given a point with coordinates {x=1, y=1, z=3}
     * Return the string "1 1 3"
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Float f : pointList) {
            sb.append(f.toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * One of the WritableComparable methods you need to implement.
     * See the Hadoop documentation for more details.
     * You should order the points "lexicographically" in the order of the coordinates.
     * Comparing two points of different dimensions results in undefined behavior.
     */
    public int compareTo(Point o) {
        if (dimension != o.dimension) {
            throw new IllegalArgumentException("Not the same dimensions.");
        }
        double error = 0.0001;
        for (int i = 0; i < dimension; i++) {
            double diff = pointList.get(i) - o.pointList.get(i);
            if (diff > error) return 1;
            if (diff < -error) return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        return this.compareTo((Point)o) == 0;
    }

    /**
     * @return The L2 distance between two points.
     */
    public static final float distance(Point x, Point y) {
        double distance = 0;
        if (x.getDimension() != y.getDimension()) {
            throw new IllegalArgumentException("Not the same dimensions.");
        }
        int i = 0;
        while (i < x.getDimension()){
            double diff = Math.abs(y.pointList.get(i) - x.pointList.get(i));
            distance += diff*diff;
            i++;
        }
        return new Float(Math.sqrt(distance));
    }

    /**
     * @return A new point equal to [x]+[y]
     */
    public static final Point addPoints(Point x, Point y) {
        StringBuilder sb = new StringBuilder();
        if (x.getDimension() != y.getDimension()) {
            throw new IllegalArgumentException("Not the same dimensions.");
        }
        int i = 0;
        while (i < x.getDimension()){
            sb.append(y.pointList.get(i) + x.pointList.get(i) + " ");
            i++;
        }
        sb.setLength(sb.length() - 1);
        return new Point(sb.toString());
    }

    /**
     * @return A new point equal to [c][x]
     */
    public static final Point multiplyScalar(Point x, float c) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < x.getDimension()){
            sb.append(c * x.pointList.get(i) + " ");
            i++;
        }
        sb.setLength(sb.length() - 1);
        return new Point(sb.toString());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(dimension);
        for(int i=0;i<dimension;i++)
            dataOutput.writeFloat(pointList.get(i));
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        dimension = dataInput.readInt();
        for(int i=0;i<dimension;i++)
            pointList.set(i,dataInput.readFloat());
    }

}
