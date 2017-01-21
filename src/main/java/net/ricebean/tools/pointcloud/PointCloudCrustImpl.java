package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Vector;
import net.ricebean.tools.pointcloud.model.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Implementation to find the crust of a point cloud.
 *
 * @Author: Stefan Meissner
 */
public class PointCloudCrustImpl implements PointCloudCrust {

    private final Vector[] pointCloud;

    /**
     * Private constructor.
     */
    private PointCloudCrustImpl() {
        throw new AssertionError("Private constructor call is not allowed.");
    }

    /**
     * Custom constructor.
     *
     * @param pointCloud The point cloud.
     */
    public PointCloudCrustImpl(List<Vector> pointCloud) {
        this.pointCloud = pointCloud.toArray(new Vector[]{});
    }

    /**
     * Compute the crust of a point cloud.
     *
     * @param radius The radius of the analyze ball.
     * @return List of triangles representing the crust of the point cloud.
     */
    @Override
    public List<Triangle> computeCrustTriangles(float radius) {

        // iterate over all triangles
        long startTime = System.currentTimeMillis();

        List<Triangle> triangles = new ArrayList<>(pointCloud.length);


        IntStream.range(0, pointCloud.length).forEach(corner_1 -> {
            for (int corner_2 = corner_1 + 1; corner_2 < pointCloud.length; corner_2++) {
                for (int corner_3 = corner_2 + 1; corner_3 < pointCloud.length; corner_3++) {

                    // the triangle
                    Vector[] triangle = new Vector[]{pointCloud[corner_1], pointCloud[corner_2], pointCloud[corner_3]};

                    // calculate the center of the triangle
                    Vector triangleCenter = triangleCenter(triangle);

                    if (triangleCenter != null) {
                        // get both ball centers
                        Vector[] ballCenters = ballCenter(radius, triangle, triangleCenter);

                        // analyze triangle
                        if (ballCenters != null) {
                            boolean keep = analyzeTriangle(triangle, ballCenters, radius, pointCloud);

                            if (keep) {
                                triangles.add(new Triangle(corner_1, corner_2, corner_3));
                            }
                        }
                    }
                }
            }

            System.out.println(
                    String.format(
                            "Compute point %d of %d. - Triangles found: %d",
                            corner_1,
                            pointCloud.length,
                            triangles.size())
            );
        });

        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("Number of Triangles: " + triangles.size());

        return triangles;
    }

    /**
     * Returns the circumcenter of a triangle.
     *
     * @param triangle The tree vectors defining a the triangle.
     * @return The center point of the triangle.
     */
    private Vector triangleCenter(Vector[] triangle) {

        // Algorithm:
        //
        //         |c-a|^2 [(b-a)x(c-a)]x(b-a) + |b-a|^2 (c-a)x[(b-a)x(c-a)]
        // m = a + ---------------------------------------------------------.
        //                            2 | (b-a)x(c-a) |^2
        //
        //                                                by Jonathan R Shewchuk
        //                                               (http://www.ics.uci.edu/~eppstein/junkyard/circumcenter.html)

        Vector ac = triangle[2].subtract(triangle[0]);
        Vector ab = triangle[1].subtract(triangle[0]);

        Vector abXac = ab.cross(ac);

        if (abXac.length() == 0) {
            return null;
        }

        // v1 = |c-a|^2 [(b-a)x(c-a)]x(b-a)
        Vector v1 = abXac.cross(ab).scale(ac.lengthSquared());

        // v2 = |b-a|^2 (c-a)x[(b-a)x(c-a)]
        Vector v2 = ac.cross(abXac).scale(ab.lengthSquared());

        // f = 2 | (b-a)x(c-a) |^2
        float f = 2f * abXac.lengthSquared();

        // get final vector
        Vector v = v1.add(v2).scale(1 / f);
        return triangle[0].add(v);
    }

    /**
     * Returns the center of the ball with defined radius and which contains the three points defined on the surface.
     *
     * @param radius         The radius of the ball.
     * @param triangle       Array of corners contained by the surface
     * @param triangleCenter The center of the triangle.
     * @return The two vectors of the centers of the two ball.
     */
    private Vector[] ballCenter(float radius, Vector[] triangle, Vector triangleCenter) {

        // vector: corner 0 (a) to triangle center (m)
        Vector am = triangleCenter.subtract(triangle[0]);

        // length: triangle center (m) to ball center (h) (pythagoras)
        float amLen = am.length();

        if (amLen > radius) {
            // ball is to small
            return null;
        }

        float mhLen = (float) Math.sqrt(radius * radius - amLen * amLen);

        // compute orthogonal vector of triangle
        Vector ab = triangle[1].subtract(triangle[0]);
        Vector orth = am.cross(ab);

        if (orth.length() == 0) {
            Vector ac = triangle[2].subtract(triangle[0]);
            orth = am.cross(ac);
        }

        // normalized vector: triangle center (m) to ball center (h)
        Vector mhNorm = orth.normalize();

        // compute and return centers of the two balls (vector addition)
        Vector mh_1 = mhNorm.scale(mhLen);
        Vector mh_2 = mhNorm.scale(mhLen * -1);

        return new Vector[]{
                triangleCenter.add(mh_1),
                triangleCenter.add(mh_2)
        };
    }

    /**
     * Analyze the given triangle
     *
     * @param triangle    The given triangle.
     * @param ballCenters The two ball centers for the given triangle
     * @param radius      The radius of the ball
     * @param pointCloud  The point cloud.
     * @return True in case the triangle is part of the crust, otherwise false.
     */
    private boolean analyzeTriangle(Vector[] triangle, Vector[] ballCenters, float radius, Vector[] pointCloud) {

        float tolerance = 0.01f;
        Vector ball;

        // keep flag (result)
        boolean keep = true;

        // analyze ball ONE
        ball = ballCenters[0];

        for (int i = 0; i < pointCloud.length && keep; i++) {
            if (ball.subtract(pointCloud[i]).length() < radius - tolerance) {
                keep = false;
            }
        }

        // analyze ball TWO (if required)
        if (!keep) {
            // reset flag
            keep = true;

            ball = ballCenters[1];

            for (int i = 0; i < pointCloud.length && keep; i++) {
                if (ball.subtract(pointCloud[i]).length() < radius - tolerance) {
                    keep = false;
                }
            }
        }

        // return result
        return keep;
    }
}
