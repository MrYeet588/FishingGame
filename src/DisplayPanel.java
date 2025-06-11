import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class DisplayPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
    private Background background;
    private FishingRodFishing fishingRodFishing;
    private Timer timer;
    private boolean fishingRodOut;
    private FishingRodPullIn fishingRodPullIn;
    private boolean fishingTime;
    private FishingQTE fishing;
    private Fish fish;
    private Fish caughtFish; // store caught fish for message display
    private Inventory inventory;
    private boolean showFailMessage = false;
    private Timer failMessageTimer;
    private final Font bigBoldFont = new Font("Arial", Font.BOLD, 24);

    private boolean inventoryOpen = false;

    // Minigame variables
    private boolean minigameActive = false;
    private Timer minigameTimer;
    private int barY, fishY;
    private int fishVelocity;
    private final int barHeight = 50;
    private final int fishSize = 20;
    private boolean pressingSpace;
    private double progress;
    private Random rand = new Random();

    public DisplayPanel() {
        inventory = new Inventory();
        fishingRodOut = false;
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        fishVelocity = 0;

        background = new Background();
        fishingRodFishing = new FishingRodFishing();
        fishingRodPullIn = new FishingRodPullIn();
        fishingRodPullIn.storeImages();

        timer = new Timer(30, this); // repaint every 30ms
        timer.start();

        fishing = new FishingQTE(this);
        fish = new Fish();
        caughtFish = null;

        // Setup minigame timer (same delay as main timer)
        minigameTimer = new Timer(30, e -> runMinigameTick());

        failMessageTimer = new Timer(3000, e -> {
            showFailMessage = false;
            failMessageTimer.stop();
            repaint();
        });
        failMessageTimer.setRepeats(false);
    }

    private void runMinigameTick() {
        // Move player bar
        if (pressingSpace) barY -= 7;
        else barY += 7;
        barY = Math.max(0, Math.min(350, barY));

        int fishCenter = fishY + fishSize / 2;
        int barCenter = barY + barHeight / 2;
        int distance = barCenter - fishCenter;

        // Small attraction force towards the bar (very slight bias)
        double attractionStrength = 0.1;  // smaller value means weaker bias

        // Calculate attraction velocity component (rounded)
        int attractionVelocity = (int) Math.signum(distance) * (int)(Math.min(Math.abs(distance) * attractionStrength, 1));

        // Add random jitter between -2 and +2
        int randomVelocity = rand.nextInt(5) - 2;

        // Update fish velocity combining random and slight bias
        fishVelocity += randomVelocity + attractionVelocity;

        // Clamp velocity between -4 and 4 for smooth movement
        if (fishVelocity > 4) fishVelocity = 4;
        if (fishVelocity < -4) fishVelocity = -4;

        fishY += fishVelocity;

        // Clamp fish position to screen height
        fishY = Math.max(0, Math.min(380, fishY));

        // Progress logic
        if (fishY >= barY && fishY <= barY + barHeight) {
            progress += 0.5;
        } else {
            progress -= 0.7;
        }
        progress = Math.max(0, Math.min(100, progress));

        if (progress >= 100) {
            minigameSuccess();
        } else if (progress <= 0) {
            minigameFail();
        }

        repaint();
    }

    private void minigameSuccess() {
        minigameActive = false;
        minigameTimer.stop();
        fishing.caughtFish();
        caughtFish = fish;
        inventory.addFish(fish);
        fish = new Fish(); // prepare next fish
        System.out.println("You caught a " + caughtFish.fishInfo());
        // Start rod pull back animation after success
        startRodPullBackAnimation();
    }

    private void minigameFail() {
        minigameActive = false;
        minigameTimer.stop();

        System.out.println("The fish got away...");

        // Show fail message for 3 seconds
        showFailMessage = true;
        failMessageTimer.start();

        // Start rod pull back animation after fail
        startRodPullBackAnimation();
    }

    private void startRodPullBackAnimation() {
        fishingRodPullIn.storeImages2();
        fishingRodOut = false;
        fishingRodPullIn.getAnimation().startTimer();
        fishingTime = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background.getBackground(), 0, 0, null);

        if (fishingRodOut && fishingRodPullIn.getAnimation().timerOn()) {
            g.drawImage(fishingRodPullIn.getFishingRodAnimation(), 0, 0, null);
        } else {
            if (!fishingTime) {
                g.drawImage(fishingRodPullIn.getFishingRodAnimation(), 0, 0, null);
            } else {
                g.drawImage(fishingRodFishing.getFishingRodFishing(), 0, 0, null);
            }
        }

        if (inventoryOpen) {
            drawInventory(g);
        }

        // Show fail message if active
        if (showFailMessage) {
            // Draw a semi-transparent dark overlay behind text
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(bigBoldFont);
            g.setColor(Color.RED);

            String message = "The fish got away";
            // Center text horizontally and vertically
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + fm.getAscent();

            g.drawString(message, x, y);

            return; // skip other drawing when showing fail message
        }

        if (minigameActive) {
            drawMinigame(g);
            return;
        }

        if (!inventoryOpen) {
            fishing.setFishingRodOut(fishingRodOut);
            fishing.update();
        }

        if (fishing.fishOnHook) {
            g.setFont(bigBoldFont);
            g.setColor(Color.BLACK);
            g.drawString("Fish on hook! Click to start minigame!", 300, 40);
        }

        if (fishing.caughtMessageOn && caughtFish != null) {
            g.setFont(bigBoldFont);
            g.setColor(Color.BLACK);
            g.drawString("You caught a " + caughtFish.fishInfo(), 300, 70);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.BLACK);
        g.drawString("Timer: " + fishing.fishingTimerTime, 10, 20);
    }

    private void drawMinigame(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw vertical outline for green bar movement range
        g.setColor(Color.WHITE);
        g.drawRect(48, 0, 24, 400);

        // Draw green bar (player controlled)
        g.setColor(Color.GREEN);
        g.fillRect(50, barY, 20, barHeight);

        // Draw fish
        g.setColor(Color.ORANGE);
        g.fillOval(50, fishY, fishSize, fishSize);

        // Progress bar background
        int progressBarX = 100;
        int progressBarY = 400;
        int progressBarWidth = 200;
        int progressBarHeight = 20;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight);

        // Progress fill
        int fillWidth = (int) (progress / 100 * progressBarWidth);
        g.setColor(Color.CYAN);
        g.fillRect(progressBarX, progressBarY, fillWidth, progressBarHeight);

        // Border
        g.setColor(Color.WHITE);
        g.drawRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight);

        // Progress text - BIG and BOLD
        g.setFont(bigBoldFont);
        g.setColor(Color.WHITE);
        g.drawString("Progress: " + (int) progress + "%", progressBarX, progressBarY - 15);

        // Instruction text - BIG and BOLD
        g.drawString("Hold SPACE to keep the green bar aligned!", 100, 40);
    }

    private void drawInventory(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150)); // semi-transparent black background
        g.fillRect(50, 50, 400, 400);

        g.setColor(Color.WHITE);
        g.drawString("Inventory:", 70, 70);

        List<String> inventoryLines = inventory.getFishInfoList();
        int y = 90;
        for (String line : inventoryLines) {
            g.drawString(line, 70, y);
            y += 20;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (inventoryOpen) return; // ignore fishing actions if inventory is open
        if (minigameActive) return; // ignore mouse during minigame

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!fishingRodOut) {
                // Cast rod
                fishingRodOut = true;
                fishingRodPullIn.storeImages();
                fishingRodPullIn.getAnimation().startTimer();
                fishingTime = true;
                fishing.fishingTimer.startTimer();
                System.out.println("Fishing rod released");
            } else {
                if (fishing.fishOnHook) {
                    // Start minigame instead of instant catch
                    System.out.println("Starting fishing minigame!");
                    startFishingMinigame();
                } else {
                    // Pull rod back normally (no fish on hook)
                    startRodPullBackAnimation();
                    System.out.println("Fishing rod pulled back");
                }
            }
        }
    }

    private void startFishingMinigame() {
        minigameActive = true;

        // Initialize minigame variables
        barY = getHeight() / 2 - barHeight / 2; // start bar halfway down screen
        fishY = getHeight() / 2 - fishSize / 2; // spawn fish exactly in middle vertically
        progress = 50; // start progress in the middle
        pressingSpace = false;

        // Stop fishing QTE timer updates to freeze rod
        fishing.setFishingRodOut(false);

        minigameTimer.start();
    }

    // MouseListener empty methods
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        // toggle inventory on 'e' or 'E' key press
        if (e.getKeyChar() == 'e' || e.getKeyChar() == 'E') {
            if (!minigameActive) {  // disable inventory toggle during minigame
                inventoryOpen = !inventoryOpen;
                System.out.println("Inventory " + (inventoryOpen ? "opened" : "closed"));
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (minigameActive) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                pressingSpace = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (minigameActive) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                pressingSpace = false;
            }
        }
    }
}
