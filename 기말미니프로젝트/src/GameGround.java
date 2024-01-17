import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;

public class GameGround extends JPanel {
	private IntroPanel introPanel = null;
	private GameOverPanel overPanel = null;
    private ScorePanel scorePanel = null;
    private BoardPanel boardPanel = null;
    private GamePanel gamePanel = null;
    private UFOPanel[] label1=new UFOPanel[500];    //UFO �г�
    private WordPanel[] label2=new WordPanel[500];  //��̹����� �ܾ ���� �����ִ� �г�
    private ItemPanel[] label3=new ItemPanel[500];  //������ ���� �г�
    private int idx1 = 0;  //UFOPanel�� �ε���
    private int idx2 = 0;  //WordPanel�� �ε���  (
    private int idx3 = 0;  //ItemPanel�� �ε���
    
    public JTextField textInput = new JTextField(20);
    private TextSource textSource = null;
    private MyThread threads = null;
    private Moving moving = null;
    private ImageIcon icon = new ImageIcon("image/earth.jpg");  //���� gameGround ���
    private Image img = icon.getImage();
    JButton btnGameStart = null;
    private int speed = 0; //�ܾ ������ �ֱ�
    private int down = 18; //�ʱ� �ܾ� �������� �ӵ�
    private int scoreChk=100;  //�� ������ �Ѿ�� �ܾ� �������� �ӵ��� ��������.
    private int rocketStartX=433;  //���� �߻�� x��ǥ
    private int rocketStartY=610;  //���� �߻�� Y��ǥ
    private int rocketEndX;
    private int rocketEndY;
    private boolean drawRocketLine;
    private Clip backGroundClip;  //�������
    private Clip boomClip;
    private Clip failClip;
    private AudioInputStream failClipAudioStream; 
    private File failClipAudioFile;
    private Clip endClip;
    
