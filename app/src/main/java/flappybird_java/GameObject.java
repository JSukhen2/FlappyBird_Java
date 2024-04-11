package flappybird_java;

import javax.swing.*;
import java.awt.*;

public class GameObject {
    
}

class BackGroundPanel extends JPanel {
    /* # 무엇을 만들 것 인가 ? (목표)
     * # 우리가 무엇을 가져올 수 있는가 ? (입력)
     * # 가공을 하여 어떤 것을 줄 것인가 ? (출력)
     */
    //ImageIcon imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ); // 메모리 낭비중
    Image imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ).getImage();
    private final Integer WIDTH = imgBackground.getWidth(this);
    private final Integer HEIGHT = imgBackground.getHeight(this);
    
    public BackGroundPanel(){
        setLayout(null);
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
}