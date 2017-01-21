package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Triangle;

import java.util.List;

/**
 * Interface of the point cloud crust algorighm.
 * @Author Stefan Meissner
 */
public interface PointCloudCrust {

    /**
     * Compute the crust of a point cloud.
     * @param radius The radius of the analyze ball.
     * @return List of triangles representing the crust of the point cloud.
     */
    public List<Triangle> computeCrustTriangles(double radius);
}
