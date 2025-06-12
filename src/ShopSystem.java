import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ShopSystem {
    private boolean shopOpen = false;
    private Map<String, Integer> shopItems;
    private Rectangle shopBounds = new Rectangle(500, 50, 300, 400);

    public ShopSystem() {
        shopItems = new HashMap<>();
        shopItems.put("Bait", 10);
        shopItems.put("Better Rod", 100);
        shopItems.put("Luck Potion", 50);
    }

    public void drawShop(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(shopBounds.x, shopBounds.y, shopBounds.width, shopBounds.height);

        g.setColor(Color.WHITE);
        g.drawRect(shopBounds.x, shopBounds.y, shopBounds.width, shopBounds.height);
        g.drawString("Shop", shopBounds.x + 120, shopBounds.y + 30);

        int y = shopBounds.y + 60;
        for (Map.Entry<String, Integer> entry : shopItems.entrySet()) {
            g.drawString(entry.getKey() + " - $" + entry.getValue(), shopBounds.x + 20, y);
            y += 30;
        }
    }

    public void toggleShop() {
        shopOpen = !shopOpen;
    }

    public boolean isShopOpen() {
        return shopOpen;
    }

    public boolean clickInShop(MouseEvent e) {
        return shopOpen && shopBounds.contains(e.getPoint());
    }

    public void buyItem(String itemName, Inventory inventory) {
        // Placeholder for adding item to inventory or effecting gameplay
        System.out.println("Bought " + itemName);
    }
}
