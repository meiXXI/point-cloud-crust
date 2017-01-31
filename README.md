# PointCloudCrust Library
The PointCloudCurst is an implementation of the PointCloudCrust Algorithm (or in-officially called "The Cheese-Maker") which builds a crust around a point cloud (triangulation of a point cloud). The input of the algorithm is a list of points located in a three dimensional space plus the radius of a ball and the output is a set of triangles which defines the "crust".
The Algorithm does not only create the outer crust but also visualize innersided holes, structures and even breakthroughs (like a cheese). A typcial practical use case of the Point Crust Algorithm is the visualization of color spaces:

![Color Space triangulated by Point Cloud Crust Algorithm.](https://github.com/ricebean-net/PointCloudCrust/blob/master/docs/point-cloud-crust-algorithm.png "Color Space triangulated by Point Cloud Crust Algorithm.")

Most 3D Mesh visualization engines also allow to colorize objects. In the following a screenshot of a color space illustrating the colors as well. The interactive model can be found here: https://plot.ly/~meixxi42/14/.

![3D Mesh colorzed Color Space.](https://github.com/ricebean-net/PointCloudCrust/blob/master/docs/colorspace.png "3D Mesh colorzed Color Space.")


## Maven Dependencies
The implementation of the Point Cloud Crust Algorithm is also available in the Central Maven Repository:
```
<dependency>
    <groupId>net.ricebean.tools.pointcloud</groupId>
    <artifactId>PointCloudCrust</artifactId>
    <version>0.1</version>
</dependency>
```

## Groovy Sample Code
Here a Groovy sample code explaining how to use the PointCloudCrust library. Most lines in this sample are for data preparation and the CSV output generation. The actual processing are two lines only.
The output of this script is a list of points plus a list of triangles stored in a CSV file. This file can be used for Mesh libraries or other visualization tools in order to visualize the 3D model.

```groovy
import net.ricebean.tools.pointcloud.PointCloudCrust
import net.ricebean.tools.pointcloud.PointCloudCrustFactory
import net.ricebean.tools.pointcloud.model.Triangle
import net.ricebean.tools.pointcloud.model.Vector

import java.nio.file.Paths

@Grapes(
        @Grab(group = 'net.ricebean.tools.pointcloud', module = 'PointCloudCrust', version = '0.1')
)

// read points from file located on github.com (last three columns) - INPUT
List<Vector> pointCloud = new ArrayList<>(2000)

new URL("https://raw.githubusercontent.com/ricebean-net/PointCloudCrust/master/src/test/resources/point_cloud_1.txt")
        .getText().readLines().forEach {
    String[] p = it.split()
    pointCloud.add(new Vector(
            Float.valueOf(p[8]),
            Float.valueOf(p[9]),
            Float.valueOf(p[10]),
            p[0]
    ))
}

// calculate crust - PROCESSING
PointCloudCrust pointCloudCrust = PointCloudCrustFactory.newInstance(pointCloud)
List<Triangle> triangles = pointCloudCrust.computeCrustTriangles(20f)


// create CSV Output - OUTPUT
int noLines = pointCloud.size() > triangles.size() ? pointCloud.size() : triangles.size();

File file = Paths.get(
        System.getProperty("user.home"),
        "point-cloud-crust-triangulation-${System.currentTimeMillis()}.csv"
).toFile()

(0..noLines - 1).each {

    // point cloud (X / Y / Z)
    if (pointCloud.size() > it) {
        Vector p = pointCloud.get(it)
        file << "${p.getX()}\t${p.getY()}\t${p.getZ()}\t"
    } else {
        file << "\t\t\t"
    }

    // triangles
    if (triangles.size() > it) {
        Triangle t = triangles.get(it);
        file << "${t.getCorner_1()}\t${t.getCorner_2()}\t${t.getCorner_3()}\t"
    } else {
        file << "\t\t\t"
    }

    file << ("\n")
}
```

## The Point Cloud Crust Algorithm ("The Cheese-Maker")
The Point Cloud Crust Algorithm iterates over all triangles possible in the point cloud and decides whether to keep or not. Each triangle is being analyzed individually und independently. So, the algorithm can be executed highly scalable. The main class of the algorithm is the [PointCloudCrustImpl](https://github.com/ricebean-net/PointCloudCrust/blob/master/src/main/java/net/ricebean/tools/pointcloud/PointCloudCrustImpl.java) class. The construction of the curst is basically just trigonometry.

### Explanation 
Here the 2-dimensional explanation of the Point Cloud Algorithm. In a two dimensional world, triangles can be substituted by lines and balls by circles:

![Explanation Point Cloud Crust Algorithm.](https://github.com/ricebean-net/PointCloudCrust/blob/master/docs/point-cloud-crust-explaination.png "Explanation Point Cloud Crust Algorithm.")


Fist of all, a radius has to be defined. The radius defines the level of granularity being visualized in the final model.
When starting, the algorithm iterates over all available lines in the 2D point cloud. Each line is analyzed individually. In order to find out the "surface-lines", two circles with the initially defined radius are being calculated. Both circles passes the two points of the line. If one of the both circles has no other points inside, the line is a "surface-line" and is being cached for the output. Lines which have points in both circles are being skipped.

