package org.imse.gaitrawparser.data;

public class LineHelper {

	public static DoublePoint getIntersection(DoubleLine line1, DoubleLine line2) {
		double[][] matrix = new double[2][3];
		matrix[0][0] = (double) line1.getA().x;
		matrix[0][1] = (double) -line2.getA().x;
		matrix[0][2] = (double) (line2.getP1().x - line1.getP1().x);
		matrix[1][0] = (double) line1.getA().y;
		matrix[1][1] = (double) -line2.getA().y;
		matrix[1][2] = (double) (line2.getP1().y - line1.getP1().y);
		if(!LineHelper.solveMatrix(matrix)) {
			return null;
		}
		double lambda = matrix[0][2];
		return new DoublePoint(((double) line1.getP1().x) + ((double) line1.getA().x) * lambda,
				((double) line1.getP1().y) + ((double) line1.getA().y) * lambda);
	}
	
	public static boolean solveMatrix(double[][] matrix) {
		if (matrix.length == 0) {
			throw new RuntimeException("Wrong Matrix.");
		}
		int width = matrix[0].length;
		for (int i = 0; i < matrix.length; i++) {
			if (width != matrix[i].length) {
				throw new RuntimeException("Wrong Matrix.");
			}
			for (int j = i; i < matrix.length; j++) {
				if (matrix[j][i] != 0) {
					double[] temp = matrix[j];
					matrix[j] = matrix[i];
					matrix[i] = temp;
					break;
				}
			}
			for (int j = i + 1; j < matrix.length; j++) {
				if (matrix[j][i] == 0) {
					continue;
				}
				double factor = -matrix[j][i] / matrix[i][i];
				matrix[j][i] = 0;
				for (int k = i + 1; k < width; k++) {
					matrix[j][k] = matrix[j][k] + matrix[i][k] * factor;
				}
			}
		}
		for (int i = matrix.length - 1; i >= 0; i--) {
			if (matrix[i][i] != 0) {
				matrix[i][width - 1] /= matrix[i][i];
			}
			for (int j = i - 1; j >= 0; j--) {
				if (matrix[j][i] == 0) {
					continue;
				}
				matrix[j][width - 1] += -matrix[j][i] * matrix[i][width - 1];
				matrix[j][i] = 0;
			}
		}
		return true;
	}
	
}
