package org.imse.gaitrawparser.data;

import org.eclipse.swt.graphics.Point;

public class Line {

	private Point p2;
	private Point p1;
	private Point A;

	public Line(Point p1, Point p2) {
		if (p1.x < p2.x) {
			this.p1 = p1;
			this.p2 = p2;
		} else {
			this.p1 = p2;
			this.p2 = p1;
		}
		A = new Point(p2.x - p1.x, p2.y - p1.y);
	}
	
	public Line(int p1x, int p1y, int p2x, int p2y) {
		this(new Point(p1x, p1y), new Point(p2x, p2y));
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}
	
	public Point getA() {
		return A;
	}
	
	public double getYForX(double x) {
		double lambda = (x - ((double) p1.x)) / ((double) A.x);
		return ((double) p1.y) + lambda * ((double) A.y);
	}
	
	public double getXForY(double y) {
		double lambda = (y - ((double) p1.y)) / ((double) A.y);
		return ((double) p1.x) + lambda * ((double) A.x);
	}

	public DoublePoint getIntersection(Line agLine) {
		double[][] matrix = new double[2][3];
		matrix[0][0] = (double) A.x;
		matrix[0][1] = (double) -agLine.getA().x;
		matrix[0][2] = (double) (agLine.getP1().x - p1.x);
		matrix[1][0] = (double) A.y;
		matrix[1][1] = (double) -agLine.getA().y;
		matrix[1][2] = (double) (agLine.getP1().y - p1.y);
		if(!solveMatrix(matrix)) {
			return null;
		}
		double lambda = matrix[0][2];
		return new DoublePoint(((double) p1.x) + ((double) A.x) * lambda,
				((double) p1.y) + ((double) A.y) * lambda);
	}
	
	private boolean solveMatrix(double[][] matrix) {
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
