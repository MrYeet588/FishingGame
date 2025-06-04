import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Background {
    private BufferedImage background;
    private Animation animation;

    public Background(){
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i <= 15; i++) {
            String fileName;
            if (i < 10){
                fileName = "src\\BackgroundImages\\Background00" + i + ".png";
            } else {
                fileName = "src\\BackgroundImages\\Background0" + i + ".png";
            }
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation(images, 175);
    }

    public void setBackground() {
        this.background = background;
    }

    public BufferedImage getBackground() {
        return animation.getActiveFrame();
    }
}
