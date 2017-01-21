package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Vector;
import net.ricebean.tools.pointcloud.model.Triangle;

import javax.vecmath.Vector3f;
import javax.xml.bind.ValidationEvent;
import java.util.List;

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
    public List<Triangle> computeCrustTriangles(double radius) {

        // iterate over all triangles
        long startTime = System.currentTimeMillis();

        int i = 0;

        for (int corner_1 = 0; corner_1 < pointCloud.length; corner_1++) {
            for (int corner_2 = corner_1 + 1; corner_2 < pointCloud.length; corner_2++) {
                for (int corner_3 = corner_2 + 1; corner_3 < pointCloud.length; corner_3++) {

                    // calculate the center of the triangle
                    Vector triangleCenter = triangleCenter(pointCloud[corner_1], pointCloud[corner_2], pointCloud[corner_3]);

                    i++;
                }
            }
        }

        System.out.print("Number of Triangles: " + i);
        System.out.print("Time: " + (System.currentTimeMillis() - startTime) + " ms");

        return null;
    }

    /**
     * Returns the center of a triangle.
     *
     * @param corner_1 Point of corner 1 in the triangle.
     * @param corner_2 Point of corner 2 in the triangle.
     * @param corner_3 Point of corner 3 in the triangle.
     * @return The center point of the triangle.
     */
    private Vector triangleCenter(Vector corner_1, Vector corner_2, Vector corner_3) {

        // Algorithm:
        //
        //         |c-a|^2 [(b-a)x(c-a)]x(b-a) + |b-a|^2 (c-a)x[(b-a)x(c-a)]
        // m = a + ---------------------------------------------------------.
        //                            2 | (b-a)x(c-a) |^2
        //
        //                                                by Jonathan R Shewchuk
        //                                               (http://www.ics.uci.edu/~eppstein/junkyard/circumcenter.html)

        Vector ac = corner_3.subtract(corner_1);
        Vector ab = corner_2.subtract(corner_1);

        Vector abXac = ab.cross(ac);

        // v1 = |c-a|^2 [(b-a)x(c-a)]x(b-a)
        Vector v1 = abXac.cross(ab).scale(ac.lengthSquared());

        // v2 = |b-a|^2 (c-a)x[(b-a)x(c-a)]
        Vector v2 = ac.cross(abXac).scale(ab.lengthSquared());

        // f = 2 | (b-a)x(c-a) |^2
        float f = 2f * abXac.lengthSquared();


        Vector v = v1.add(v2).scale(1 / f);

        return corner_1.add(v);

//       ==============================
//        - Vector3f a,b,c // are the 3 pts of the tri
//
//        - Vector3f ac = c - a ;
//        - Vector3f ab = b - a ;
//        - Vector3f abXac = ab.cross( ac ) ;
//
//        // this is the vector from a TO the circumsphere center
//        - Vector3f toCircumsphereCenter = (abXac.cross( ab )*ac.len2() + ac.cross( abXac )*ab.len2()) / (2.f*abXac.len2()) ;
//        - float circumsphereRadius = toCircumsphereCenter.len() ;
//
//        // The 3 space coords of the circumsphere center then:
//        - Vector3f ccs = a  +  toCircumsphereCenter ; // now this is the actual 3space location



    }
}
