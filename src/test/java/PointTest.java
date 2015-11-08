import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EvanKing on 11/6/15.
 */
public class PointTest {
    public static final double error = .0001;
    @Test
     public void test_tostring(){
        Point p = new Point("1 2 3");
        assertEquals("1.0 2.0 3.0", p.toString());
    }

    @Test
    public void test_distance(){
        Point x = new Point("1 2 3");
        Point y = new Point("4 5 6");
        assertEquals(5.196152210, Point.distance(x, y), error);
    }

    @Test
    public void test_addpoint(){
        Point x = new Point("1 2 3");
        Point y = new Point("4 5 6");
        Point z = new Point("5 7 9");
        assertEquals(z, Point.addPoints(x, y));
    }

    @Test
    public void test_compareto(){
        Point x = new Point("1 2 3");
        Point y = new Point("1 2 3");
        assertTrue(x.compareTo(y) == 0);
    }

    @Test
    public void test_multiply(){
        Point x = new Point("1 2 3");
        Point y = new Point("2 4 6");
        assertEquals(y, Point.multiplyScalar(x, 2));
    }
}