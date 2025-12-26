package com.industry.math;

import java.util.ArrayList;
import java.util.List;

public class Matrix3 {

    public Vector3 xHat;
    public Vector3 yHat;
    public Vector3 zHat;

    public Matrix3(List<List<Double>> matrix) {
        if (matrix.size() != 3) throw new IllegalArgumentException("matrix must have dimensions of 3");

        for (List<Double> row : matrix) {
            if (row.size() != 3) throw new IllegalArgumentException("matrix must have dimensions of 3");
        }

        xHat = new Vector3(matrix.get(0).getFirst(), matrix.get(1).getFirst(), matrix.get(2).getFirst());
        yHat = new Vector3(matrix.get(0).get(1),  matrix.get(1).get(1), matrix.get(2).get(1));
        zHat = new Vector3(matrix.get(0).get(2),  matrix.get(1).get(2), matrix.get(2).get(2));
    }

    public Matrix3(Vector3 xHat, Vector3 yHat, Vector3 zHat) {
        this.xHat = xHat;
        this.yHat = yHat;
        this.zHat = zHat;
    }

    public Vector3 transform(Vector3 vector) {
        Vector3 v1 = xHat.multiply(vector.x);
        Vector3 v2 = yHat.multiply(vector.y);
        Vector3 v3 = zHat.multiply(vector.z);

        return v1.add(v2).add(v3);
    }

    public Matrix3 multiply(Matrix3 matrix) {
        Vector3 xh = matrix.transform(xHat);
        Vector3 yh = matrix.transform(yHat);
        Vector3 zh = matrix.transform(zHat);

        return new Matrix3(xh, yh, zh);
    }

    public static Matrix3 getRotationMatrix(Vector3 axis, double twist) {
        axis = axis.normalize();
        double ux = axis.x, uy = axis.y, uz = axis.z;
        double cosT = Math.cos(twist);
        double sinT = Math.sin(twist);
        double oneMinusCos = 1 - cosT;

        double[][] m = new double[3][3];
        m[0][0] = cosT + ux*ux*oneMinusCos;
        m[0][1] = ux*uy*oneMinusCos - uz*sinT;
        m[0][2] = ux*uz*oneMinusCos + uy*sinT;

        m[1][0] = uy*ux*oneMinusCos + uz*sinT;
        m[1][1] = cosT + uy*uy*oneMinusCos;
        m[1][2] = uy*uz*oneMinusCos - ux*sinT;

        m[2][0] = uz*ux*oneMinusCos - uy*sinT;
        m[2][1] = uz*uy*oneMinusCos + ux*sinT;
        m[2][2] = cosT + uz*uz*oneMinusCos;

        // Convert to your Matrix3 class
        return new Matrix3(
                new Vector3(m[0][0], m[1][0], m[2][0]),
                new Vector3(m[0][1], m[1][1], m[2][1]),
                new Vector3(m[0][2], m[1][2], m[2][2])
        );
    }

    public static Matrix3 getTwistMatrix(Vector3 axis, double angle, double twist) {
        Matrix3 rotation = Matrix3.getRotationMatrix(axis, angle);

        if (twist != 0) {
            Matrix3 twistMatrix = Matrix3.getRotationMatrix(axis, twist);
            rotation = twistMatrix.multiply(rotation);
        }

        return rotation;
    }

    public static Matrix3 getScaleMatrix(double scaleX, double scaleY, double scaleZ) {
        return new Matrix3(
                new Vector3(scaleX, 0,  0),
                new Vector3(0,  scaleY, 0),
                new Vector3(0,  0,  scaleZ)
        );
    }
}
