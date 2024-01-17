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
    private UFOPanel[] label1=new UFOPanel[500];    //UFO 패널
    private WordPanel[] label2=new WordPanel[500];  //운석이미지와 단어가 같이 묶여있는 패널
    private ItemPanel[] label3=new ItemPanel[500];  //아이템 상자 패널
    private int idx1 = 0;  //UFOPanel의 인덱스
    private int idx2 = 0;  //WordPanel의 인덱스  (
    private int idx3 = 0;  //ItemPanel의 인덱스
    
    public JTextField textInput = new JTextField(20);
    private TextSource textSource = null;
    private MyThread threads = null;
    private Moving moving = null;
    private ImageIcon icon = new ImageIcon("image/earth.jpg");  //현재 gameGround 배경
    private Image img = icon.getImage();
    JButton btnGameStart = null;
    private int speed = 0; //단어가 나오는 주기
    private int down = 18; //초기 단어 내려가는 속도
    private int scoreChk=100;  //이 점수가 넘어가면 단어 내려오는 속도가 빨라진다.
    private int rocketStartX=433;  //로켓 발사대 x좌표
    private int rocketStartY=610;  //로켓 발사대 Y좌표
    private int rocketEndX;
    private int rocketEndY;
    private boolean drawRocketLine;
    private Clip backGroundClip;  //배경음악
    private Clip boomClip;
    private Clip failClip;
    private AudioInputStream failClipAudioStream; 
    private File failClipAudioFile;
    private Clip endClip;
    
    @Override  //그래픽
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //배경에 꽉 차게
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
            int textX = (getWidth() - textWidth) / 2;  //사진위에 단어가 그려질 위치
            int textY = (getHeight() + textHeight) / 2;  //사진위에 단어가 그려질 위치
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
    
    class UFOPanel extends JPanel {  //ufo 이미지를 가지는 panel
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
    
    public JButton startButton() {  //게임을 시작 혹은, 재시작하는 버튼
    	setLayout(null);
        btnGameStart = new JButton("Press to Start!!");//시작 버튼
        btnGameStart.setFont(new Font("고딕", Font.BOLD, 18));
        btnGameStart.setBounds(700, 630, 200, 70);
        btnGameStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	backGroundClip.setFramePosition(0);  //시작 지점으로 이동
            	backGroundClip.start();
            	
            	down=18;  //초기 속도 설정
            	boardPanel.init(); //시간제한 60초로 설정, 체력 3 시작
            	boardPanel.heartSet(boardPanel.leftHeart());  //실시간 남은 체력 반영
            	scorePanel.scoreInit(); //0점으로 시작
            	scorePanel.restart();   //0점으로 보이게
            	boardPanel.startTimer(); //60초 남은시간 시작
            	overPanel.setVisible(false);  
                // 단어가 만들어져 있는지 확인
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
                speed=introPanel.level();  //인트로에서 받아온 레벨로 적용 (높은 난이도일수록 단어가 나오는 주기가 빨라짐)
                
                //스레드 시작
                threads = new MyThread(speed);
                threads.start();
                
                RocketPanel rocketPanel = new RocketPanel(); //배경의 로켓발사대 추가
                add(rocketPanel);
                rocketPanel.setLocation(400, 610);
                
                textInput.setFocusable(true);
                textInput.requestFocus(); //입력창에 포커스 주기
                System.out.println("게임 시작!!");  //콘솔 입력확인용
                
                btnGameStart.setVisible(false);
            }
        }); //시작버튼 이벤트
        return btnGameStart;
    }
    
    public GameGround(ScorePanel scorePanel, GamePanel gamePanel, BoardPanel boardPanel) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.scorePanel = scorePanel;
        this.gamePanel = gamePanel;
        this.boardPanel = boardPanel;
        textSource = new TextSource(this);
        this.overPanel = new GameOverPanel(this, textSource, scorePanel);
        setLayout(null);
        
        //배경 음악 재생
        backGroundClip = AudioSystem.getClip();
		File audioFile=new File("audio/background.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		backGroundClip.open(audioStream);
		
		//단어 적중 효과음
		boomClip = AudioSystem.getClip();
        File boomClipAudioFile = new File("audio/boom.wav");
        AudioInputStream boomClipAudioStream = AudioSystem.getAudioInputStream(boomClipAudioFile);
        boomClip.open(boomClipAudioStream);
        
        //단어 실패 효과음
        failClip = AudioSystem.getClip();
        failClipAudioFile = new File("audio/error.wav");
        failClipAudioStream = AudioSystem.getAudioInputStream(failClipAudioFile);
        failClip.open(failClipAudioStream);
        
        //게임종료 효과음
        endClip = AudioSystem.getClip();
        File endClipAudioFile = new File("audio/end.wav");
        AudioInputStream endClipAudioStream = AudioSystem.getAudioInputStream(endClipAudioFile);
        endClip.open(endClipAudioStream);
		
        introPanel = new IntroPanel();
        this.add(introPanel);
        scorePanel.updateHighScores(textSource);
        this.add(overPanel);
        Font customFont = new Font("Arial", Font.BOLD, 19);

        // 타자 입력하는 곳
        textInput.setSize(350, 40);
        textInput.setLocation(280, 670);
        textInput.setFont(customFont);
        add(textInput);
        
        textInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {   
            	boolean correct=false; //단어 맞추기 성공 여부
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {    
                    if (!textInput.getText().equals("")) {
                        int plus = 0;
                        int xPoint = 0, yPoint = 0;           
                        String text = textInput.getText(); // 현재 입력창의 단어
                        text=text.trim(); 
                        textInput.setText("");
                        for (int j = 0; j < idx1; j++) {
                            if (text.equals(label1[j].getWord()) && label1[j].isVisible()) {
                                yPoint = label1[j].getY();
                                xPoint = label1[j].getX();
                                label1[j].setVisible(false); // 단어 화면에서 안보이게 
                                if(down<=18)
                                	down+=2;
                                correct=true;  //단어를 맞췄다면
                                break;
                            }
                        }
                        for (int j = 0; j < idx2; j++) {
                            if (text.equals(label2[j].getWord()) && label2[j].isVisible()) {
                                yPoint = label2[j].getY();
                                xPoint = label2[j].getX();
                                label2[j].setVisible(false); // 단어 화면에서 안보이게  
                                correct=true;  //단어를 맞췄다면
                                break;
                            }
                        }
                        for (int j = 0; j < idx3; j++) {
                            if (text.equals(label3[j].getWord()) && label3[j].isVisible()) {
                                yPoint = label3[j].getY();
                                xPoint = label3[j].getX();
                                label3[j].setVisible(false); // 단어 화면에서 안보이게
                                boardPanel.plusHeart(); 
                                boardPanel.heartSet(boardPanel.leftHeart());  //실시간 남은 체력 반영
                                boardPanel.plusTimer();   //남은 시간 5초 추가  
                                correct=true;  //단어를 맞췄다면
                                break;
                            }
                        }
                        if(correct) {     
	                        // 빨리 입력할수록 높은 점수를 받는다!! y좌표 별 점수 획득 
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
	
	                            // 빨간색 선을 그리는 애니메이션 설정
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
	                            
	                            if(scorePanel.scoreResult()>=scoreChk) {  //100점이 늘어날때마다 속도는 계속 줄어든다.
	                            	if(down>=12) 
	                            		down-=3;
	                            	else
	                            		down--;
	                            	scoreChk+=100;
	                            }
	                            boomClip.setFramePosition(0);  //입력성공 사운드
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
        private int speed;   //난이도
        public MyThread(int speed) {
        	this.speed=speed;  //sleep의 주기가 난이도이다
        }

        @Override
        public void run() {
        	boardPanel.heartSet(boardPanel.leftHeart());  //실시간 남은 체력 반영
            for (int i = 0; i < 500; i++) {
            	gamePanel.checkWait();  //cpu를 소모하지 않고 wait 시켜줄 수 있다.
            	if (gamePanel.isGameRunning()) {  //게임이 진행중
                    gamePanel.resumegame();
                }
                try {
                	setLayout(null);     	
                	String word=new String();
                    if(speed>=2000) {  //초보와 중수모드에서 6단어 이상은 나오지 않는다.
	                    while(true) {
	                    	word = textSource.next();
	                    	if(word.length()<=5)
	                    		break;
	                    }
	                }
                    else if(speed>=1600) {  //나머지 모드에서도 8단어 이상은 나오지 않는다.
	                    while(true) {
	                    	word = textSource.next();
	                    	if(word.length()<=7)
	                    		break;
	                    }
	                }
                    else
                    	word=textSource.next();   
                    
                    int index = (int) (Math.random() * 770);  //랜덤한 X좌표의 위치
                    double point = Math.random();  //UFO,운석,상자 3가지 중 각각의 확률별로 랜덤하게 떨어진다
                    
                    if(point<0.15) {  //ufo 등장 확률 15%
                    	label1[idx1] = new UFOPanel(word); 
	                    label1[idx1].setSize(180, 180); 
	                    label1[idx1].setLocation(index, -100);  //처음 시작 위치
	                    
	                    add(label1[idx1]);
	                    moving = new Moving(label1[idx1]);  //각각의 label은 각각의 moving 스레드로 움직여진다.
	                    moving.start();
	        
	                    idx1++;
	                    sleep(speed);
                    }
                    else if (point<0.9) {  //운석 등장 확률 75%
	                    label2[idx2] = new WordPanel(word); 
	                    label2[idx2].setSize(180, 180); 
	                    label2[idx2].setLocation(index, -100); 
	                    
	                    add(label2[idx2]);
	                    moving = new Moving(label2[idx2]);
	                    moving.start();
	        
	                    idx2++;
	                    sleep(speed);
                    } else {  //보급상자 등장 확률 10%
                    	 label3[idx3] = new ItemPanel(word);
                         label3[idx3].setSize(180, 180);
                         label3[idx3].setLocation(index, -100);

                         add(label3[idx3]);
                         moving = new Moving(label3[idx3]);
                         moving.start();

                         idx3++;
                         sleep(speed);
                    }
                
                    if(boardPanel.leftTime()<=0) {  //남은시간이 0초가 되어서 종료될 때
 	                    //종료 패널 출력	
                        textSource.updateRank(scorePanel.userName,scorePanel.scoreResult());  //rank.txt에 새로이 추가
                        endClip.setFramePosition(0);  //시작 지점으로 이동
                        endClip.start();
                        int left=boardPanel.leftHeart();  //단어 실패 사운드가 울리지 않게하기 위해
                        for(int j=0; j<left+1; j++) 
                        	boardPanel.lossHeart();
                        	
                        backGroundClip.stop();  //배경음악 종료
                         System.out.println("타임오버");
                         boardPanel.quitTimer(); //타이머 종료
                         textSource.rankCheck(); //rank.txt 다시 확인
                         textSource.printSortedRank();  //정렬된 랭크 콘솔에 출력
                         overPanel.changeRank(scorePanel.scoreResult()); //랭크를 출력한다 (D~SSS)
                         scorePanel.updateHighScores(textSource);  //새롭게 바뀐 랭크판 업데이트
                         overPanel.setVisible(true);
 	                    return;
                    } 

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        // 스레드 종료 메서드
        public void stopThread() {
            interrupt();
        }
    }

    class Moving extends Thread {  //ufo,운석,상자 panel들을 각각 움직이게하는 스레드
    	private JPanel label; 
    	
        public Moving(JPanel label) {
            this.label = label;
        }

        @Override
        public void run() {
            while (label.isVisible() && label.getY() < 520) {
            	
            	gamePanel.checkWait();  //cpu를 소모하지 않고 wait 시켜줄 수 있다.
            	if (gamePanel.isGameRunning()) {
                    gamePanel.resumegame();                 
                }
            	
                if (label.isVisible()) {  //단어가 아직 입력되지 않았음 여부 -> isVisible()
                    int dx = label.getX();
                    int dy = label.getY();
                    label.setLocation(dx, dy + 1);    	
                }
                else { //단어 입력된 상태
                	break;
                }
                if (label.isVisible() && label.getY() >= 510&&boardPanel.leftHeart() >= 0) {  //빨간 선을 넘어가는 순간 
                    boardPanel.lossHeart(); 
                    boardPanel.heartSet(boardPanel.leftHeart());  //실시간 남은 체력 반영
                    failClip.setFramePosition(0);  //시작 지점으로 이동
                    failClip.start(); //fail audio 재생
                    
                    if(boardPanel.leftHeart()==0) {  //hp가 0이 되었을 때
                    	textSource.updateRank(scorePanel.userName,scorePanel.scoreResult());  //rank.txt에 새로이 추가
                    	endClip.setFramePosition(0);  //시작 지점으로 이동
                    	endClip.start();  //게임오버 사운드
                    	boardPanel.lossHeart(); //단어 실패사운드가 울리는 현상 방지
                    	backGroundClip.stop();  //배경음악 종료
                    	System.out.println("게임오버");
                    	boardPanel.quitTimer(); //타이머 종료
                    	textSource.rankCheck(); //rank.txt 다시 확인
                    	textSource.printSortedRank();  //정렬된 랭크 콘솔에 출력
                    	overPanel.changeRank(scorePanel.scoreResult()); //랭크를 출력한다 (D~SSS)
                    	scorePanel.updateHighScores(textSource);  //새롭게 바뀐 랭크판 업데이트
                    	overPanel.setVisible(true);  //overPanel 활성화
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

class ExplosionPanel extends JPanel {  //폭발 이미지를 가지는 panel
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

class RocketPanel extends JPanel {  //로켓발사대 이미지를 가지는 panels
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

