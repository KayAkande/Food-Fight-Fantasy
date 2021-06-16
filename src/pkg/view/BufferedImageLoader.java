package pkg.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Zack (RealTutsGML)
 */
public class BufferedImageLoader { //This class reads/loads graphics to use in the program

    private BufferedImage image; //takes desired object image as an argument

    /**
     * Argumented constructor
     *
     * @param path
     * @return image
     */
    public BufferedImage loadImage(String path) {
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {

            e.printStackTrace();
        }

        return image;
    }
}
