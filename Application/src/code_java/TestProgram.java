// package test;

// IMPORT LIST
import java.lang.System;
import java.io.*;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.imageio.*;

import imageapi.*;
import imageapi.tools.*;

/**
 * Test program for the image processing API.
 */
public class TestProgram extends JPanel {
	/**
	 *  Has no effect on rest of program. Safely ignore this.
	 *  <p>
	 *  A non-declared serialVerionUID causes compilation warning.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Width of the canvas.
	 */
	public int width;

	/**
	 * Height of the canvas.
	 */
	public int height;

	/**
	 * Scale factor which deternmines the scale of each drawn pixel.
	 * <p>
	 * Uses drawRect() in Graphics2D class.
	 */
	public int scale = 1;

	/**
	 * Array of integer pixel elements.
	 */
	public static int[][] canvas;

	public TestProgram() {
		width = 0;
		height = 0;
	}

	public TestProgram(CompositeImage composite) {
		width = composite.getWidth();
		height = composite.getHeight();

		canvas = composite.getMatrix();
	}

	public TestProgram(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();

		canvas = Tools.getPixelsFast(image);
	}

	public TestProgram(int[][] composite) {
		width = composite[0].length;
		height = composite.length;
		canvas = composite;
	}

	// Assuming that short[][] is 12-bit RAW format
	public TestProgram(short[][] colorData) {
		canvas = new int[colorData.length][colorData[0].length];
		for (int i = 0; i < colorData.length; i++) {
			for (int j = 0; j < colorData[i].length; j++) {
				canvas[i][j] = Tools.mapper(colorData[i][j], 4096, 256);
			}
		}
		Tools.from256toARGB(canvas);
		// System.out.println(canvas.length + " " + canvas[0].length);
		width = canvas[0].length;
		height = canvas.length;
		scale = 5;
	}

	// Generate image
	public TestProgram(int width, int height, int scale, boolean colors) {
		this.width = width;
		this.height = height;
		this.scale = scale;

		if (colors) canvas = Blur.linearBlur(Tools.makeColorfulNoise(width, height), 7).getMatrix();
		else canvas = Blur.linearBlur(Tools.makeNoise(width, height), 7).getMatrix();
	}

	public TestProgram(int average, int channel) {
		width = 1;
		height = 1;

		scale = 50;
		canvas = new int[1][1];

		switch (channel) {
			case 0: Tools.from256toARGB(canvas);
							break;
			case 1: canvas[0][0] = 255 << 24;
							canvas[0][0] += average << 16;
							break;
			case 2: canvas[0][0] = 255 << 24;
							canvas[0][0] += average << 8;
							break;
			case 3: canvas[0][0] = 255 << 24;
							canvas[0][0] += average;
							break;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[i].length; j++) {
				Color color = new Color(canvas[i][j]);
				g2d.setColor(color);
				g2d.fillRect(j*scale, i*scale, scale, scale);
				// g2d.scale();
			}
		}
	}

	public int getCanvasWidth() {
		return width;
	}

	public int getCanvasHeight() {
		return height;
	}

	public int getCanvasScale() {
		return scale;
	}

	public void setCanvasWidth(int newWidth) {
		width = newWidth;
	}

	public void setCanvasHeight(int newHeight) {
		height = newHeight;
	}

	public void setCanvasScale(int newScale) {
		scale = newScale;
	}

	// MISC. METHODS
	public void printValues() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.println(canvas[i][j]);
			}
		}
	}

	public static void main(String[] args) throws IOException {

		JFrame frame = new JFrame("Sketch");

		System.out.println();
		System.out.println("    ______          __     ____                                      ");
		System.out.println("   /_  __/__  _____/ /_   / __ \\_________  ____ __________ _____ ___ ");
		System.out.println("    / / / _ \\/ ___/ __/  / /_/ / ___/ __ \\/ __ `/ ___/ __ `/ __ `__ \\");
		System.out.println("   / / /  __(__  ) /_   / ____/ /  / /_/ / /_/ / /  / /_/ / / / / / /");
		System.out.println("  /_/  \\___/____/\\__/  /_/   /_/   \\____/\\__, /_/   \\__,_/_/ /_/ /_/ ");
		System.out.println("	                                /____/");

		System.out.println();
		System.out.println(" 1 - Generate random image");
		System.out.println(" 2 - Load image");
		System.out.println(" 3 - Test Algebraic Color To Greyscale");
		System.out.println(" 4 - Test gauss blur iterations");
		System.out.println();


		do {

			Console c = System.console();
			int option = -1;
			try {
				option = Integer.parseInt(c.readLine("Choose one: "));
			} catch (NumberFormatException e) {
				System.err.println("Must be a number!");
			}

			TestProgram test;
			test = null;

			if (option == 1) {
				// Generate random image
				int width, height, scale;
				boolean color;
				System.out.println("Grey\tColor");
				System.out.println("1\t2");

				int choice = Integer.parseInt(c.readLine("Setting: "));
				if (choice == 1) color = false;
				else if (choice == 2) color = true;
				else break;

				width = Integer.parseInt(c.readLine("Width of Array: "));
				height = Integer.parseInt(c.readLine("Height of Array: "));
				scale = Integer.parseInt(c.readLine("Scale factor: "));

				test = new TestProgram(width, height, scale, color);

		  } else if (option == 2) {
				BufferedImage image = ImageIO.read(new File("media/testimage_small.jpg"));
				test = new TestProgram(image);
			} else if (option == 3) {
				CompositeImage composite = new CompositeImage(ImageIO.read(new File("media/testimage_smaller.jpg")));

				ALGEBRAIC_COLOR_TO_GREYSCALE grey = new ALGEBRAIC_COLOR_TO_GREYSCALE(composite);
				composite = grey.getResult();
				test = new TestProgram(composite);

			} else if (option == 4) {
				CompositeImage composite = new CompositeImage(ImageIO.read(new File("media/testimage_even_smaller.jpg")));
				composite = Blur.gaussBlur(composite, 5);
				// test.setCanvasScale(2);
				test = new TestProgram(composite);
			} else if (option == 5) {
				CompositeImage composite = new CompositeImage(ImageIO.read(new File("media/testimage_smaller.jpg")));
				composite = Gradient.getGradient(composite);
				test = new TestProgram(composite.getMatrix());
			} else {
				System.exit(0);
			}

			test.setLocation(500, 500);

			frame.add(test);
			// frame.repaint(0, 0, 0, test.gtX(), test.gtY());
			frame.setSize(test.width * test.scale, test.height * test.scale);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);

		} while (true);
	}
}
