import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardPanel extends JPanel {
    private int heart = 3; // ���� ü��
    private int leftTime = 60; // �� ������ ���� �ð�
    private JLabel timeLabel;
    private JLabel heartLabel1=null;
    private JLabel heartLabel2=null;
    private JLabel heartLabel3=null;
    private Timer timer=null;

    @Override  //�׷���
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = new Font("���� �������� Bold", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.green);
        g.drawString("", 50, 300);
    }
    
    public BoardPanel() {
    	setLayout(null); // ���̾ƿ� �����ڸ� ����
        setBackground(Color.gray);
        Font customFont = new Font("Arial", Font.BOLD, 19);

        // ���� �ð��� ǥ���� �� ����
        timeLabel = new JLabel("Left Time: " + leftTime);
        timeLabel.setFont(customFont);

        // Ÿ�̸Ӹ� ����Ͽ� ���� �ð��� ���ҽ�Ű�� ��� �߰�
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftTime--;
                timeLabel.setText("Left Time: " + leftTime);
                timeLabel.setFont(customFont);
                // �ð��� 0�� �Ǹ� Ÿ�̸� ����
                if (leftTime == 0) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        
        timeLabel.setSize(200,40);
        timeLabel.setLocation(70,20);
        add(timeLabel);
   
        heartLabel1 = new JLabel();
        heartLabel2 = new JLabel();
        heartLabel3 = new JLabel();
        
        heartLabel1.setSize(100,100);
        heartLabel2.setSize(100,100);
        heartLabel3.setSize(100,100);
        
        heartLabel1.setLocation(0,100);
        heartLabel2.setLocation(90,100);
        heartLabel3.setLocation(180,100);
        
    }
    
    public void lossHeart() {  //��Ʈ �Ѱ� ����
    	this.heart--;
    }
    public void plusHeart() {  //��Ʈ �Ѱ� ���� �� 3�� �ʰ����� �ʴ´�.
    	if(this.heart<3)
    		this.heart++;
    }
    public int leftHeart() {  //���� ��Ʈ return
    	return this.heart;
    }
    public void init() {  //�����ð��� ��Ʈ�� �ʱⰪ���� ����
    	leftTime=61;
    	heart=3;
    }
    
    public void heartSet(int heart) {
    	ImageIcon emptyHeart = new ImageIcon("image/heart1.png");
        ImageIcon fullHeart = new ImageIcon("image/heart2.PNG");
    	switch (heart) {
        case 3:
        	heartLabel1.setIcon(fullHeart);
            heartLabel2.setIcon(fullHeart);
            heartLabel3.setIcon(fullHeart);
            repaint();
        	break;
        case 2:
        	heartLabel1.setIcon(fullHeart);
            heartLabel2.setIcon(fullHeart);
            heartLabel3.setIcon(emptyHeart);
            repaint();
        	break;
        case 1:
        	heartLabel1.setIcon(fullHeart);
            heartLabel2.setIcon(emptyHeart);
            heartLabel3.setIcon(emptyHeart);
            repaint();
        	break;
        case 0:
        	heartLabel1.setIcon(emptyHeart);
            heartLabel2.setIcon(emptyHeart);
            heartLabel3.setIcon(emptyHeart);
            repaint();
        	break;
        }
        add(heartLabel1);
        add(heartLabel2);
        add(heartLabel3);
    }
    
    public void quitTimer() {
    	timer.stop();
    }
    public void startTimer() {
    	timer.start();
    }
    public void plusTimer() {
    	this.leftTime+=5;
    }
    public int leftTime() {  //���� �ð� ����, �ð��� 0�̵Ǹ� ����ȴ�.
    	return leftTime;
    }
}
