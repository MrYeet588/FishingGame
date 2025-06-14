import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class fishingTimer implements ActionListener {
    private Timer timer;
    private int num;

    public fishingTimer(){
        num = 0;
        timer = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        num++;
    }
    public int getNum(){
        return num;
    }

    public void startTimer(){
        num = 0;
        timer.restart();
        timer.start();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }
}
