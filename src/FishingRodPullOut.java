import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FishingRodPullOut {
    private Animation2 animation;

    public FishingRodPullOut(){
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String fileName;
            fileName = "src\\FishingRodPullOut\\FishingRodPullOut00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation2(images, 200);
    }

    public BufferedImage getFishingRodPullOut() {
        return animation.getActiveFrame();
    }

}
