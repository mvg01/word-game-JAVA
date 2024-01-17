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
    private int heart = 3; // 남은 체력
    private int leftTime = 60; // 초 단위로 남은 시간
    private JLabel timeLabel;
    private JLabel heartLabel1=null;
    private JLabel heartLabel2=null;
    private JLabel heartLabel3=null;
    private Timer timer=null;

    @Override  //그래픽
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = new Font("한컴 말랑말랑 Bold", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.green);
        g.drawString("", 50, 300);
    }
    
    public BoardPanel() {
    	setLayout(null); // 레이아웃 관리자를 제거
        setBackground(Color.gray);
        Font customFont = new Font("Arial", Font.BOLD, 19);

        // 남은 시간을 표시할 라벨 생성
        timeLabel = new JLabel("Left Time: " + leftTime);
        timeLabel.setFont(customFont);

        // 타이머를 사용하여 남은 시간을 감소시키는 기능 추가
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftTime--;
                timeLabel.setText("Left Time: " + leftTime);
                timeLabel.setFont(customFont);
                // 시간이 0이 되면 타이머 중지
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
    
    public void lossHeart() {  //하트 한개 감소
    	this.heart--;
    }
    public void plusHeart() {  //하트 한개 증가 단 3개 초과하지 않는다.
    	if(this.heart<3)
    		this.heart++;
    }
    public int leftHeart() {  //남은 하트 return
    	return this.heart;
    }
    public void init() {  //남은시간과 하트를 초기값으로 설정
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
    public int leftTime() {  //남은 시간 리턴, 시간이 0이되면 종료된다.
    	return leftTime;
    }
}
