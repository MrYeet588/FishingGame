import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private ArrayList<Fish> caughtFish;

    public Inventory() {
        caughtFish = new ArrayList<>();
    }

    public void addFish(Fish fish) {
        caughtFish.add(fish);
    }

    public List<Fish> getCaughtFish() {
        return caughtFish;
    }

    // New method to get a copy of the fish list (for external use)
    public List<Fish> getFishList() {
        return new ArrayList<>(caughtFish);
    }

    // Remove a specific fish from the inventory
    public boolean removeFish(Fish fish) {
        return caughtFish.remove(fish);
    }

    public int getCount() {
        return caughtFish.size();
    }

    // Returns list of fish info strings (for easier drawing line-by-line)
    public List<String> getFishInfoList() {
        ArrayList<String> infoList = new ArrayList<>();
        if (caughtFish.isEmpty()) {
            infoList.add("Inventory is empty.");
            return infoList;
        }
        for (int i = 0; i < caughtFish.size(); i++) {
            infoList.add((i + 1) + ". " + caughtFish.get(i).fishInfo());
        }
        return infoList;
    }

    public void clear() {
        caughtFish.clear();
    }
}