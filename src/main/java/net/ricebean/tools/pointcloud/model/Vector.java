package net.ricebean.tools.pointcloud.model;

/**
 * Model object of a point.
 */
public class Vector {

    private final String name;

    private final float x;

    private final float y;

    private final float z;

    /**
     * Private default constructor.
     */
    private Vector() {
        throw new AssertionError();
    }

    /**
     * Custom constructor.
     *
     * @param x The X coordinate of the point.
     * @param y The Y coordinate of the point.
     * @param z The Z coordinate of the point.
     */
    public Vector(float x, float y, float z) {
        this(x,y,z,null);
    }

    /**
     * Custom constructor.
     *
     * @param x The X coordinate of the point.
     * @param y The Y coordinate of the point.
     * @param z The Z coordinate of the point.
     * @param z Name of the point.
     */
    public Vector(float x, float y, float z, String name) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for the X coordinate of the point.
     *
     * @return The X coordinate of the point.
     */
    public float getX() {
        return x;
    }

    /**
     * Getter for the Y coordinate of the point.
     *
     * @return The Y coordinate of the point.
     */
    public float getY() {
        return y;
    }

    /**
     * Getter for the Z coordinate of the point.
     *
     * @return The Z coordinate of the point.
     */
    public float getZ() {
        return z;
    }

    /**
     * Getter for the  name of the point.
     *
     * @return The name of the point.
     */
    public String getName() {
        return name;
    }

    /**
     * Subtracts the other point from the current one.
     *
     * @param other The other point
     */
    public final Vector subtract(Vector other) {
        return new Vector(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    /**
     * Cross product of the current point and the other one.
     *
     * @param other The other vector
     */
    public final Vector cross(Vector other) {
        return new Vector(
                this.y * other.z - this.z * other.y,
                other.x * this.z - other.z * this.x,
                this.x * other.y - this.y * other.x
        );
    }

    /**
     * Returns the length of this vector.
     *
     * @return The length of this vector
     */
    public final float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Returns the squared length of the current vector.
     *
     * @return The squared length of the current vector
     */
    public final float lengthSquared() {
        return (this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Scalar multiplication of the current vector with a scale factor.
     *
     * @param s The scalar factor.
     */
    public final Vector scale(float s) {
        return new Vector(
                this.x * s,
                this.y * s,
                this.z * s
        );
    }

    /**
     * Adds the other Vector to the current one.
     *
     * @param other The sum of the current and the other vector.
     */
    public final Vector add(Vector other) {
        return new Vector(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    /**
     * Normalizes this vector.
     */
    public final Vector normalize()
    {
        float factor = 1 / length();

        return new Vector(
                this.x * factor,
                this.y * factor,
                this.z * factor
        );
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Float.compare(vector.x, x) != 0) return false;
        if (Float.compare(vector.y, y) != 0) return false;
        return Float.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
