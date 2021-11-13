package projects.randomwalker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel {
    private ArrayList<Walker> walkers = new ArrayList<>();

    public Canvas() {
        this.setBackground(Color.WHITE);
    }

    public void addWalker(Walker w) {
        walkers.add(w);
    }

    public Walker getWalker(int i) {
        return walkers.get(i);
    }

    public void clear() {
        walkers.clear();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        for(Walker w : walkers) {
            g2d.fillRect(w.getWalkerX(), w.getWalkerY(), w.getWalkerWidth(), w.getWalkerHeight());
        }
    }
}
