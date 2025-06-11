import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FishingMinigamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer gameTimer;
    private int barY, fishY;
    private final int barHeight = 50;
    private final int fishSize = 20;
    private boolean pressing;
    private double progress;
    private Random rand = new Random();

    public FishingMinigamePanel() {
        setPreferredSize(new Dimension(300, 400));
        setBackground(Color.CYAN);
        addKeyListener(this);
        setFocusable(true);

        barY = 200;
        fishY = rand.nextInt(360);
        progress = barY / 2 - barHeight / 2;

        gameTimer = new Timer(30, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (pressing) barY -= 5;
        else barY += 5;

        // Clamp bar position
        barY = Math.max(0, Math.min(350, barY));

        // Move fish randomly
        fishY += rand.nextInt(11) - 5;
        fishY = Math.max(0, Math.min(380, fishY));

        // Check if fish is in bar
        if (fishY >= barY && fishY <= barY + barHeight) {
            progress += 0.5;
        } else {
            progress -= 0.7;
        }

        progress = Math.max(0, Math.min(100, progress));

        repaint();

        if (progress >= 100) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "You caught the fish!");
        } else if (progress <= 0) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "The fish got away...");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(50, barY, 20, barHeight);

        g.setColor(Color.ORANGE);
        g.fillOval(55, fishY, fishSize, fishSize);

        g.setColor(Color.BLACK);
        g.drawString("Progress: " + (int)progress + "%", 100, 20);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) pressing = false;
    }
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) pressing = true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fishing Minigame Prototype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FishingMinigamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
