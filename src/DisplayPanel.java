import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private Fish caughtFish;
    private Inventory inventory;
    private boolean showFailMessage = false;
    private Timer failMessageTimer;
    private final Font bigBoldFont = new Font("Arial", Font.BOLD, 24);
    private boolean inventoryOpen = false;

    private boolean minigameActive = false;
    private Timer minigameTimer;
    private int barY, fishY;
    private int fishVelocity;
    private final int barHeight = 75;
    private final int fishSize = 20;
    private boolean pressingSpace;
    private double progress;
    private Random rand = new Random();

    private boolean showStartScreen = true;
    private boolean showLoadingScreen = true;
    private Timer loadingScreenTimer;

    // --- NEW for shop & money ---
    private boolean shopOpen = false;
    private double money = 0;

    // For selecting an item in the inventory/shop to sell
    private int selectedFishIndex = -1;

    // Button bounds
    private final Rectangle shopToggleButtonBounds = new Rectangle(820, 900, 150, 40);
    private final Rectangle sellButtonBounds = new Rectangle(700, 550, 150, 40);

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

        timer = new Timer(30, this);
        timer.start();

        fishing = new FishingQTE(this);
        fish = new Fish();
        caughtFish = null;

        minigameTimer = new Timer(30, e -> runMinigameTick());

        failMessageTimer = new Timer(500, e -> {
            showFailMessage = false;
            failMessageTimer.stop();
            repaint();
        });
        failMessageTimer.setRepeats(false);

        loadingScreenTimer = new Timer(2000, e -> {
            showLoadingScreen = false;
            loadingScreenTimer.stop();
            repaint();
        });
        loadingScreenTimer.setRepeats(false);
        loadingScreenTimer.start();
    }

    private void runMinigameTick() {
        if (pressingSpace) barY -= 7;
        else barY += 4;
        barY = Math.max(0, Math.min(350, barY));

        int fishCenter = fishY + fishSize / 2;
        int barCenter = barY + barHeight / 2;
        int distance = barCenter - fishCenter;

        double attractionStrength = 0.01;
        int attractionVelocity = (int) Math.signum(distance) * (int)(Math.min(Math.abs(distance) * attractionStrength, 1));
        int randomVelocity = rand.nextInt(5) - 2;
        fishVelocity += randomVelocity + attractionVelocity;
        fishVelocity = Math.max(-4, Math.min(4, fishVelocity));
        fishY += fishVelocity;
        fishY = Math.max(0, Math.min(380, fishY));

        if (fishY >= barY && fishY <= barY + barHeight) {
            progress += 0.5;
        } else {
            progress -= 1;
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
        fish = new Fish();
        System.out.println("You caught a " + caughtFish.fishInfo());
        startRodPullBackAnimation();
    }

    private void minigameFail() {
        minigameActive = false;
        minigameTimer.stop();
        System.out.println("The fish got away...");
        showFailMessage = true;
        failMessageTimer.start();
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

        if (showLoadingScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Loading...", getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        if (showStartScreen) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.WHITE);
            g.drawString("Fishing Game", getWidth() / 2 - 150, 100);
            try {
                g.drawImage((ImageIO.read(new File("src\\feesh.png"))), 690, 35, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Press ENTER to start", getWidth() / 2 - 100, 250);
            g.drawString("Press SPACE to move the bar", getWidth() / 2 - 140, 290);
            g.drawString("Click to cast/pull rod or start minigame", getWidth() / 2 - 180, 320);
            g.drawString("Press 'E' to open/close inventory", getWidth() / 2 - 150, 350);
            return;
        }

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

        if (shopOpen) {
            drawShop(g);
        }

        // Draw the Shop toggle button (bottom right)
        drawShopToggleButton(g);

        if (showFailMessage) {
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(bigBoldFont);
            g.setColor(Color.RED);

            String message = "The fish got away";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + fm.getAscent();

            g.drawString(message, x, y);
            return;
        }

        if (minigameActive) {
            drawMinigame(g);
            return;
        }

        if (!inventoryOpen && !shopOpen) {
            fishing.setFishingRodOut(fishingRodOut);
            fishing.update();
        }

        if (fishing.fishOnHook) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.BLACK);
            g.drawString("!", 880, 325);
        }

        if (fishing.caughtMessageOn && caughtFish != null) {
            g.setFont(bigBoldFont);
            g.setColor(Color.BLACK);
            g.drawString("You caught a " + caughtFish.fishInfo(), 250, 70);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.BLACK);
        g.drawString("Timer: " + fishing.fishingTimerTime, 10, 20);
    }

    private void drawShopToggleButton(Graphics g) {
        g.setColor(shopOpen ? Color.RED : Color.GREEN);
        g.fillRect(shopToggleButtonBounds.x, shopToggleButtonBounds.y, shopToggleButtonBounds.width, shopToggleButtonBounds.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String text = shopOpen ? "Close Shop" : "Open Shop";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int textX = shopToggleButtonBounds.x + (shopToggleButtonBounds.width - textWidth) / 2;
        int textY = shopToggleButtonBounds.y + (shopToggleButtonBounds.height + textHeight) / 2 - 4;
        g.drawString(text, textX, textY);
    }
    private void drawMinigame(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.drawRect(48, 0, 24, 425);

        g.setColor(Color.GREEN);
        g.fillRect(50, barY, 20, barHeight);

        g.setColor(Color.ORANGE);
        g.fillOval(50, fishY, fishSize, fishSize);

        int progressBarX = 100;
        int progressBarY = 400;
        int progressBarWidth = 200;
        int progressBarHeight = 20;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight);

        int fillWidth = (int) (progress / 100 * progressBarWidth);
        g.setColor(Color.CYAN);
        g.fillRect(progressBarX, progressBarY, fillWidth, progressBarHeight);

        g.setColor(Color.WHITE);
        g.drawRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight);

        g.setFont(bigBoldFont);
        g.setColor(Color.WHITE);
        g.drawString("Progress: " + (int) progress + "%", progressBarX, progressBarY - 15);

        g.drawString("Hold SPACE to keep the green bar aligned!", 100, 40);
    }
    private void drawShop(Graphics g) {
        // Shop background
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(50, 50, 600, 500);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Shop - Sell Your Fish", 70, 90);

        // Draw money display at top right inside shop box
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.YELLOW);
        g.drawString("Money: $" + money, 350, 90);

        // Draw fish list with selectable highlight
        List<Fish> fishList = inventory.getFishList();
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int y = 130;
        for (int i = 0; i < fishList.size(); i++) {
            Fish f = fishList.get(i);
            if (i == selectedFishIndex) {
                g.setColor(new Color(100, 100, 255, 150));
                g.fillRect(60, y - 15, 500, 25);
            }
            g.setColor(Color.WHITE);
            g.drawString(f.getName() + " - Sell Price: $" + f.getSellPrice(), 70, y);
            y += 30;
        }

        // Draw sell button
        g.setColor(selectedFishIndex >= 0 ? Color.GREEN : Color.GRAY);
        g.fillRect(sellButtonBounds.x, sellButtonBounds.y, sellButtonBounds.width, sellButtonBounds.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String sellText = "Sell Selected";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(sellText);
        int textHeight = fm.getAscent();
        int textX = sellButtonBounds.x + (sellButtonBounds.width - textWidth) / 2;
        int textY = sellButtonBounds.y + (sellButtonBounds.height + textHeight) / 2 - 4;
        g.drawString(sellText, textX, textY);
    }

    private void drawInventory(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(50, 50, 400, 400);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Inventory:", 70, 70);

        // Show money in inventory top right
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.YELLOW);
        g.drawString("Money: $" + money, 70, 420);

        List<String> inventoryLines = inventory.getFishInfoList();
        int y = 100;
        for (String line : inventoryLines) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
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
        if (showStartScreen || showLoadingScreen || minigameActive) return;

        Point p = e.getPoint();

        // Check if clicked the shop toggle button
        if (shopToggleButtonBounds.contains(p)) {
            shopOpen = !shopOpen;
            inventoryOpen = false;  // close inventory when shop opens
            selectedFishIndex = -1; // reset selection
            repaint();
            return;
        }

        // If shop is open, check shop clicks
        if (shopOpen) {
            // Check if clicked sell button
            if (sellButtonBounds.contains(p)) {
                sellSelectedFish();
                return;
            }

            // Check if clicked on fish list to select
            Rectangle fishArea = new Rectangle(60, 115, 500, 400);
            if (fishArea.contains(p)) {
                int clickedY = p.y;
                // Calculate which fish index based on y
                int index = (clickedY - 130) / 30;
                if (index >= 0 && index < inventory.getFishList().size()) {
                    selectedFishIndex = index;
                    repaint();
                }
                return;
            }
        }

        // Inventory toggle handled by key 'E', but allow clicking outside shop/inventory:
        if (inventoryOpen) {
            // Could add inventory item clicking later
            return;
        }

        // Normal fishing controls (only if no UI open)
        if (!fishingRodOut && !inventoryOpen && !shopOpen) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                fishingRodOut = true;
                fishingRodPullIn.storeImages();
                fishingRodPullIn.getAnimation().startTimer();
                fishingTime = true;
                fishing.fishingTimer.startTimer();
                System.out.println("Fishing rod released");
            }
        } else if (fishingRodOut && !inventoryOpen && !shopOpen) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (fishing.fishOnHook) {
                    System.out.println("Starting fishing minigame!");
                    startFishingMinigame();
                } else {
                    startRodPullBackAnimation();
                    System.out.println("Fishing rod pulled back");
                }
            }
        }
    }

    private void sellSelectedFish() {
        if (selectedFishIndex >= 0 && selectedFishIndex < inventory.getFishList().size()) {
            Fish fishToSell = inventory.getFishList().get(selectedFishIndex);
            double sellPrice = fishToSell.getSellPrice();
            inventory.removeFish(fishToSell);
            money += sellPrice;
            System.out.println("Sold " + fishToSell.getName() + " for $" + sellPrice);
            selectedFishIndex = -1;
            repaint();
        }
    }

    private void startFishingMinigame() {
        minigameActive = true;
        barY = getHeight() / 2 - barHeight / 2;
        fishY = getHeight() / 2 - fishSize / 2;
        progress = 50;
        pressingSpace = false;
        fishing.setFishingRodOut(false);
        minigameTimer.start();
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'e' || e.getKeyChar() == 'E') {
            if (!minigameActive && !showStartScreen && !showLoadingScreen) {
                inventoryOpen = !inventoryOpen;
                if (inventoryOpen) {
                    shopOpen = false;
                    selectedFishIndex = -1;
                }
                System.out.println("Inventory " + (inventoryOpen ? "opened" : "closed"));
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (showStartScreen && e.getKeyCode() == KeyEvent.VK_ENTER) {
            showStartScreen = false;
            return;
        }
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