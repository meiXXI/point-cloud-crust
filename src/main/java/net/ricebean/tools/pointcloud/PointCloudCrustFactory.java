package net.ricebean.tools.pointcloud;

import net.ricebean.tools.pointcloud.model.Vector;

import java.util.List;

/**
 * Factory class for getting an PointCloudCrust implementation.
 */
public class PointCloudCrustFactory {

    /**
     * Get a new instance of the PointCloudCrust Algorithm.
     * @param pointCloud The point cloud for initializing.
     * @return A initialized instance of the point cloud crust.
     */
    public static PointCloudCrust newInstance(List<Vector> pointCloud) {
        return new PointCloudCrustImpl(pointCloud);
    }
}
