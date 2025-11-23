package com.industry.math;

import java.util.List;
import java.util.Vector;

public class Matrix4 {

    private Matrix3 rotationMatrix;
    private Vector3 translation;

    public Matrix4(List<List<Double>> matrix) {
        if (matrix.size() != 4) throw new IllegalArgumentException("matrix must have dimensions of 3");

        for (List<Double> row : matrix) {
            if (row.size() != 4) throw new IllegalArgumentException("matrix must have dimensions of 3");
        }

        Vector3 xHat = new Vector3(matrix.get(0).getFirst(), matrix.get(1).getFirst(), matrix.get(2).getFirst());
        Vector3 yHat = new Vector3(matrix.get(0).get(1),  matrix.get(1).get(1), matrix.get(2).get(1));
        Vector3 zHat = new Vector3(matrix.get(0).get(2),  matrix.get(1).get(2), matrix.get(2).get(2));

        List<Double> row4 = matrix.get(3);
        if (row4.get(0) != 0 || row4.get(1) != 0 || row4.get(2) != 0)
            throw new IllegalArgumentException("Invalid Bottom Row");
        if (row4.get(3) != 1)
            throw new IllegalArgumentException("Invalid Corner");

        translation = new Vector3(matrix.get(0).get(3), matrix.get(1).get(3), matrix.get(2).get(3));
        rotationMatrix = new Matrix3(xHat, yHat, zHat);
    }

    public Matrix4(Matrix3 matrix, Vector3 translation) {
        this.translation = translation;
        this.rotationMatrix = matrix;
    }

    public Vector3 transform(Vector3 vector) {
        return translation.add(rotationMatrix.transform(vector));
    }

    public Matrix4 multiply(Matrix4 other) {
        Matrix3 combinedRotation = this.rotationMatrix.multiply(other.rotationMatrix);

        Vector3 combinedTranslation = this.rotationMatrix.transform(other.translation).add(this.translation);

        return new Matrix4(combinedRotation, combinedTranslation);
    }
}
