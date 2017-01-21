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

/**
 * JUnit test case for PrintCloudCrustImpl
 */
public class VectorCloudCrustImplTest {

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
        List<Triangle> triangles = pointCloudCrust.computeCrustTriangles(10d);

        // assert
    }

    @Test
    public void triangleCenter2D() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 0);
        Vector v3 = new Vector(2, 2, 0);

        Vector expected = new Vector(2, 0, 0);


        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("triangleCenter", Vector.class, Vector.class, Vector.class);
        method.setAccessible(true);
        Vector result = (Vector) method.invoke(new PointCloudCrustImpl(pointCloud), v1, v2, v3);

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

        Vector expected = new Vector(2, -0.25f, 1);


        // act
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("triangleCenter", Vector.class, Vector.class, Vector.class);
        method.setAccessible(true);
        Vector result = (Vector) method.invoke(new PointCloudCrustImpl(pointCloud), v1, v2, v3);

        // assert
        assertEquals("The center is wrong.", expected, result);
    }

    /**
     * Helper method for creating a large amount of points.
     *
     * @return Large amout of points.
     */
    private List<Vector> generatePointCloud() throws URISyntaxException {
        List<Vector> pointCloud = new ArrayList<Vector>(2000);

        try (Stream<String> stream = Files.lines(Paths.get(
                VectorCloudCrustImplTest.class.getResource(RES_POINTS).toURI()
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