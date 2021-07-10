package pkg.view;

import java.awt.image.BufferedImage;

public class SpriteSheet { //Takes an image designated as a sprite sheet and lets us select different portions to use as different sprites.

    private final BufferedImage image;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage grabImage(int col, int row, int width, int height) {
        return image.getSubimage((col * 192) - 192, ((row * 192) - 192), width, height);
    }
    public BufferedImage grabRImage(int col, int row, int width, int height) {
        return image.getSubimage((col * -192) + 1920, ((row * 192) - 192), width, height);
    }

}
