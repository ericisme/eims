package cn.qtone.common.utils.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 生成随机数字或字母串，以图像方式显示，用于人工识别，使程序很难识别。 减小系统被程序
 * 自动攻击的可能性。生成的图形颜色由四种不同的颜色随机组合而成，数字或字母垂直方向
 * 位置在一定范围内也是随机的，减少被程序自动识别的几率。由于数字的0，1，2易和字母的
 * o，l,z混淆，使人眼难以识别，因此排除数字中的0、1和2，字母中的l、o和z
 * 
 * @author 马必强
 */

public class RandomGraphic
{
	private final static char[] chars = {'a' ,'b', 'c', 'd', 'e', 'f', 'g', 'h',
		'i','j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', '3', '4', '5', '6', '7', '8', '9'};
	
	// 字符的高度和宽度，单位为像素
	private int wordHeight = 9;

	private int wordWidth = 12;

	// 字符大小
	private int fontSize = 16;

	// 垂直方向起始位置
	private final int initypos = 5;

	// 要生成的字符个数，由工厂方法得到
	private int charCount = 0;

	// 颜色数组，绘制字串时随机选择一个
	private static final Color[] CHAR_COLOR = { Color.RED, Color.BLUE,
			Color.GREEN, Color.MAGENTA };

	// 随机数生成器
	private Random r = new Random();

	/**
	 * 生成图像的格式常量，JPEG格式,生成为文件时扩展名为.jpg； 
	 * 输出到页面时需要设置MIME type 为image/jpeg
	 */
	public static String GRAPHIC_JPEG = "jpg";

	/**
	 * 生成图像的格式常量，PNG格式,生成为文件时扩展名为.png； 
	 * 输出到页面时需要设置MIME type 为image/png
	 */
	public static String GRAPHIC_PNG = "PNG";

	// 用工厂方法创建对象
	protected RandomGraphic(int charCount)
	{
		this.charCount = charCount;
	}

	/**
	 * 创建对象的工厂方法.指定长度
	 */
	public static RandomGraphic getInstance(int len) throws Exception
	{
		return new RandomGraphic(len);
	}

	/**
	 * 创建对象的工厂方法.不指定长度
	 */
	public static RandomGraphic getInstance() throws Exception
	{
		return new RandomGraphic(0);
	}

	/**
	 * 随机生成一个数字串，并以图像方式绘制，绘制结果输出到流out中
	 * 
	 * @param graphicFormat 设置生成的图像格式，值为GRAPHIC_JPEG或GRAPHIC_PNG
	 * @param out 图像结果输出流
	 * @return 随机生成的串的值
	 * @throws IOException
	 */
	public String drawNumber(String graphicFormat, OutputStream out)
			throws IOException
	{
		// 随机生成的串的值
		return draw(randNumber(), graphicFormat, out);

	}

	/**
	 * 随机生成一个字母串，并以图像方式绘制，绘制结果输出到流out中
	 * 
	 * @param graphicFormat 设置生成的图像格式，值为GRAPHIC_JPEG或GRAPHIC_PNG
	 * @param out 图像结果输出流
	 * @return 随机生成的串的值
	 * @throws IOException
	 */
	public String drawAlpha(String graphicFormat, OutputStream out)
			throws IOException
	{
		// 随机生成的串的值
		return draw(randAlpha(), graphicFormat, out);

	}

	/**
	 * 随机生成一个字母串，并以图像方式绘制，绘制结果输出到流out中
	 * 
	 * @param graphicFormat 设置生成的图像格式，值为GRAPHIC_JPEG或GRAPHIC_PNG
	 * @param out 图像结果输出流
	 * @return 随机生成的串的值
	 * @throws IOException
	 */
	public String drawNumberAndAlpha(String graphicFormat, OutputStream out)
			throws IOException
	{
		// 随机生成的串的值
		return draw(randNumberAndAlpha(), graphicFormat, out);

	}

	/**
	 * 以图像方式绘制字符串，绘制结果输出到流out中
	 * 
	 * @param charValue 要绘制的字符串
	 * @param graphicFormat 设置生成的图像格式，值为GRAPHIC_JPEG或GRAPHIC_PNG
	 * @param out 图像结果输出流
	 * @return 随机生成的串的值
	 * @throws IOException
	 */
	protected String draw(String charValue, String graphicFormat,
			OutputStream out) throws IOException
	{
		// 计算图像的宽度和高度
		int w = (charCount + 2) * wordWidth;
		int h = wordHeight * 3;

		// 创建内存图像区
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bi.createGraphics();

		// 设置背景色
		Color backColor = Color.WHITE;
		g.setBackground(backColor);
		g.fillRect(0, 0, w, h);

		// 设置font
		g.setFont(new Font(null, Font.BOLD, fontSize));
		// 绘制charValue,每个字符颜色随机
		for (int i = 0; i < charCount; i++) {
			String c = charValue.substring(i, i + 1);
			Color color = CHAR_COLOR[randomInt(0, CHAR_COLOR.length)];
			g.setColor(color);
			int xpos = (i + 1) * wordWidth;
			// 垂直方向上随机
			int ypos = randomInt(initypos + wordHeight, initypos + wordHeight
					* 2);
			g.drawString(c, xpos, ypos);
		}
		g.dispose();
		bi.flush();
		// 输出到流
		ImageIO.write(bi, graphicFormat, out);

		return charValue;
	}

	public String randNumber(int len)
	{
		StringBuilder charValue = new StringBuilder();
		// 生成随机数字串
		for (int i = 0; i < len; i++) {
			charValue.append(randomInt(0, 10));
		}
		return charValue.toString();
	}

	public String randNumber()
	{
		return randNumber(charCount);
	}

	public String randAlpha(int len)
	{
		StringBuilder charValue = new StringBuilder();
		// 生成随机字母串
		for (int i = 0; i < len; i++) {
			char c = (char) (randomInt(0, 26) + 'a');
			charValue.append(c);
		}
		return charValue.toString();
	}

	public String randAlpha()
	{
		return randAlpha(charCount);
	}

	public String randNumberAndAlpha(int len)
	{
		StringBuilder charValue = new StringBuilder();
		// 生成随机字母和数字串
		for (int i = 0; i < len; i++) {
			charValue.append(chars[randomInt(0, chars.length)]);
		}
		return charValue.toString();
	}

	public String randNumberAndAlpha()
	{
		return randNumberAndAlpha(charCount);
	}

	/**
	 * 返回[from,to)之间的一个随机整数
	 * 
	 * @param from  起始值
	 * @param to 结束值
	 * @return [from,to)之间的一个随机整数
	 */
	protected int randomInt(int from, int to)
	{
		// Random r = new Random();
		return from + r.nextInt(to - from);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		/*System.out.println(RandomGraphic.getInstance(5)
				.drawAlpha(RandomGraphic.GRAPHIC_PNG,
						new FileOutputStream("d:/myimg.png")));*/

		System.out.println(RandomGraphic.getInstance(5).randNumber());
		System.out.println(RandomGraphic.getInstance(5).randAlpha());
		System.out.println(RandomGraphic.getInstance(30).randNumberAndAlpha());
	}

}
