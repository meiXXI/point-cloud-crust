package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Vector;
import net.ricebean.tools.pointcloud.model.Triangle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * JUnit test case for PrintCloudCrustImpl
 */
public class PointCloudCrustImplTest {

    private static final String RES_POINTS = "/point_cloud_1.txt";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void computeCrustTriangles() throws Exception {
        // arrange
        List<Vector> pointCloud = generatePointCloud();
        PointCloudCrustImpl pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        // act
        List<Triangle> triangles = pointCloudCrust.computeCrustTriangles(10f);

        // assert
    }

    @Test
    public void triangleCenter2D() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 0);
        Vector v3 = new Vector(2, 2, 0);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector expected = new Vector(2, 0, 0);


        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("triangleCenter", Vector[].class);
        method.setAccessible(true);
        Vector result = (Vector) method.invoke(new PointCloudCrustImpl(pointCloud), new Object[]{triangle});

        // assert
        assertEquals("The center is wrong.", expected, result);
    }

    @Test
    public void triangleCenter3D() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector expected = new Vector(2, -0.25f, 1);


        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("triangleCenter", Vector[].class);
        method.setAccessible(true);
        Vector result = (Vector) method.invoke(new PointCloudCrustImpl(pointCloud), new Object[]{triangle});

        // assert
        assertEquals("The center is wrong.", expected, result);
    }

    @Test
    public void ballCenter() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        float radius = 10f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 0);
        Vector v3 = new Vector(2, 2, 0);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, 0, 0);

        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("ballCenter", float.class, Vector[].class, Vector.class);
        method.setAccessible(true);
        Vector[] result = (Vector[]) method.invoke(new PointCloudCrustImpl(pointCloud), radius, triangle, triangleCenter);

        // assert
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v1).length(), 0.0001f);
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v2).length(), 0.0001f);
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v3).length(), 0.0001f);

        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v1).length(), 0.0001f);
        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v2).length(), 0.0001f);
        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v3).length(), 0.0001f);
    }

    @Test
    public void ballCenter_2() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        float radius = 7.5f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, -0.25f, 1);

        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("ballCenter", float.class, Vector[].class, Vector.class);
        method.setAccessible(true);
        Vector[] result = (Vector[]) method.invoke(new PointCloudCrustImpl(pointCloud), radius, triangle, triangleCenter);

        // assert
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v1).length(), 0.0001f);
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v2).length(), 0.0001f);
        assertEquals("Distance to Ball 1 is wrong.", radius, result[0].subtract(v3).length(), 0.0001f);

        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v1).length(), 0.0001f);
        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v2).length(), 0.0001f);
        assertEquals("Distance to Ball 2 is wrong.", radius, result[1].subtract(v3).length(), 0.0001f);
    }

    @Test
    public void ballCenter_SmallBall() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        float radius = 0.2f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, -0.25f, 1);

        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("ballCenter", float.class, Vector[].class, Vector.class);
        method.setAccessible(true);
        Vector[] result = (Vector[]) method.invoke(new PointCloudCrustImpl(pointCloud), radius, triangle, triangleCenter);

        // assert
        assertNull(result);
    }

    /**
     * Helper method for creating a large amount of points.
     *
     * @return Large amount of points.
     */
    private List<Vector> generatePointCloud() throws URISyntaxException {
        List<Vector> pointCloud = new ArrayList<Vector>(2000);

        try (Stream<String> stream = Files.lines(Paths.get(
                PointCloudCrustImplTest.class.getResource(RES_POINTS).toURI()
        ))) {

            stream.forEach(it -> {
                String[] s = it.split("\\s+");
                pointCloud.add(new Vector(
                        Float.valueOf(s[8]),
                        Float.valueOf(s[7]),
                        Float.valueOf(s[7])
                ));

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pointCloud;
    }
}