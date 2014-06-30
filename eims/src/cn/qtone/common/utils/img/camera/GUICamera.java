package cn.qtone.common.utils.img.camera;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * 屏幕照相机
 * 
 * @author 马必强
 * 
 */
public class GUICamera
{
	/*
	 * 内部支持的几种文件存储格式
	 */
	public static final int IMG_JPG = 0;

	public static final int IMG_PNG = 1;

	public static final int IMG_BMP = 2;

	private static final String[] IMG_FORMAT = { "jpg", "png", "bmp" };

	private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	private String fileName; // 图片保存的文件名(可加路径信息)

	private int imgFormatIndex; // 图片文件的存储格式

	private boolean stopSnap = false; // 是否停止的标志，默认是不停止

	/**
	 * 默认构造方法。使用自动生成的文件名和JPEG的格式存储
	 * 
	 */
	public GUICamera()
	{
		this.imgFormatIndex = GUICamera.IMG_JPG;
		this.fileName = "GUICamera_" + System.currentTimeMillis() + "."
				+ GUICamera.IMG_FORMAT[imgFormatIndex];
	}

	/**
	 * 指定文件名
	 * 
	 * @param fileName
	 */
	public GUICamera(String fileName)
	{
		this.imgFormatIndex = GUICamera.IMG_JPG;
		this.fileName = fileName + "." + GUICamera.IMG_FORMAT[imgFormatIndex];
	}

	/**
	 * 指定文件名和存储格式。格式错误将使用默认的格式进行存储！
	 * 
	 * @param fileName
	 * @param imgFormat
	 */
	public GUICamera(String fileName, int imgFormat)
	{
		if (imgFormat < 0 || imgFormat >= GUICamera.IMG_FORMAT.length) {
			this.imgFormatIndex = GUICamera.IMG_JPG;
		} else {
			this.imgFormatIndex = imgFormat;
		}
		this.fileName = fileName + "." + GUICamera.IMG_FORMAT[imgFormat];
	}

	/**
	 * 抓取屏幕并保存成图象文件！该方法将抓取全屏幕
	 * 
	 */
	public void snapShot()
	{
		try {
			int width = (int) d.getWidth(), height = (int) d.getHeight();

			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0, width, height));

			// 生成指定的图象文件
			File imgFile = new File(fileName);

			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, GUICamera.IMG_FORMAT[imgFormatIndex],
					imgFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 抓取指定范围内的屏幕区域。如果坐标超出了范围则取全屏幕
	 * 
	 * @param x1
	 *            左上角的横坐标
	 * @param y1
	 *            左上角的纵坐标
	 * @param x2
	 *            右下角的横坐标
	 * @param y2
	 *            右下角的纵坐标
	 */
	public void snapShot(int x1, int y1, int x2, int y2)
	{
		try {
			int width = (int) d.getWidth(), height = (int) d.getHeight();

			x1 = x1 < 0 ? 0 : (x1 > width ? width : x1);
			y1 = y1 < 0 ? 0 : (y1 > height ? height : y1);
			x2 = x2 > width ? width : (x2 < 0 ? 0 : x2);
			y2 = y2 > height ? height : (y2 < 0 ? 0 : y2);

			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = (new Robot())
					.createScreenCapture(new Rectangle(x1, y1, x2, y2));

			// 生成指定的图象文件
			File imgFile = new File(fileName);

			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, GUICamera.IMG_FORMAT[imgFormatIndex],
					imgFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 设置是否停止拍照的的操作。一般用在连续拍照的过程中
	 * 
	 * @param stopSnap
	 */
	public void setSnapStop(boolean stopSnap)
	{
		this.stopSnap = stopSnap;
	}

	/**
	 * 连续拍照。此时指定的文件名将无效，系统使用自定的文件名来生成图象文件确保不会产生冲突
	 * 
	 * @param times
	 *            指定拍照的次数，如果为-1则表示一直拍。直到设置setSnapStop为true时停止
	 * @param delay
	 *            延迟的时间数，即两次连续拍照的间隔时间。单位是ms
	 * @param ops
	 *            将图象文件写入的流。为null则表示写入文件中，否则要指定输出流
	 */
	public void snapShotContinue(int times, long delay, OutputStream ops)
	{
		if (times < 0 && times != -1) {
			times = 0;
		}
		delay = delay < 0 ? 0 : delay;
		try {
			Robot robot = new Robot();
			Rectangle rect = new Rectangle(0, 0, (int) d.getWidth(), (int) d
					.getHeight());
			while (times == -1 ? !stopSnap : times-- != 0) {
				fileName = "gc_" + System.currentTimeMillis() + "."
						+ GUICamera.IMG_FORMAT[imgFormatIndex];
				BufferedImage bi = robot.createScreenCapture(rect);
				if (ops == null) {
					File imgFile = new File(fileName);
					ImageIO.write(bi, GUICamera.IMG_FORMAT[imgFormatIndex],
							imgFile);
				} else {
					ImageIO
							.write(bi, GUICamera.IMG_FORMAT[imgFormatIndex],
									ops);
				}

				if (delay > 0) {
					Thread.sleep(delay);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
