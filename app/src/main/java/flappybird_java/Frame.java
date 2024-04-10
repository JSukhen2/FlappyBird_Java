package flappybird_java;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public Frame(){

    
    setTitle("Flappy Bird");
    setSize(600,600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    setMinimumSize( new Dimension(300 , 300)); // 최소 창 크기 제한 걸어주기
    }
}
