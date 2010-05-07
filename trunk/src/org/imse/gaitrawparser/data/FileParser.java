package org.imse.gaitrawparser.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class FileParser {
	private static final int TOLERANCE = 10;

	private static List<PressurePoint> points;
	private static List<FootPrint> footPrints;

	private static int lenY;

	private static int lenX;

	public static void parseFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		points = new ArrayList<PressurePoint>();

		String input = "";
		String[] temp = new String[7];

		lenX = 0;
		lenY = 0;

		
		try {
			while ((input = reader.readLine()) != null) {
				temp = input.split(",");
				PressurePoint p = new PressurePoint();
				p.setTime(Double.parseDouble(temp[0]));
				p.setX(Integer.parseInt(temp[1]));
				p.setY(Integer.parseInt(temp[2]));
				if (p.getX() >= lenX) {
					lenX = p.getX() + 1;
				}
				if (p.getY() >= lenY) {
					lenY = p.getY() + 1;
				}
				p.setPressure(Integer.parseInt(temp[3]));
				p.setObjectNumber(Integer.parseInt(temp[4]));
				if (Integer.parseInt(temp[5]) == 0) {
					p.setFoot(Foot.Left);
				} else {
					p.setFoot(Foot.Right);
				}
				if (Integer.parseInt(temp[6]) == 0) {
					p.setPartOfFoot(true);
				} else {
					p.setPartOfFoot(false);
				}
				points.add(p);

			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		boolean rotate = false;
		
		for (PressurePoint p : points) {
			if (p.isPartOfFoot()) {
				rotate = p.getX() > lenX / 2;
				break;
			}
		}
		
		PressurePoint[][] mat = new PressurePoint[lenX][lenY];
		for (PressurePoint p : points) {
			if (rotate) {
				p.setX(lenX - 1 - p.getX());
				p.setY(lenY - 1 - p.getY());
			}
			if (p.isPartOfFoot()) {
				mat[p.getX()][p.getY()] = p;
			}
		}
		footPrints = new ArrayList<FootPrint>();
		for (int x = 0; x < lenX; x++) {
			for (int y = 0; y < lenY; y++) {
				if (mat[x][y] != null) {
					FootPrint newFoot;
					if (mat[x][y].getFoot() == Foot.Left) {
						newFoot = new LeftFootPrint(lenX, lenY);
					} else {
						newFoot = new RightFootPrint(lenX, lenY);
					}
					// Breitensuche starten
					List<PressurePoint> q = new ArrayList<PressurePoint>();
					q.add(mat[x][y]);
					mat[x][y] = null;
					for (int i = 0; i < q.size(); i++) {
						PressurePoint p = q.get(i);
						newFoot.addPressurePoint(p);
						for (int j = -TOLERANCE; j <= TOLERANCE; j++) {
							for (int k = -TOLERANCE; k <= TOLERANCE; k++) {
								if (p.getX() + j >= 0 && p.getX() + j < lenX
										&& p.getY() + k >= 0
										&& p.getY() + k < lenY) {
									if (mat[p.getX() + j][p.getY() + k] != null) {
										q.add(mat[p.getX() + j][p.getY() + k]);
										mat[p.getX() + j][p.getY() + k] = null;
									}
								}
							}
						}
					}
					footPrints.add(newFoot);
					newFoot.calculateALRG();
				}
			}
		}
		//List<FootPrint> l = footPrints;
	}

	public static List<PressurePoint> getPoints() {
		return points;
	}

	public static List<FootPrint> getFootPrints() {
		return footPrints;
	}
	
	public static int getLenX() {
		return lenX;
	}
	
	public static int getLenY() {
		return lenY;
	}
}
