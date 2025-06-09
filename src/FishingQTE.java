public class FishingQTE {
    public fishingTimer fishingTimer;
    public int fishingTimerTime;
    public boolean fishOnHook;
    private DisplayPanel panel;

    public FishingQTE(DisplayPanel panel){
        fishingTimer = new fishingTimer();
        fishOnHook = false;
        this.panel = panel;
        fishingTimer.startTimer();
    }


    public void fishOnHookAlert(){
        fishingTimer.startTimer();
        fishingTimerTime = fishingTimer.getNum();
        int temp2 = (int) ((Math.random() * 4) + 5);
        if (fishingTimerTime >= temp2 - 5 && fishingTimerTime <= temp2 + 5) {
            fishOnHook = true;
        } else {
            fishOnHook = false;
        }
    }
}
