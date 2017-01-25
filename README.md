# PointCloudCrust Library
The PointCloudCurst is an implementation of the PointCloudCrust Algorithm (or in-officially called "The Cheese-Maker") which builds a crust around a point cloud (triangulation of a point cloud). The input of the algorithm is a list of points located in a three dimensional space plus the radius of a ball and the output is a set of triangles which defines the "crust".
The Algorithm does not only create the outer crust but also visualize innersided holes, structures and even breakthroughs (like a cheese). A typcial practical use case of the Point Crust Algorithm is the visualization of color spaces:

![Color Space triangulated by Point Cloud Crust Algorithm.](https://github.com/ricebean-net/PointCloudCrust/blob/master/docs/point-cloud-crust-algorithm.png "Color Space triangulated by Point Cloud Crust Algorithm.")

## Maven Dependencies
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
        @Grab(group='net.ricebean.tools.pointcloud', module='PointCloudCrust', version='0.1')
)

// read points from file located on github.com (last three columns) - INPUT
List<String> points = new URL (
        "https://raw.githubusercontent.com/ricebean-net/PointCloudCrust/master/src/test/resources/point_cloud_1.txt"
).getText().readLines()

// prepare input
List<Vector> pointCloud = new ArrayList<>(points.size())

points.forEach{
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
        String[] p = points.get(it).split()
        file << "${p[0]}\t${p[8]}\t${p[9]}\t${p[10]}\t"
    } else {
        file << "\t\t\t\t"
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
