package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static Color getColorFromTemperature(int temperature) {
        float normalizedValue = (float) (temperature - 1) / 9;

        int red = (int) (normalizedValue * 255);
        int blue = (int) ((1 - normalizedValue) * 255);

        return new Color(red, 0, blue);
    }

    public static Color getColorFromPopulation(int population, int maxPopulation) {
        float normalizedValue = (float) (population) / maxPopulation;

        int green = (int) Math.min((normalizedValue * 255), 255);

        return new Color(0, green, 0);
    }

    /*public static BufferedImage applyRGBFilter(BufferedImage originalIcon, int rFilter, int gFilter, int bFilter) {
        Image image = originalIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics gr = bufferedImage.createGraphics();
        gr.drawImage(image, 0, 0, null);
        gr.dispose();

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = Math.min(r + rFilter, 255);
                g = Math.min(g + gFilter, 255);
                b = Math.min(b + bFilter, 255);

                int newRGB = (r << 16) | (g << 8) | b;
                bufferedImage.setRGB(x, y, newRGB);
            }
        }

        return new ImageIcon(bufferedImage);
    }

     */
}
