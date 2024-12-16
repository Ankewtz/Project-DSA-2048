import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends Canvas implements Runnable {

    public static final int WIDTH = 400, HEIGHT = 400;
    public static float scale = 2.0f;

    public JFrame frame;
    public Thread thread;
    public Keyboard key;
    public Game game;
    public boolean running = false;
    public volatile boolean autoplay = false;
    public int[][] prevBoard = new int[4][4];

    public static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private GreedyAI greedyAI = new GreedyAI();
    private RandomAI randomAI = new RandomAI();

    public Main() {
        setPreferredSize(new Dimension((int) (WIDTH * scale), (int) (HEIGHT * scale)));
        frame = new JFrame();
        game = new Game();
        key = new Keyboard();
        addKeyListener(key);

        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        

        // Create buttons for AI selection and autoplay control
        JButton greedyButton = new JButton("Play Greedy AI");
        greedyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoplay = true;
                new Thread(() -> {
                    while (autoplay) {
                        playGreedyAI();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        JButton randomButton = new JButton("Play Random AI");
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoplay = true;
                new Thread(() -> {
                    while (autoplay) {
                        playRandomAI();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoplay = false;
            }
        });

        JPanel panel = new JPanel();
        panel.add(greedyButton);
        panel.add(randomButton);
        panel.add(stopButton);
        frame.add(panel, BorderLayout.NORTH);

        frame.setResizable(false);
        frame.setTitle("2048");
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
    }

    public void start() {
        running = true;
        thread = new Thread(this, "loopThread");
        thread.start();
    }

    public void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTimeInNanoSeconds = System.nanoTime();
        long timer = System.currentTimeMillis();
        double nanoSecondsPerUpdate = 1000000000.0 / 60.0;
        double updatesToPerform = 0.0;
        int frames = 0;
        int updates = 0;
        updateBoard();

        requestFocus();
        while (running) {
            long currentTimeInNanoSeconds = System.nanoTime();
            updatesToPerform += (currentTimeInNanoSeconds - lastTimeInNanoSeconds) / nanoSecondsPerUpdate;
            if (updatesToPerform >= 1) {
                update();
                updates++;
                updatesToPerform--;
            }
            lastTimeInNanoSeconds = currentTimeInNanoSeconds;

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                frame.setTitle("2048 " + updates + " updates, " + frames + " frames");
                updates = 0;
                frames = 0;
                timer += 1000;
            }

            boolean boardChanged = hasBoardChanged();
            if (boardChanged) {
                updateBoard();
                game.printBoard();
            }
        }
    }

    private void playGreedyAI() {
        int move = greedyAI.getNextMove(game.getBoard());
        game.executeMove(move);
    }

    private void playRandomAI() {
        int move = randomAI.getNextMove();
        game.executeMove(move);
    }

    private boolean hasBoardChanged() {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.getBoard()[i][j] != prevBoard[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateBoard() {
        for (int i = 0; i < prevBoard.length; i++) {
            for (int j = 0; j < prevBoard[0].length; j++) {
                prevBoard[i][j] = game.getBoard()[i][j];
            }
        }
    }

    public void update() {
        game.update();
        key.update();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        game.render();

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.drawImage(image, 0, 0, (int) (WIDTH * scale), (int) (HEIGHT * scale), null);
        game.renderText(g);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.frame.setResizable(false);
        m.frame.setTitle("2048");
        m.frame.add(m);
        m.frame.pack();
        m.frame.setVisible(true);
        m.frame.setLocationRelativeTo(null);
        m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.frame.setAlwaysOnTop(true);
        m.start();
    }
}
