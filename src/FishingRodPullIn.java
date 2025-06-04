import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FishingRodPullIn {
    public Animation2 animation;

    public FishingRodPullIn(){

    }
    public void storeImages(){
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            String fileName;
            fileName = "src\\FishingRodPullOut\\FishingRodPullOut00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation2(images, 150);
    }
    public void storeImages2(){
        ArrayList<BufferedImage> images2 = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String fileName;
            fileName = "src\\FishingRodPullOut\\FishingRodPullOut00" + i + ".png";
            try {
                images2.add(ImageIO.read(new File(fileName)));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation2(images2, 150);
    }

    public BufferedImage getFishingRodAnimation() {
        return animation.getActiveFrame();
    }

    public Animation2 getAnimation() {
        return animation;
    }
}
