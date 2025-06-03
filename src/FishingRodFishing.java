import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FishingRodFishing {
    private Animation animation;

    public FishingRodFishing(){
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            String fileName;
                fileName = "src\\FishingRodFishingImages\\FishingRodFishing00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation(images, 200);
    }

    public BufferedImage getFishingRodFishing() {
        return animation.getActiveFrame();
    }
}
