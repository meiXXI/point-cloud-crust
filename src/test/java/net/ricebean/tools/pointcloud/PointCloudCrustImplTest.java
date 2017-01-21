package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Vector;
import net.ricebean.tools.pointcloud.model.Triangle;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * JUnit test case for PrintCloudCrustImpl
 */
public class PointCloudCrustImplTest {

    private static final String RES_POINTS = "/point_cloud_1.txt";

    private PointCloudCrustImpl pointCloudCrust;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        pointCloudCrust = null;
    }

    @Test
    public void computeCrustTrianglesSimple() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(100);
        pointCloud.add(new Vector(0, 0, 0));
        pointCloud.add(new Vector(0, 2, 0));
        pointCloud.add(new Vector(2, 0, 0));
        pointCloud.add(new Vector(2, 2, 0));
        pointCloud.add(new Vector(1, 1, 2));
        pointCloud.add(new Vector(1, 1, 1));

        // act
        PointCloudCrustImpl pointCloudCrust = new PointCloudCrustImpl(pointCloud);
        List<Triangle> triangles = pointCloudCrust.computeCrustTriangles(10f);

        // assert
        // assertEquals("Number of triangles is wrong.", 6, triangles.size());

        // csv output
        csvOutput(pointCloud, triangles);
    }

    @Test
    @Ignore
    public void computeCrustTrianglesComplex() throws Exception {
        // arrange
        List<Vector> pointCloud = generatePointCloud();
        PointCloudCrustImpl pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        // act
        List<Triangle> triangles = pointCloudCrust.computeCrustTriangles(20f);

        // assert

        // csv output
        csvOutput(pointCloud, triangles);
    }

    @Test
    public void triangleCenter2D() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);
        pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 0);
        Vector v3 = new Vector(2, 2, 0);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector expected = new Vector(2, 0, 0);


        // act
        Vector result = callTriangleCenter(triangle);

        // assert
        assertEquals("The center is wrong.", expected, result);
    }

    @Test
    public void triangleCenter3D() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);
        pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector expected = new Vector(2, -0.25f, 1);


        // act
        Vector result = callTriangleCenter(triangle);

        // assert
        assertEquals("The center is wrong.", expected, result);
    }

    @Test
    public void triangleCenterNull() throws Exception {

        // arrange
        pointCloudCrust = new PointCloudCrustImpl(generatePointCloud());

        Vector[] triangle = new Vector[]{
                new Vector(48.12f, 73.47f, 4.61f),
                new Vector(88.87f, 1.20f, -5.84f),
                new Vector(88.87f, 1.20f, -5.84f)
        };

        // act
        Vector triangleCenter = callTriangleCenter(triangle);

        // assert
        assertNull(triangleCenter);
    }

    @Test
    public void ballCenter() throws Exception {

        // arrange
        List<Vector> pointCloud = new ArrayList<>(0);
        pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        float radius = 10f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 0);
        Vector v3 = new Vector(2, 2, 0);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, 0, 0);

        // act
        Vector[] result = callBallCenter(radius, triangle, triangleCenter);

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
        pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        float radius = 7.5f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, -0.25f, 1);

        // act
        Vector[] result = callBallCenter(radius, triangle, triangleCenter);

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
        pointCloudCrust = new PointCloudCrustImpl(pointCloud);

        float radius = 0.2f;

        Vector v1 = new Vector(0, 0, 0);
        Vector v2 = new Vector(4, 0, 2);
        Vector v3 = new Vector(2, 2, 1);
        Vector[] triangle = new Vector[]{v1, v2, v3};

        Vector triangleCenter = new Vector(2, -0.25f, 1);

        // act
        Vector[] result = callBallCenter(radius, triangle, triangleCenter);

        // assert
        assertNull(result);
    }

    @Test
    public void analyzeTest_1() throws Exception {

        // arrange
        pointCloudCrust = new PointCloudCrustImpl(generatePointCloud());

        Vector[] triangle = new Vector[]{
                new Vector(48.12f, 73.47f, 4.61f),
                new Vector(88.87f, 1.20f, -5.84f),
                new Vector(88.87f, 1.20f, -5.84f)
        };

        Vector triangleCenter = callTriangleCenter(triangle);

        // act
        // boolean keep = callAnalyzeTriangle();

        // assert
    }

    @Test
    @Ignore
    public void readTextFile() throws Exception {
        List<Vector> pointCloud = generatePointCloud();
        List<Triangle> triangles = new ArrayList<>(0);

        csvOutput(pointCloud, triangles);
    }

    /**
     * Helper method for calling ballCenter method.
     */
    private Vector[] callBallCenter(float radius, Vector[] triangle, Vector triangleCenter) throws Exception {
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("ballCenter", float.class, Vector[].class, Vector.class);
        method.setAccessible(true);
        return (Vector[]) method.invoke(pointCloudCrust, radius, triangle, triangleCenter);
    }

    /**
     * Helper method for calling the triangleCenter method.
     */
    private Vector callTriangleCenter(Vector[] triangle) throws Exception {
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("triangleCenter", Vector[].class);
        method.setAccessible(true);
        return (Vector) method.invoke(pointCloudCrust, new Object[]{triangle});
    }

    /**
     * Helper method for calling the triangleCenter method.
     */
    private Boolean callAnalyzeTriangle(Vector[] triangle, Vector[] ballCenters, float radius, Vector[] pointCloud) throws Exception {
        Method method = PointCloudCrustImpl.class.getDeclaredMethod("analyzeTriangle", Vector[].class, Vector[].class, float.class, Vector[].class);
        method.setAccessible(true);
        return (boolean) method.invoke(pointCloudCrust, triangle, ballCenters, radius, pointCloud);
    }

    /**
     * Helper method for CSV output.
     */
    private void csvOutput(List<Vector> pointCloud, List<Triangle> triangles) throws Exception {

        // csv output
        File file = new File("/Users/stefan/Desktop/plotly-" + System.currentTimeMillis() + ".csv");
        FileOutputStream fos = new FileOutputStream(file);

        int lines = pointCloud.size() > triangles.size() ? pointCloud.size() : triangles.size();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < lines; i++) {

            // point cloud
            if (pointCloud.size() > i) {
                Vector v = pointCloud.get(i);
                bw.write(String.format("%f\t%f\t%f\t%s\t", v.getX(), v.getY(), v.getZ(), v.getName()));
            } else {
                bw.write("\t\t\t");
            }

            // triangles
            if (triangles.size() > i) {
                Triangle t = triangles.get(i);
                bw.write(String.format("%d\t%d\t%d\t", t.getCorner_1(), t.getCorner_2(), t.getCorner_3()));
            } else {
                bw.write("\t\t\t");
            }

            bw.write("#cccccc\t2");

            // new line
            bw.newLine();
        }

        bw.close();
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
                        Float.valueOf(s[9]),
                        Float.valueOf(s[10]),
                        s[0]
                ));

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pointCloud;
    }
}