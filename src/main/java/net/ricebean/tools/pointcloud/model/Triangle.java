package net.ricebean.tools.pointcloud.model;

/**
 * Model object of a triangle.
 */
public class Triangle {

    private final int corner_1;

    private final int corner_2;

    private final int corner_3;

    /**
     * Private default constructor.
     */
    private Triangle() {
        throw new AssertionError("Default constructor call is not allowed.");
    }

    /**
     * Custom constructor.
     * @param corner_1 Index of the point of corner 1.
     * @param corner_2 Index of the point of corner 2.
     * @param corner_3 Index of the point of corner 3.
     */
    public Triangle(int corner_1, int corner_2, int corner_3) {
        this.corner_1 = corner_1;
        this.corner_2 = corner_2;
        this.corner_3 = corner_3;
    }

    /**
     * Getter for the index of the point of corner 1.
     * @return The index of the point of corner 1.
     */
    public int getCorner_1() {
        return corner_1;
    }

    /**
     * Getter for the index of the point of corner 2.
     * @return The index of the point of corner 2.
     */
    public int getCorner_2() {
        return corner_2;
    }

    /**
     * Getter for the index of the point of corner 3.
     * @return The index of the point of corner 3.
     */
    public int getCorner_3() {
        return corner_3;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "corner_1=" + corner_1 +
                ", corner_2=" + corner_2 +
                ", corner_3=" + corner_3 +
                '}';
    }
}
