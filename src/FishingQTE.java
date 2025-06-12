public class FishingQTE {
    public fishingTimer fishingTimer;
    public int fishingTimerTime;
    public boolean fishOnHook;
    public boolean caughtMessageOn;  // controls caught message display
    private final int caughtMessageDuration = 2; // seconds to show message
    private int caughtMessageCounter;  // counts seconds elapsed for caught message
    private DisplayPanel panel;

    private boolean fishingRodOut;

    public FishingQTE(DisplayPanel panel){
        this.panel = panel;
        fishingTimer = new fishingTimer();
        fishOnHook = false;
        caughtMessageOn = false;
        caughtMessageCounter = 0;
        fishingTimer.startTimer();
    }

    public void update() {
        fishingTimerTime = fishingTimer.getNum();

        if (!fishingRodOut) {
            // Rod is pulled back, reset everything
            fishOnHook = false;
            fishingTimer.startTimer();  // restart timer for next fishing attempt
            return;
        }

        // Rod is out
        if (!fishOnHook) {
            // Fish hasn't bitten yet, check if it should bite
            int biteTime = (int) (Math.random() * 11) + 10; // 5-10 seconds
            if (fishingTimerTime >= biteTime) {
                fishOnHook = true;
                // Stop timer or let it run if you want to show countdown etc.
                // You can stop timer if you want: fishingTimer.stopTimer();
            }
        }

        // Manage caught message timer if showing message
        if (caughtMessageOn) {
            caughtMessageCounter++;
            if (caughtMessageCounter >= caughtMessageDuration) {
                caughtMessageOn = false;
                caughtMessageCounter = 0;
            }
        }
    }

    public void caughtFish() {
        // Call this when fish is caught (rod pulled in with fish)
        fishOnHook = false;
        caughtMessageOn = true;
        caughtMessageCounter = 0;
        fishingTimer.startTimer();  // reset timer for next fish cycle
    }

    public void setFishingRodOut(boolean out) {
        this.fishingRodOut = out;
    }
}