package flappybird_java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JFrame {
    private BackgroundPanel pnlGame = new BackgroundPanel();
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Timer pipeSpawnTimer;
    private TimerTask pipeSpawnTimerTask;

    private static Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private static int taskBarHeight = (int) (scrnSize.getHeight() - winSize.getHeight());

    // Components
    private Bird bird = new Bird();
    private ScoreText scoreText = new ScoreText();
    private StartScreen startScreen = new StartScreen();
    private GameOverScreen gameOverScreen = new GameOverScreen();
    private ResetButton resetButton = new ResetButton();

    // Variable
    private float sizeMultiply = 1.0f;
    private final int ORIGIN_SIZE = 512;
    private boolean flagGameOver = false;
    private boolean flagGameStart = false;
    private int jumpDelay = 0;
    private final int JUMP_DELAY = 10;

    public Frame() {
        // Initialize
        setTitle("Flappy Bird In Java");
        setSize(513, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(256, 256));
        setLayout(new CardLayout());

        // Game Screen
        pnlGame.setLayout(null);

        startScreen.setLocation(164, 123);
        startScreen.setSize(0,0);
        pnlGame.add(startScreen);

        gameOverScreen.setLocation(160,145);
        gameOverScreen.setSize(0,0);
        pnlGame.add(gameOverScreen);

        resetButton.setLocation(204,276);
        resetButton.setSize(0,0);
        pnlGame.add(resetButton);

        scoreText.setLocation(0, 0);
        scoreText.setSize(0, 0);
        pnlGame.add(scoreText);

        bird.setLocation(100, 224);
        bird.setSize(100, 100);
        pnlGame.add(bird);

        add(pnlGame, "Game");

        // Space Bar & Mouse left Click
        pnlGame.setFocusable(true);
        pnlGame.requestFocus();
        pnlGame.addKeyListener(new MyKeyAdapter());
        pnlGame.addMouseListener(new MyMouseListener());
        

        // Timer
        timerTask = new TimerTask() {
            @Override
            public void run() {
                pnlGame.update();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);

    } // Constructor

    public float getSizeMultiply() {
        return sizeMultiply;
    }

    public int getTaskBarHeight() {
        return taskBarHeight;
    }

    public Bird getBird() {
        return bird;
    }

    public void gameOver() {
        if (flagGameOver){
            return;
        }

        flagGameOver = true;
        pipeSpawnTimer.cancel();

        gameOverScreen.setVisible(true);
        resetButton.setVisible(true);
    }

    public boolean isGameOver() {
        return flagGameOver;
    }

    public void addScore() {
        scoreText.addScore(1);
    }

    public void startGame(){
        if(flagGameStart){
            return;
        }

        flagGameStart = true;
        flagGameOver = false;
        startScreen.setVisible(false);

        pipeSpawnTimer = new Timer();
        pipeSpawnTimerTask = new TimerTask() {

            @Override
            public void run() {
                int randY = (int) (Math.random() * 472);
                int clampY = Main.clamp(randY, PipeSpawner.GAP +
                        Pipe.MIN_HEIGHT, 472 - PipeSpawner.GAP - Pipe.MIN_HEIGHT);
                PipeSpawner.spawnPipe(pnlGame, clampY);
            }
        };
        pipeSpawnTimer.scheduleAtFixedRate(pipeSpawnTimerTask, 0, PipeSpawner.SPAWN_DELAY);
    }

    public void resetGame(){
        if (flagGameOver == false){
            return;
        }
        flagGameStart = false;

        pipeSpawnTimer.cancel();
        pipeSpawnTimer.purge(); // 타이머 자체를 없앤다.

        startScreen.setVisible(true);
        gameOverScreen.setVisible(false);
        resetButton.setVisible(false);
        scoreText.resetScore();

        bird.setLocation(100, 224);

        for ( Component k : pnlGame.getComponents()){ // Object < Component 가 최상위
            try{
                Pipe pipe = (Pipe)k; // 다운캐스팅
                pnlGame.remove(pipe);
            }catch(Exception e){
            }
        }
        // 지우고 다시 그리는 함수들 불러오기
        repaint();
        revalidate();
    }

    public void initGame(){ // 호환성
        pnlGame.update();
    }

    public boolean isGameStart(){ // Checker true & false
        return flagGameStart;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        if (width > height) {
            setSize(height, height);    
        } else {
            setSize(width, width);
        }
        sizeMultiply = (float) (getHeight() - taskBarHeight) / (float) (ORIGIN_SIZE - taskBarHeight);
    }

    // Listeners
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            startGame();
            bird.jump();
        }
    }
    public class MyKeyAdapter extends KeyAdapter {
        //#TODO: 스페이스바를 눌렀을 때 새가 점프하도록 구현
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                if (jumpDelay >= JUMP_DELAY){
                    bird.jump();
                    
                }
                    break;
            }       
        }
    }
} // Frame class