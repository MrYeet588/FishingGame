import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation2 implements ActionListener {
    private ArrayList<BufferedImage> frames;
    private Timer timer;
    private int currentFrame;

    public Animation2(ArrayList<BufferedImage> frames, int delay) {
        this.frames = frames;
        currentFrame = 0;
        timer = new Timer(delay, this);
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            //This advances the animation to the next frame
            //It also uses modulus to reset the frame to the beginning after the last frame
            //In other words, this allows our animation to loop
            currentFrame = (currentFrame + 1) % frames.size();
            if(currentFrame == frames.size() - 1){
                timer.stop();
            }
        }
    }

    public void startTimer(){
        timer.restart();
    }
    public boolean timerOn(){
        return timer.isRunning();
    }
}
