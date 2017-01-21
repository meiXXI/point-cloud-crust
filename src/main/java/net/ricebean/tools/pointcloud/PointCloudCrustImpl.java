package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Point;
import net.ricebean.tools.pointcloud.model.Triangle;

import java.util.Collections;
import java.util.List;

/**
 * Implementation to find the crust of a point cloud.
 * @Author: Stefan Meissner
 */
public class PointCloudCrustImpl implements PointCloudCrust {

    private final List<Point> pointCloud;

    /**
     * Private constructor.
     */
    private PointCloudCrustImpl() {
        throw new AssertionError("Private constructor call is not allowed.");
    }

    /**
     * Custom constructor.
     * @param pointCloud The point cloud.
     */
    public PointCloudCrustImpl(List<Point> pointCloud) {
        this.pointCloud = Collections.unmodifiableList(pointCloud);
    }

    /**
     * Compute the crust of a point cloud.
     * @param radius The radius of the analyze ball.
     * @return List of triangles representing the crust of the point cloud.
     */
    @Override
    public List<Triangle> computeCrustTriangles(double radius) {
        return null;
    }
}
