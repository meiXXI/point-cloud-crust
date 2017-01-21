package net.ricebean.tools.pointcloud.model;

/**
 * Model object of a point.
 */
public class Point {

    private final double x;

    private final double y;

    private final double z;

    /**
     * Private default constructor.
     */
    private Point() {
        throw  new AssertionError();
    }

    /**
     * Custom constructor.
     * @param x The X coordinate of the point.
     * @param y The Y coordinate of the point.
     * @param z The Z coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for the X coordinate of the point.
     * @return The X coordinate of the point.
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the Y coordinate of the point.
     * @return The Y coordinate of the point.
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for the Z coordinate of the point.
     * @return The Z coordinate of the point.
     */
    public double getZ() {
        return z;
    }
}
