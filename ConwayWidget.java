import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class ConwayWidget extends JPanel implements Runnable, MouseListener {

    private Lock pauseLock = new ReentrantLock();

    private final int TILE_SIZE = 16;
    private final int GRID_SIZE = 32;
    private final int GRID_OVERFLOW_VAL = GRID_SIZE - 1;

    private static final boolean[] rulesetAlive = { false, false, true, true, false, false, false, false, false };
    private static final boolean[] rulesetDead = { false, false, false, true, false, false, false, false, false };

    private long sleepDuration = 100;

    private volatile boolean paused;

    private boolean[][] map, oldMap;

    public ConwayWidget() {
        map = new boolean[GRID_SIZE][GRID_SIZE];
        oldMap = new boolean[GRID_SIZE][GRID_SIZE];

        int d = TILE_SIZE * GRID_SIZE;
        setPreferredSize(new Dimension(d, d));
    }

    @Override
    public void run() {
        // Glider!
        map[0][1] = true;
        map[1][2] = true;
        map[2][0] = true;
        map[2][1] = true;
        map[2][2] = true;

        // start listening for mouse input
        addMouseListener(this);
        while (true) {
            pauseLock.lock();
            generateNext();
            repaint();
            pauseLock.unlock();
            sleep();
        }
    }

    public void toggle() {
        if (paused) {
            pauseLock.unlock();
            paused = false;
        } else {
            pauseLock.lock();
            paused = true;
        }
    }

    private int getNeighorCount(int row, int col) {
        int count = 0;
        int down = (row + 1) % GRID_SIZE;
        int up = (row + GRID_OVERFLOW_VAL) % GRID_SIZE;
        int right = (col + 1) % GRID_SIZE;
        int left = (col + GRID_OVERFLOW_VAL) % GRID_SIZE;
        // Ugly as SIN... we can do better
        if (oldMap[up][left]) {
            count++;
        }
        if (oldMap[up][col]) {
            count++;
        }
        if (oldMap[up][right]) {
            count++;
        }
        if (oldMap[row][right]) {
            count++;
        }
        if (oldMap[down][right]) {
            count++;
        }
        if (oldMap[down][col]) {
            count++;
        }
        if (oldMap[down][left]) {
            count++;
        }
        if (oldMap[row][left]) {
            count++;
        }
        return count;
    }

    private void generateNext() {
        boolean[][] tmp = oldMap;
        oldMap = map;
        map = tmp;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int neighborCount = getNeighorCount(row, col);
                if (oldMap[row][col]) {
                    map[row][col] = rulesetAlive[neighborCount];
                } else {
                    map[row][col] = rulesetDead[neighborCount];
                }
            }
        }
    }

    private static final Color BACKGROUND_COLOR = new Color(32, 32, 32);
    private static final Color ALIVE_COLOR = new Color(255, 0, 98);
    @Override
    public void paint(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        final int width = getWidth();
        final int height = getHeight();
        g.fillRect(0, 0, width, height);

        g.setColor(ALIVE_COLOR);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (map[row][col]) {
                    g.fillRect(col << 4, row << 4, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {}
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (!paused) {
            toggle();
        }
        int x = e.getX();
        int y = e.getY();
        int row = y >> 4;
        int col = x >> 4;
        map[row][col] = !map[row][col];
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
