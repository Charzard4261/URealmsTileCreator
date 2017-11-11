package charzard;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Utils {

	public static BufferedImage rotate180(BufferedImage inputImage)
	{
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		BufferedImage returnImage = new BufferedImage(width, height,
				inputImage.getType());

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int tx2 = width - x - 1;

				int ty2 = width - y - 1;
				returnImage.setRGB(tx2, ty2, inputImage.getRGB(x, y));
			}
		}

		return returnImage;

	}

	public static BufferedImage createResizedCopy(Image originalImage,
			int scaledWidth, int scaledHeight, boolean preserveAlpha)
	{
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;

		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
				imageType);

		Graphics2D g = scaledBI.createGraphics();

		if (preserveAlpha)
		{
			g.setComposite(AlphaComposite.Src);
		}

		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);

		g.dispose();

		return scaledBI;
	}
}
