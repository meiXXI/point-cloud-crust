package net.ricebean.tools.pointcloud.model;

import com.sun.deploy.config.VerboseDefaultConfig;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for the Point method.
 */
public class VectorTest {
    @Test
    public void subtract() throws Exception {

        // arrange
        Vector a = new Vector(1,2,3);
        Vector b = new Vector(4,5,6);

        // act
        Vector c = a.subtract(b);

        // assert
        Vector expected = new Vector(-3,-3,-3);
        assertEquals("Vector subtraction is wrong.", expected, c);
    }

    @Test
    public void cross() throws Exception {

        // arrange
        Vector a = new Vector(1,2,3);
        Vector b = new Vector(-7,8,9);

        // act
        Vector c = a.cross(b);

        // assert
        Vector expected = new Vector(-6,-30,22);
        assertEquals("Cross product is wrong.", expected, c);
    }

    @Test
    public void lengthSquared() throws Exception {

        // arrange
        Vector a = new Vector(1,2,3);

        // act
        float result = a.lengthSquared();

        // assert
        float expected = 14;
        assertEquals("Squared length is wrong.", expected, result, 0.001f);
    }

    @Test
    public void scale() throws Exception {

        // arrange
        Vector a = new Vector(2,1,3);
        float scale = 5;

        // act
        Vector c = a.scale(scale);

        // assert
        Vector expected = new Vector(10,5,15);
        assertEquals("Scale multiplication is wrong.", expected, c);
    }

    @Test
    public void add() throws Exception {

        // arrange
        Vector a = new Vector(1,2,3);
        Vector b = new Vector(4,5,6);

        // act
        Vector c = a.add(b);

        // assert
        Vector expected = new Vector(5,7,9);
        assertEquals("The sum is wrong.", expected, c);
    }
}