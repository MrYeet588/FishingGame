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
    private boolean fishingRodAction;
    private FishingRodPullOut fishingRodPullOut;

    public DisplayPanel() {
        fishingRodOut = false;
        fishingRodAction = false;
        addMouseListener(this);
        background = new Background();
        fishingRodFishing = new FishingRodFishing();
        fishingRodPullOut = new FishingRodPullOut();
        timer = new Timer(1, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getBackground(),0 ,0,null);
        if (fishingRodAction) {
            g.drawImage(fishingRodPullOut.getFishingRodPullOut(), 0, 0, null);
            g.drawImage(fishingRodFishing.getFishingRodFishing(), 0, 0, null);
        }
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
                fishingRodAction = true;
                System.out.println("start");
            }else {
                fishingRodOut = false;
                System.out.println("Stop");
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
