package org.imse.gaitrawparser.data.input;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.Compatibility;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;
import org.omg.CORBA.portable.RemarshalException;

public class FileParser {
	/**
	 * This constant is needed to find "dead" sensor.
	 * These are sensors, which produce a pressure value
	 * greater than zero but aren't set back to zero
	 * later.
	 * If a sensor hasn't changed its pressure value
	 * for MAX_CONST_PRESSURE_SEC its assumed to be
	 * dead...
	 */
	private static final double MAX_CONST_PRESSURE_SEC = 0.2;
	
	/**
	 * This constant is needed to handle cases where a foot
	 * seems to be completely off the ground but then
	 * touches the ground again.
	 * MIN_SWING_TIME is the time that two pressure
	 * points of the same Foot (as in Right or Left) have
	 * to be appart so they are thought to belong to a
	 * different footprint
	 */
	private static final double MIN_SWING_TIME = 0.2;
	
	/**
	 * This constant is used to find cases where
	 * GaitRawParser fails. If there are lesss than
	 * MIN_PRESSURE_POINTS_PER_PRINT pressure points
	 * in a footprint GaitRawParser assumes that something
	 * went wrong.
	 */
	private static final int MIN_PRESSURE_POINTS_PER_PRINT = 5;
	
	/**
	 * This constant is used to find cases where
	 * GaitRawParser fails. There are pressure points
	 * in a footprint whose x-coordinate differs by more
	 * than MAX_FOOT_PRINT_SENSORS_LENGTH GaitRawParser
	 * assumes that something went wrong.
	 */
	private static final int MAX_FOOT_PRINT_SENSORS_LENGTH = 40;

	private static List<PressurePoint> points;
	private static List<FootPrint> footPrints;

	private static int lenY;

	private static int lenX;

	public static FileParserResult parseFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		points = new ArrayList<PressurePoint>();

		String input = "";
		String[] temp;

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
		

		
		int oldLenX = lenX;
		int oldLenY = lenY;
		
		lenX = lenX + 20;
		lenY = lenY + 20;

		footPrints = new ArrayList<FootPrint>();
		
		
		Collections.sort(points, new Comparator<PressurePoint>() {

			@Override
			public int compare(PressurePoint o1, PressurePoint o2) {
				return new Double(o1.getTime()).compareTo(o2.getTime());
			}
		});
		
		PressurePoint[][] mat = new PressurePoint[lenX][lenY];
		
		int begin = 0;
		
		List<FootPrint> printz = footPrints;
		List<PressurePoint> pointz = points;
		

		List<PressurePoint> tempPoints = new ArrayList<PressurePoint>();
		for (int i = 0; i < points.size(); i++) {
			PressurePoint p = points.get(i);
			if (rotate) {
				p.setX(oldLenX - 1 - p.getX());
				p.setY(oldLenY - 1 - p.getY());
			}
			p.setX(p.getX() + 15);
			p.setY(p.getY() + 15);
			if (p.isPartOfFoot()) {
				tempPoints.add(p);
			}
		}
		
		for (;;) {
			Foot currentFoot = null;
			int i = 0;
			for (; i < tempPoints.size(); i++) {
				PressurePoint p = tempPoints.get(i);
				if (currentFoot == null) {
					currentFoot = p.getFoot();
				}
				mat[p.getX()][p.getY()] = (p.getPressure() == 0) ?
						null : p;
				boolean footDone = true;
				boolean otherFootFound = false;
				for (int x = 0; x < lenX; x++) {
					for (int y = 0; y < lenY; y++) {
						if (mat[x][y] != null) {
							if (p.getTime() - mat[x][y].getTime() > MAX_CONST_PRESSURE_SEC) {
								mat[x][y] = null;
								continue;
							}
							if (mat[x][y].getFoot() == currentFoot) {
								footDone = false;
								break;
							} else {
								otherFootFound = true;
							}
						}
					}
					if (!footDone) {
						break;
					}
				}
				if (footDone && otherFootFound) {
					boolean swingOk = true;
					for (int j = i + 1; j < tempPoints.size(); j++) {
						if (tempPoints.get(j).getFoot() == currentFoot) {
							if (tempPoints.get(j).getTime() - tempPoints.get(i).getTime() < MIN_SWING_TIME) {
								swingOk = false;
							}
							break;
						}
					}
					if (swingOk) {
						addFootPrint(tempPoints, i, currentFoot);
						break;
					}
				}
			}
			List<PressurePoint> newTempPoints = new ArrayList<PressurePoint>();
			Foot remaingPointsFoot = null;
			boolean allOneFoot = true;
			for (int j = 0; j < tempPoints.size(); j++) {
				PressurePoint p = tempPoints.get(j);
				if (j > i || p.getFoot() != currentFoot) {
					if (remaingPointsFoot == null) {
						remaingPointsFoot = p.getFoot();
					}
					if (remaingPointsFoot != p.getFoot()) {
						allOneFoot = false;
					}
					newTempPoints.add(p);
				}
			}
			tempPoints = newTempPoints;
			if (allOneFoot) {
				if (remaingPointsFoot != null) {
					addFootPrint(tempPoints, tempPoints.size() - 1, remaingPointsFoot);
				}
				break;
			}
			currentFoot = (currentFoot == Foot.Left) ?
					Foot.Right : Foot.Left;
		}

		FileParserResult result = new FileParserResult();
		result.setFootPrints(footPrints);
		result.setPressurePoints(points);
		result.setLenX(lenX);
		result.setLenY(lenY);
		result.setResultType(FileParserResultType.ResultOk);
		for (int i = 0; i < footPrints.size(); i++) {
			FootPrint print = footPrints.get(i);
			if (print.getPressurePoints().size() < MIN_PRESSURE_POINTS_PER_PRINT) {
				result.setResultType(FileParserResultType.TooSmallPrint);
				result.setFootPrintProblemIndex(i);
				break;
			}
			int minX = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			for (PressurePoint p : print.getPressurePoints()) {
				if (p.getX() < minX) {
					minX = p.getX();
				}
				if (p.getX() > maxX) {
					maxX = p.getX();
				}
			}
			if (maxX - minX > MAX_FOOT_PRINT_SENSORS_LENGTH) {
				result.setResultType(FileParserResultType.TooBigPrint);
				result.setFootPrintProblemIndex(i);
				break;
			}
		}
		return result;
	}
	
	private static void addFootPrint(List<PressurePoint> ppoints, int upToIndex, Foot foot) {
		FootPrint print = (foot == Foot.Left) ?
				new LeftFootPrint(lenX, lenY) : new RightFootPrint(lenX, lenY);
		for (int j = 0; j <= upToIndex; j++) {
			if (ppoints.get(j).getFoot() == foot) {
				print.addPressurePoint(ppoints.get(j));
			}
		}
		footPrints.add(print);
	}
}