    @Override  //�׷���
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //��濡 �� ����
        g.setColor(Color.RED);
        g.drawLine(0, 610, this.getWidth(), 610);
        g.drawLine(0, 613, this.getWidth(), 613);
        if (drawRocketLine) {
            g.setColor(Color.RED);
            g.drawLine(rocketStartX, rocketStartY, rocketEndX, rocketEndY);
        }
    }
   
    class WordPanel extends JPanel {
        private String word;
        private Image meteorImage;
        private int meteorWidth = 70; 
        private int meteorHeight = 70; 
        
        public WordPanel(String word) {
            this.word = word;
            setOpaque(false); 
            ImageIcon meteorIcon = new ImageIcon("image/meteor2.png");
            meteorImage = meteorIcon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
            int x = (getWidth() - meteorWidth) / 2;
            int y = (getHeight() - meteorHeight) / 2;
            g.drawImage(meteorImage, x, y, meteorWidth, meteorHeight, this);
            g.setColor(Color.RED);
            Font font = new Font("Arial", Font.BOLD, 24);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(word);
            int textHeight = fm.getHeight();
            int textX = (getWidth() - textWidth) / 2;  //�������� �ܾ �׷��� ��ġ
            int textY = (getHeight() + textHeight) / 2;  //�������� �ܾ �׷��� ��ġ
            g.drawString(word, textX, textY);
        }
        public String getWord() {
            return word;
        }
    }
    
    class ItemPanel extends JPanel {  
    	private String word;
        private Image boxImage;
        private int boxWidth = 70; 
        private int boxHeight = 70; 
        
        public ItemPanel(String word) {
            this.word = word;
            setOpaque(false); 

            ImageIcon boxIcon = new ImageIcon("image/box.png");
            boxImage = boxIcon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);

            int x = (getWidth() - boxWidth) / 2;
            int y = (getHeight() - boxHeight) / 2;
            g.drawImage(boxImage, x, y, boxWidth, boxHeight, this);
            g.setColor(Color.green);
            Font font = new Font("Arial", Font.BOLD, 24);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(word);
            int textHeight = fm.getHeight();
            int textX = (getWidth() - textWidth) / 2;
            int textY = (getHeight() + textHeight) / 2;
            g.drawString(word, textX, textY-7);
        }
       
        public String getWord() {
            return word;
        }
    }
    
    class UFOPanel extends JPanel {  //ufo �̹����� ������ panel
    	private String word;
        private Image ufoImage;
        private int ufoWidth = 70; 
        private int ufoHeight =70; 
        
        public UFOPanel(String word) {
            this.word = word;
            setOpaque(false); 

            ImageIcon ufoIcon = new ImageIcon("image/ufo2.png");
            ufoImage = ufoIcon.getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);

            int x = (getWidth() - ufoWidth) / 2;
            int y = (getHeight() - ufoHeight) / 2;
            g.drawImage(ufoImage, x, y, ufoWidth, ufoHeight, this);
            g.setColor(Color.green);
            Font font = new Font("Arial", Font.BOLD, 24);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(word);
            int textHeight = fm.getHeight();
            int textX = (getWidth() - textWidth) / 2; 
            int textY = (getHeight() + textHeight) / 2;
            g.drawString(word, textX, textY);
        }
       
        public String getWord() {
            return word;
        }
    }
    
    public JButton startButton() {  //������ ���� Ȥ��, ������ϴ� ��ư
    	setLayout(null);
        btnGameStart = new JButton("Press to Start!!");//���� ��ư
        btnGameStart.setFont(new Font("���", Font.BOLD, 18));
        btnGameStart.setBounds(700, 630, 200, 70);
        btnGameStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	backGroundClip.setFramePosition(0);  //���� �������� �̵�
            	backGroundClip.start();
            	
            	down=18;  //�ʱ� �ӵ� ����
            	boardPanel.init(); //�ð����� 60�ʷ� ����, ü�� 3 ����
            	boardPanel.heartSet(boardPanel.leftHeart());  //�ǽð� ���� ü�� �ݿ�
            	scorePanel.scoreInit(); //0������ ����
            	scorePanel.restart();   //0������ ���̰�
            	boardPanel.startTimer(); //60�� �����ð� ����
            	overPanel.setVisible(false);  
                // �ܾ ������� �ִ��� Ȯ��
                if (label2[0] != null) {
                    threads.stopThread();
                    //moving.stopThread();
                    for (int j = 0; j <= idx1; j++) {
                        if (label1[j] != null) {
                            label1[j].setVisible(false);
                        }
                    }
                    idx1 = 0;
                    for (int j = 0; j <= idx2; j++) {
                        if (label2[j] != null) {
                            label2[j].setVisible(false);
                        }
                    }
                    idx2 = 0;
                    for (int j = 0; j <= idx3; j++) {
                        if (label3[j] != null) {
                            label3[j].setVisible(false);
                        }
                    }
                    idx3 = 0;
                }
                textInput.setVisible(true);
                speed=introPanel.level();  //��Ʈ�ο��� �޾ƿ� ������ ���� (���� ���̵��ϼ��� �ܾ ������ �ֱⰡ ������)
                
                //������ ����
                threads = new MyThread(speed);
                threads.start();
                
                RocketPanel rocketPanel = new RocketPanel(); //����� ���Ϲ߻�� �߰�
                add(rocketPanel);
                rocketPanel.setLocation(400, 610);
                
                textInput.setFocusable(true);
                textInput.requestFocus(); //�Է�â�� ��Ŀ�� �ֱ�
                System.out.println("���� ����!!");  //�ܼ� �Է�Ȯ�ο�
                
                btnGameStart.setVisible(false);
            }
        }); //���۹�ư �̺�Ʈ
        return btnGameStart;
    }
    
    public GameGround(ScorePanel scorePanel, GamePanel gamePanel, BoardPanel boardPanel) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.scorePanel = scorePanel;
        this.gamePanel = gamePanel;
        this.boardPanel = boardPanel;
        textSource = new TextSource(this);
        this.overPanel = new GameOverPanel(this, textSource, scorePanel);
        setLayout(null);
        
        //��� ���� ���
        backGroundClip = AudioSystem.getClip();
		File audioFile=new File("audio/background.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		backGroundClip.open(audioStream);
		
		//�ܾ� ���� ȿ����
		boomClip = AudioSystem.getClip();
        File boomClipAudioFile = new File("audio/boom.wav");
        AudioInputStream boomClipAudioStream = AudioSystem.getAudioInputStream(boomClipAudioFile);
        boomClip.open(boomClipAudioStream);
        
        //�ܾ� ���� ȿ����
        failClip = AudioSystem.getClip();
        failClipAudioFile = new File("audio/error.wav");
        failClipAudioStream = AudioSystem.getAudioInputStream(failClipAudioFile);
        failClip.open(failClipAudioStream);
        
        //�������� ȿ����
        endClip = AudioSystem.getClip();
        File endClipAudioFile = new File("audio/end.wav");
        AudioInputStream endClipAudioStream = AudioSystem.getAudioInputStream(endClipAudioFile);
        endClip.open(endClipAudioStream);
		
        introPanel = new IntroPanel();
        this.add(introPanel);
        scorePanel.updateHighScores(textSource);
        this.add(overPanel);
        Font customFont = new Font("Arial", Font.BOLD, 19);

        // Ÿ�� �Է��ϴ� ��
        textInput.setSize(350, 40);
        textInput.setLocation(280, 670);
        textInput.setFont(customFont);
        add(textInput);
        
        textInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {   
            	boolean correct=false; //�ܾ� ���߱� ���� ����
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {    
                    if (!textInput.getText().equals("")) {
                        int plus = 0;
                        int xPoint = 0, yPoint = 0;           
                        String text = textInput.getText(); // ���� �Է�â�� �ܾ�
                        text=text.trim(); 
                        textInput.setText("");
                        for (int j = 0; j < idx1; j++) {
                            if (text.equals(label1[j].getWord()) && label1[j].isVisible()) {
                                yPoint = label1[j].getY();
                                xPoint = label1[j].getX();
                                label1[j].setVisible(false); // �ܾ� ȭ�鿡�� �Ⱥ��̰� 
                                if(down<=18)
                                	down+=2;
                                correct=true;  //�ܾ ����ٸ�
                                break;
                            }
                        }
                        for (int j = 0; j < idx2; j++) {
                            if (text.equals(label2[j].getWord()) && label2[j].isVisible()) {
                                yPoint = label2[j].getY();
                                xPoint = label2[j].getX();
                                label2[j].setVisible(false); // �ܾ� ȭ�鿡�� �Ⱥ��̰�  
                                correct=true;  //�ܾ ����ٸ�
                                break;
                            }
                        }
                        for (int j = 0; j < idx3; j++) {
                            if (text.equals(label3[j].getWord()) && label3[j].isVisible()) {
                                yPoint = label3[j].getY();
                                xPoint = label3[j].getX();
                                label3[j].setVisible(false); // �ܾ� ȭ�鿡�� �Ⱥ��̰�
                                boardPanel.plusHeart(); 
                                boardPanel.heartSet(boardPanel.leftHeart());  //�ǽð� ���� ü�� �ݿ�
                                boardPanel.plusTimer();   //���� �ð� 5�� �߰�  
                                correct=true;  //�ܾ ����ٸ�
                                break;
                            }
                        }
                        if(correct) {     
	                        // ���� �Է��Ҽ��� ���� ������ �޴´�!! y��ǥ �� ���� ȹ�� 
	                            if (yPoint <= 150) {
	                                plus += 37;
	                            } else if (yPoint <= 250) {
	                                plus += 31;
	                            } else if (yPoint <= 350) {
	                                plus += 25;
	                            } else if (yPoint <= 450) {
	                                plus += 17;
	                            } else if (yPoint <= 550) {
	                                plus += 13;
	                            }  
	                            
	                            rocketEndX = xPoint + 80;
	                            rocketEndY = yPoint + 90;
	                            drawRocketLine = true;
	
	                            // ������ ���� �׸��� �ִϸ��̼� ����
	                            Timer lineTimer = new Timer(12, new ActionListener() {
	                                int steps = 0;
	                                @Override
	                                public void actionPerformed(ActionEvent e) {
	                                    steps++;
	                                    repaint();
	
	                                    if (steps > 12) {
	                                        drawRocketLine = false;
	                                        ((Timer) e.getSource()).stop();
	                                    }
	                                }
	                            });
	
	                            lineTimer.start();
	                            ExplosionPanel explosionPanel = new ExplosionPanel();
	                            add(explosionPanel);
	                            explosionPanel.setLocation(xPoint + 50, yPoint + 50);
	                            explosionPanel.setVisible(true);
	                            Timer timer = new Timer(300, new ActionListener() {
	                                @Override
	                                public void actionPerformed(ActionEvent e) {
	                                    explosionPanel.setVisible(false);
	                                }
	                            });
	                            timer.setRepeats(false);
	                            timer.start();

	                            scorePanel.increase(plus);
	                            
	                            if(scorePanel.scoreResult()>=scoreChk) {  //100���� �þ������ �ӵ��� ��� �پ���.
	                            	if(down>=12) 
	                            		down-=3;
	                            	else
	                            		down--;
	                            	scoreChk+=100;
	                            }
	                            boomClip.setFramePosition(0);  //�Է¼��� ����
	                        	boomClip.start();  
	                        }
	                    }
                    }
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        add(startButton());
    }
    
    class MyThread extends Thread { 
        private int speed;   //���̵�
        public MyThread(int speed) {
        	this.speed=speed;  //sleep�� �ֱⰡ ���̵��̴�
        }

        @Override
        public void run() {
        	boardPanel.heartSet(boardPanel.leftHeart());  //�ǽð� ���� ü�� �ݿ�
            for (int i = 0; i < 500; i++) {
            	gamePanel.checkWait();  //cpu�� �Ҹ����� �ʰ� wait ������ �� �ִ�.
            	if (gamePanel.isGameRunning()) {  //������ ������
                    gamePanel.resumegame();
                }
                try {
                	setLayout(null);     	
                	String word=new String();
                    if(speed>=2000) {  //�ʺ��� �߼���忡�� 6�ܾ� �̻��� ������ �ʴ´�.
	                    while(true) {
	                    	word = textSource.next();
	                    	if(word.length()<=5)
	                    		break;
	                    }
	                }
                    else if(speed>=1600) {  //������ ��忡���� 8�ܾ� �̻��� ������ �ʴ´�.
	                    while(true) {
	                    	word = textSource.next();
	                    	if(word.length()<=7)
	                    		break;
	                    }
	                }
                    else
                    	word=textSource.next();   
                    
                    int index = (int) (Math.random() * 770);  //������ X��ǥ�� ��ġ
                    double point = Math.random();  //UFO,�,���� 3���� �� ������ Ȯ������ �����ϰ� ��������
                    
                    if(point<0.15) {  //ufo ���� Ȯ�� 15%
                    	label1[idx1] = new UFOPanel(word); 
	                    label1[idx1].setSize(180, 180); 
	                    label1[idx1].setLocation(index, -100);  //ó�� ���� ��ġ
	                    
	                    add(label1[idx1]);
	                    moving = new Moving(label1[idx1]);  //������ label�� ������ moving ������� ����������.
	                    moving.start();
	        
	                    idx1++;
	                    sleep(speed);
                    }
                    else if (point<0.9) {  //� ���� Ȯ�� 75%
	                    label2[idx2] = new WordPanel(word); 
	                    label2[idx2].setSize(180, 180); 
	                    label2[idx2].setLocation(index, -100); 
	                    
	                    add(label2[idx2]);
	                    moving = new Moving(label2[idx2]);
	                    moving.start();
	        
	                    idx2++;
	                    sleep(speed);
                    } else {  //���޻��� ���� Ȯ�� 10%
                    	 label3[idx3] = new ItemPanel(word);
                         label3[idx3].setSize(180, 180);
                         label3[idx3].setLocation(index, -100);

                         add(label3[idx3]);
                         moving = new Moving(label3[idx3]);
                         moving.start();

                         idx3++;
                         sleep(speed);
                    }
                
                    if(boardPanel.leftTime()<=0) {  //�����ð��� 0�ʰ� �Ǿ ����� ��
 	                    //���� �г� ���	
                        textSource.updateRank(scorePanel.userName,scorePanel.scoreResult());  //rank.txt�� ������ �߰�
                        endClip.setFramePosition(0);  //���� �������� �̵�
                        endClip.start();
                        int left=boardPanel.leftHeart();  //�ܾ� ���� ���尡 �︮�� �ʰ��ϱ� ����
                        for(int j=0; j<left+1; j++) 
                        	boardPanel.lossHeart();
                        	
                        backGroundClip.stop();  //������� ����
                         System.out.println("Ÿ�ӿ���");
                         boardPanel.quitTimer(); //Ÿ�̸� ����
                         textSource.rankCheck(); //rank.txt �ٽ� Ȯ��
                         textSource.printSortedRank();  //���ĵ� ��ũ �ֿܼ� ���
                         overPanel.changeRank(scorePanel.scoreResult()); //��ũ�� ����Ѵ� (D~SSS)
                         scorePanel.updateHighScores(textSource);  //���Ӱ� �ٲ� ��ũ�� ������Ʈ
                         overPanel.setVisible(true);
 	                    return;
                    } 

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        // ������ ���� �޼���
        public void stopThread() {
            interrupt();
        }
    }

    class Moving extends Thread {  //ufo,�,���� panel���� ���� �����̰��ϴ� ������
    	private JPanel label; 
    	
        public Moving(JPanel label) {
            this.label = label;
        }

        @Override
        public void run() {
            while (label.isVisible() && label.getY() < 520) {
            	
            	gamePanel.checkWait();  //cpu�� �Ҹ����� �ʰ� wait ������ �� �ִ�.
            	if (gamePanel.isGameRunning()) {
                    gamePanel.resumegame();                 
                }
            	
                if (label.isVisible()) {  //�ܾ ���� �Էµ��� �ʾ��� ���� -> isVisible()
                    int dx = label.getX();
                    int dy = label.getY();
                    label.setLocation(dx, dy + 1);    	
                }
                else { //�ܾ� �Էµ� ����
                	break;
                }
                if (label.isVisible() && label.getY() >= 510&&boardPanel.leftHeart() >= 0) {  //���� ���� �Ѿ�� ���� 
                    boardPanel.lossHeart(); 
                    boardPanel.heartSet(boardPanel.leftHeart());  //�ǽð� ���� ü�� �ݿ�
                    failClip.setFramePosition(0);  //���� �������� �̵�
                    failClip.start(); //fail audio ���
                    
                    if(boardPanel.leftHeart()==0) {  //hp�� 0�� �Ǿ��� ��
                    	textSource.updateRank(scorePanel.userName,scorePanel.scoreResult());  //rank.txt�� ������ �߰�
                    	endClip.setFramePosition(0);  //���� �������� �̵�
                    	endClip.start();  //���ӿ��� ����
                    	boardPanel.lossHeart(); //�ܾ� ���л��尡 �︮�� ���� ����
                    	backGroundClip.stop();  //������� ����
                    	System.out.println("���ӿ���");
                    	boardPanel.quitTimer(); //Ÿ�̸� ����
                    	textSource.rankCheck(); //rank.txt �ٽ� Ȯ��
                    	textSource.printSortedRank();  //���ĵ� ��ũ �ֿܼ� ���
                    	overPanel.changeRank(scorePanel.scoreResult()); //��ũ�� ����Ѵ� (D~SSS)
                    	scorePanel.updateHighScores(textSource);  //���Ӱ� �ٲ� ��ũ�� ������Ʈ
                    	overPanel.setVisible(true);  //overPanel Ȱ��ȭ
                    	return;
                    }
                    label.setVisible(false);
                    break;
                }
                try {
                	sleep(down);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                if (isInterrupted()) {
                    return;
                }
            }
        }
    }
}

class ExplosionPanel extends JPanel {  //���� �̹����� ������ panel
    private Image explosionImage;
    public ExplosionPanel() {
        setOpaque(false);
        ImageIcon explosionIcon = new ImageIcon("image/expl.png");
        explosionImage = explosionIcon.getImage();
        setSize(50, 50);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(explosionImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class RocketPanel extends JPanel {  //���Ϲ߻�� �̹����� ������ panels
    private Image rocketImage;
    public RocketPanel() {
        setOpaque(false);
        ImageIcon rIcon = new ImageIcon("image/rke.png");
        rocketImage = rIcon.getImage();
        setSize(50, 50);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rocketImage, 0, 0, getWidth(), getHeight(), this);
    }
}

