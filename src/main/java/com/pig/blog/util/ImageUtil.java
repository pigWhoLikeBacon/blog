package com.pig.blog.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	public static void thumd(File img) {
		try {
			BufferedImage image = ImageIO.read(img);
			Builder<BufferedImage> builder = null;
			
			int imageWidth = image.getWidth();
			int imageHeitht = image.getHeight();
			if ((float) 180 / 320 != (float) imageWidth / imageHeitht) {
				if (imageWidth * 180 > imageHeitht * 320) {
					image = Thumbnails.of(img).height(180).asBufferedImage();
				} else {
					image = Thumbnails.of(img).width(320).asBufferedImage();
				}
				builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 320, 180).size(320, 180);
			} else {
				builder = Thumbnails.of(image).size(320, 180);
			}

			builder.toFile(img.getAbsolutePath() + "_320x180.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
