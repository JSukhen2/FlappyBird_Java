package flappybird_java;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.stream.Stream;

public abstract class GameObject extends JLabel {
    private Image image;
    private int imageWidth, imageHeight;
    protected int x;
    protected int y;

    public GameObject(Image image) {
        super();
        this.image = image;
        setOpaque(false);

        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }

    public void update() {
        float sizeMultiply = Main.getSizeMultiply();
        setSize( (int)(imageWidth * sizeMultiply), (int)(imageHeight * sizeMultiply) );
    }

    //5주차
    public int getImageWidth() {
        return image.getWidth(null);
    }

    public int getImageHeight() {
        return image.getHeight(null);
    }

    public void setImage(Image image){
        this.image = image;
        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
        repaint();
    }
    //6주차

    public boolean isCollided(GameObject object) {
        Rectangle rectThis = new Rectangle( getX(), getY(), getWidth(), getHeight() );
        Rectangle rectObject = new Rectangle( object.getX(), object.getY(), object.getWidth(), object.getHeight() );

        return rectThis.intersects(rectObject);

    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        int fixedX = (int)( x * Main.getSizeMultiply() );
        int fixedY = (int)( y * Main.getSizeMultiply() );
        super.setLocation(fixedX, fixedY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
} //GameObject class

class BackgroundPanel extends JPanel {
    private Image imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ).getImage();
    private final int WIDTH = imgBackground.getWidth(null);
    private final int HEIGHT = imgBackground.getHeight(null);

    public void update() {
        for ( Component k : getComponents() ) {
            GameObject obj = (GameObject)k;
            obj.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        float sizeMultiply = Main.getFrame().getSizeMultiply();
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        g.drawImage(imgBackground, 0, 0, fixedWidth, fixedHeight, this);
    }
} //BackgroundPanel class


class Bird extends GameObject {
    private final static Image image = new ImageIcon( Main.getPath("/sprites/bird_midflap.png") ).getImage();

    private float jump = 0f;
    private final float GRAVITY = 3f;
    private final float G_FORCE = 0.5f;

    public Bird() {
        super(image);
    }

    @Override
    public void update() {
        super.update();

        if ( jump > -GRAVITY) {
            jump -= G_FORCE;
        }
        else{
            jump = -GRAVITY;
        }

        y = Main.clamp( (int)(y - jump), 0, 472 - image.getHeight(null) );
        setLocation(x, y);
    }

    public void jump() {
        if ( Main.getFrame().isgameOver() == false) {
            jump = 10;
        }
    }
} //Bird class

class Pipe extends GameObject {
    private int speed = 2;
    public static final int MIN_HEIGHT = 50;
    private Bird bird;
    public final String TAG;

    public Pipe(Image image, String tag) {
        super(image);
        bird = Main.getFrame().getBird();
        TAG = tag;
    }

    @Override
    public void update() {
        super.update();
        //Move

        if (Main.getFrame().isgameOver() == false){
            x -= speed;
        }
        setLocation(x, y);

        //Remove
        if (x <= -50) {
            getParent().remove(this);
        }

        //Collision 함수로 만들면 좋음
        // || -> OR
        if ( Main.getFrame().isgameOver() || getX() + getWidth() < bird.getX() ){
            return;
        }
        if ( isCollided(bird) ){
            switch(TAG){
                case "Pipe":
                    Main.getFrame().gameOver();
                    break;
                case "ScoreAdder":
                    //점수 추가
                    getParent().remove(this);
                    Main.getFrame().addScore();
                    break;

            }
        }
    }
} //Pipe class

class PipeDown extends Pipe {
    private final static Image image = new ImageIcon( Main.getPath("/sprites/pipe_down.png") ).getImage();

    public PipeDown() {
        super(image, "Pipe");
    }

    //5주차
    @Override
    public void setLocation(int x, int y) {
        int clampY = Main.clamp(y, -image.getHeight(null) + Pipe.MIN_HEIGHT, 0);
        super.setLocation(x, clampY);
    }
    //
}

class PipeUp extends Pipe {
    private final static Image image = new ImageIcon( Main.getPath("/sprites/pipe_up.png") ).getImage();

    public PipeUp() {
        super(image, "Pipe");
    }

    //5주차
    @Override
    public void setLocation(int x, int y) {
        int clampY = Main.clamp(y, 472 - image.getHeight(null), 472 - Pipe.MIN_HEIGHT);
        super.setLocation(x, clampY);
    }
    //
}

//5주차
class PipeSpawner {
    public static final int SPAWN_DELAY = 1500;
    public static final int GAP = 100;

    public static void spawnPipe(BackgroundPanel root, int y) {
        if (Main.getFrame().isgameOver()){
            return;
        }
        PipeUp pipeUp = new PipeUp();
        PipeDown pipeDown = new PipeDown();
        ScoreAdder scoreAdder = new ScoreAdder();

        pipeUp.setLocation(600, y + GAP);
        pipeDown.setLocation( 600, y - GAP - pipeDown.getImageHeight() );
        scoreAdder.setLocation(642, y - scoreAdder.getImageHeight()/2); // 창의 끝(600) + 파이프 넓이(52) - 투명창 넓이(10)

        root.add(pipeUp);
        root.add(pipeDown);
        root.add(scoreAdder);
    }
}

class ScoreAdder extends Pipe{

    public ScoreAdder() {
        super(getImage(), "ScoreAdder");
    }

    public static Image getImage() {
        // width = 10
        // height = frame.getHeight * 2 = 512 * 2 = 1024
        // x, y = spawner
        BufferedImage buffImage = new BufferedImage(10, 1024, BufferedImage.TYPE_4BYTE_ABGR);
        return new ImageIcon(buffImage).getImage();
    }

}

class ScoreText extends GameObject {
    private static final Image[] aryImage = {
            new ImageIcon(Main.getPath("/sprites/0.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/1.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/2.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/3.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/4.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/5.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/6.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/7.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/8.png")).getImage(),
            new ImageIcon(Main.getPath("/sprites/9.png")).getImage()
    };

    private class Margin { // Struct 유사 구조체
        public static final int X = 3;
        public static final int Y = 12;
    }

    private int score = 0;
    private Image image;

    public ScoreText() {
        super(aryImage[0]);
        updateImage();
    }

    public void addScore(int score) {
        try{
            this.score += score;
            updateImage();
        }
        catch (Exception e){

        }
    }

    public void updateImage() {
        int[] parsedScore = Stream.of( String.valueOf(score).split("") ).mapToInt(Integer::parseInt).toArray();
        int height = aryImage[0].getHeight(null) + Margin.Y;
        BufferedImage newImage = new BufferedImage(512,height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = newImage.createGraphics();
        
        int offset = 0;
        for (int k : parsedScore) {
            offset += aryImage[k].getWidth(null) + Margin.X;
        }

        int x = 256 - offset / 2;
        for ( int k : parsedScore) {
            Image imgNumber = aryImage[k];
            graphics.drawImage(imgNumber, x, Margin.Y, null);
            x += imgNumber.getWidth(null) + Margin.X;
        }
        graphics.dispose();

        setImage( new ImageIcon(newImage).getImage() );
    }
}