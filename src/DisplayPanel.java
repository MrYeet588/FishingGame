import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DisplayPanel extends JPanel implements ActionListener, MouseListener {
    private Background background;
    private FishingRodFishing fishingRodFishing;
    private Timer timer;
    private boolean fishingRodOut;
    private FishingRodPullIn fishingRodPullIn;
    public boolean temp;
    private boolean fishingTime;
    private FishingQTE fishing;
    public Fish fish;

    public DisplayPanel() {
        fishingRodOut = false;
        temp = false;
        addMouseListener(this);
        background = new Background();
        fishingRodFishing = new FishingRodFishing();
        fishingRodPullIn = new FishingRodPullIn();
        fishingRodPullIn.storeImages();
        timer = new Timer(1, this);
        timer.start();
        fishing = new FishingQTE(this);
        fish = new Fish();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background.getBackground(),0 ,0,null);
        if (fishingRodOut && fishingRodPullIn.getAnimation().timerOn()) {
                g.drawImage(fishingRodPullIn.getFishingRodAnimation(), 0, 0, null);
        } else {
            if (!fishingTime){
                g.drawImage(fishingRodPullIn.getFishingRodAnimation(), 0, 0, null);
            } else {
                g.drawImage(fishingRodFishing.getFishingRodFishing(), 0, 0, null);

            }
        }
        if (fishing.fishOnHook){
            g.drawString("Fish on hook", 400, 10);
            System.out.println("Fish on hook");
            fishingLogic();
            if (!temp) {
                g.drawString("You caught a " + fish.fishInfo(), 400, 10);
                System.out.println("You caught a " + fish.fishInfo());
                fishing.fishOnHook = false;
            }
        }
        g.drawString(String.valueOf(fishing.fishingTimerTime), 10, 10);
    }

    public void fishingLogic(){
        fish = new Fish();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!fishingRodOut) {
                fishingRodOut = true;
                temp = true;
                fishingRodPullIn.storeImages();
                fishingRodPullIn.getAnimation().startTimer();
                fishingTime = true;
                fishing.fishOnHookAlert();
                System.out.println("Fishing rod released");
            }else {
                temp = false;
                fishingRodPullIn.storeImages2();
                fishingRodOut = false;
                fishingRodPullIn.getAnimation().startTimer();
                fishingTime = false;
                System.out.println("fishing rod pulled back");
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
