package com.sid.Rubik;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Mosaic {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("Started");
		genearetMosaic("rabit.jpeg");
		System.out.println("Ended: " + (System.currentTimeMillis() - start) + " ms");
	}

	private static void genearetMosaic(String filePath) throws IOException {
		List<Color> constantColors = getconstantColors();

		/* ===================== Create User View Image ===================== */

		BufferedImage originalImage = ImageIO.read(new File(filePath));

		BufferedImage newImage = resizeImage(originalImage, 900, 900);

		for (int x = 0; x < newImage.getWidth(); x++) {
			int countY = 1;
			for (int y = 0; y < newImage.getHeight(); y++) {

				try {
					Color pixelColor = new Color(newImage.getRGB(x, y));
					if (countY == 3) {
						newImage.setRGB(x, y, Color.BLACK.getRGB());
						countY = 1;
					} else {
						Color newColor = constantColors.get(FindNearestColor(constantColors, pixelColor));
						newImage.setRGB(x, y, newColor.getRGB());
						countY++;
					}

				} catch (Exception e) {
				}
			}
		}

		ImageIO.write(newImage, "jpg", new FileOutputStream(new File("output.jpg")));
	}

	public static List<Color> getconstantColors() {
		List<Color> color = new ArrayList<Color>();

		color.add(new Color(237, 237, 237)); // White
		color.add(new Color(9, 35, 166)); // Blue
		color.add(new Color(186, 0, 0)); // Red
		color.add(new Color(236, 207, 14)); // Yellow
		color.add(new Color(236, 103, 14)); // Orange
		color.add(new Color(9, 128, 7)); // Green
		return color;
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT) {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	public static int FindNearestColor(List<Color> constantColors, Color current) {
		double shortestDistance;
		int index;

		index = -1;
		shortestDistance = new Integer(Integer.MAX_VALUE);

		for (int i = 0; i < constantColors.size(); i++) {
			Color match;
			long distance;

			match = constantColors.get(i);
			distance = (long) getDistance(current, match);

			if (distance < shortestDistance) {
				index = i;
				shortestDistance = distance;
			}
		}

		return index;
	}

	public static int getDistance(Color current, Color match) {
		int redDifference;
		int greenDifference;
		int blueDifference;
		int alphaDifference;

		alphaDifference = current.getAlpha() - match.getAlpha();
		redDifference = current.getRed() - match.getRed();
		greenDifference = current.getGreen() - match.getGreen();
		blueDifference = current.getBlue() - match.getBlue();

		return alphaDifference * alphaDifference + redDifference * redDifference + greenDifference * greenDifference + blueDifference * blueDifference;
	}

}
