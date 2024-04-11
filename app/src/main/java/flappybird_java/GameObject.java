package flappybird_java;

import javax.swing.*;
import java.awt.event.*;
import com.google.common.graph.Graph;

import java.awt.*;

public abstract class GameObject extends JPanel { // abstract  -> 추상
    // 캐릭터, 파이프 Update 가 필요한 내용
    abstract void Update();
}

class BackGroundPanel extends JPanel {
    /* # 무엇을 만들 것 인가 ? (목표)
     * # 우리가 무엇을 가져올 수 있는가 ? (입력)
     * # 가공을 하여 어떤 것을 줄 것인가 ? (출력)
     */
    //ImageIcon imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ); // 메모리 낭비중
    private Image imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ).getImage();
    private final Integer WIDTH = imgBackground.getWidth(this);
    private final Integer HEIGHT = imgBackground.getHeight(this);
    private Bird bird = new Bird();
    
    public BackGroundPanel(){
        setLayout(null);

        bird.setLocation(100, 200);
        bird.setSize( bird.getWidth(),bird.getHEIGHT() );
        add(bird);

        addMouseListener( new MyMouseListener() );
    }

    public void update(){
        bird.Update();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Frame frame = Main.getFrame();

        float sizeMultiply = frame.getSizeMultiply();
        int fixedWidth = (int)(WIDTH * sizeMultiply); // 수정된 내용
        int fixedHeight = (int)(HEIGHT * sizeMultiply);
        g.drawImage(imgBackground, 0 , 0 ,fixedWidth, fixedHeight, this);

        for (int i = 0; i<frame.getWidth() / fixedWidth + 1; i++){
            g.drawImage(imgBackground, i * fixedWidth, 0, fixedWidth, fixedHeight, this);
        }
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e){
            bird.setJumpPoWER(-10);
        }
    }
}
// 개체 - Bird
class Bird extends GameObject{
    private Image imgBird = new ImageIcon( Main.getPath("/sprites/bird_midflap.png") ).getImage();
    private final Integer WIDTH = imgBird.getWidth(this);
    private final Integer HEIGHT = imgBird.getHeight(this);
    private int jumpPower = -1;
    private final int MAX_JUMP_POWER = 2;
    private float cooltime = 0; // 마우스 클릭
    private int y = getY();

    public void Update(){
        y = Main.Cilmp(y + jumpPower, getHeight(), Main.getFrame().getBackGroundPanel().getHeight() );
        setLocation( 100, y - getHeight() );

        if (jumpPower < MAX_JUMP_POWER){
            jumpPower += 1;
        }
    }

    public int getWidth(){
        return WIDTH;
    }
    public int getHEIGHT(){
        return HEIGHT;
    }
    public void setJumpPoWER(int jumpPower){
        this.jumpPower = jumpPower;
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);
        g.drawImage(imgBird, 0 , 0 ,fixedWidth, fixedHeight, this);
        setSize(fixedWidth,fixedHeight);
    }
}
// 개체 - Pipe
class Pile extends GameObject{
    private Image imgPipe = new ImageIcon( Main.getPath("/sprites/bird_midflap.png") ).getImage();
    private final Integer WIDTH = imgPipe.getWidth(this);
    private final Integer HEIGHT = imgPipe.getHeight(this);

    public void Update(){
        
    }
}