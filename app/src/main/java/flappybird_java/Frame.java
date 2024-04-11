package flappybird_java;

import javax.swing.*;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame {
    private BackGroundPanel pnlBackground1 = new BackGroundPanel();
    private BackGroundPanel pnlBackground2 = new BackGroundPanel();
    private BackGroundPanel[] pnlarrBackground = {pnlBackground1, pnlBackground2};
    private Timer timer = new Timer();
    private TimerTask timerTask;
    // BackGroundPanel[] pnlarrBackground = new BackGroundPanel[2];
    
    //Variable
    private float sizeMultiply = 1.0f; //크기 배율
    private final int ORIGIN_SIZE = 512;

    public Frame(){ // Unity - OnCreate (Start 함수와 같음)

    // #창에 대한 기본 세팅
    setTitle("Flappy Bird");
    setSize(512,512);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // #창에 대한 제약
    setMinimumSize( new Dimension(256 , 256)); // 최소 창 크기 제한 걸어주기
    add(pnlBackground1);

    timerTask = new TimerTask() {
        @Override
        public void run(){
            pnlBackground1.update();
        }
    };
    timer.scheduleAtFixedRate(timerTask, 0, 10);
    }


    public float getSizeMultiply(){ // SizeMultiply 값을 다른 곳으로 보내는 함수
        return sizeMultiply;
    }

    public BackGroundPanel getBackGroundPanel(){ //get
        return pnlBackground1;
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        if (width > height){
            setSize(height,height);
        }
        else {
            setSize(width,width);
        }
        sizeMultiply = (float)getWidth() / (float)ORIGIN_SIZE;
    }

    /*private class FrameSizeListener extends ComponentAdapter{
        @Override // 성능면에서 좋다
        public void componentResized(ComponentEvent e) {
            int width = getWidth();
            int height = getHeight();

            //  *** width와 height 중 작은 값을 뽑고, setSize() 함수에 w , h 변수에 값을 넣는다.

            if (width > height){
                setSize(height,height);
            }
            else {
                setSize(width,width);
            }

            sizeMultiply = (float)getWidth() / (float)ORIGIN_SIZE;

            int fixedWidth = (int)(288 * sizeMultiply); // 수정된 내용
            int fixedHeight = (int)(512 * sizeMultiply);

            pnlBackground1.setSize(fixedWidth,fixedHeight);

            System.out.println(sizeMultiply);

            repaint(); // 화면 크기 변경시 검정색으로 변하는 것을 덜 하게 해줌
        }
    }*/
}
