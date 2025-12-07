package com.industry.math;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.Objects;

public final class Vector3 {

    public double x;
    public double y;
    public double z;

    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3d vector) {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3(Vec3d vector) {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3 add(Vector3 vector) {
        return new Vector3(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Vector3 subtract(Vector3 vector) {
        return new Vector3(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Vector3 scale(double scalar) {
        return multiply(scalar);
    }

    public Vector3 multiply(double scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalize() {
        double len = length();
        return len == 0 ? ZERO : new Vector3(x / len, y / len, z / len);
    }

    public Vector3 applyNonLinear(NonLinearFunc function) {
        return function.apply(this);
    }

    public Vec3d toVec3d() {
        return new Vec3d(x, y, z);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public static Vector3 from(@NotNull Vec3d vec) {
        return new Vector3(vec);
    }

    public static Vector3 from(@NotNull Vector3d vec) {
        return new Vector3(vec);
    }

    public static Vector3 from(@NotNull Vector3 vec) {
        return vec;
    }

    public static Vec3d from(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3 vector)) return false;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0 &&
                Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
