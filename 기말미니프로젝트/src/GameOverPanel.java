import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOverPanel extends JPanel {
		private GameGround gameGround=null;
    	private ScorePanel scorePanel=null;
    	private TextSource textSource=null;
    	private JLabel rank=new JLabel();
    	private JLabel overText=new JLabel("게임이 종료되었습니다.");
    	private JLabel userName=null;
    	private JLabel score=new JLabel();
    	private JButton restart = null;
    	private ImageIcon icon = new ImageIcon("image/earth3.jpg");  
        private Image img = icon.getImage();
    	private int scoreNum;
    	private JTextField wordInput;
        private JButton addWordButton;
    	
    	@Override  //그래픽
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //배경에 꽉 차게
    	}
    	
    	public void changeRank(int finalScore) {  //랭크를 출력하고 점수를 표시한다.
    		scoreNum=finalScore;
    		score.setText("당신의 점수는: "+scoreNum);
    		
    		if(scoreNum<=150) {
    			rank.setText("Your Rank is D");
    		}
    		else if(scoreNum<=250) {
    			rank.setText("Your Rank is C");
    		}
    		else if(scoreNum<=400) {
    			rank.setText("Your Rank is B");
    		}
    		else if(scoreNum<=550) {
    			rank.setText("Your Rank is A");
    		}
    		else if(scoreNum<=700) {
    			rank.setText("Your Rank is A+");
    		}
    		else if(scoreNum<=850) {
    			rank.setText("Your Rank is S");
    		}
    		else {
    			rank.setText("Your Rank is SSS");
    		}
    		
    		this.textSource.getSortedRank();
    		scorePanel.updateHighScores(textSource); 
    		this.add(score); 
    		scoreNum=0;
    	}
    	
    	public GameOverPanel(GameGround gameGround, TextSource textSource, ScorePanel scorePanel) {
    		this.gameGround=gameGround;
    		this.textSource=textSource;
    		this.scorePanel=scorePanel;
   
    		scorePanel.updateHighScores(textSource);
    		userName = new JLabel(this.scorePanel.userInfo()+" 님");
    		setLayout(null);
    		this.gameGround.textInput.setFocusable(false);  //텍스트입력칸 포커스 지우기
    		setSize(900,800);
    		setBackground(Color.green);
    		Font font = new Font("한컴 말랑말랑 Bold", Font.BOLD, 32);
    		Font rfont = new Font("Bauhaus 93 보통", Font.BOLD, 40);
    		
    		overText.setForeground(Color.RED);
    		overText.setFont(font);
    		userName.setForeground(Color.green);
    		userName.setFont(font);
    		score.setForeground(Color.green);
    		score.setFont(font);
    		
    		rank.setFont(rfont);
    		rank.setForeground(new Color(255, 215, 0));
    	
    		overText.setBounds(350,300,300,40);
    		userName.setBounds(250,350,300,40);
    		score.setBounds(300,400,500,50);
    		rank.setBounds(330,100,500,200);
    		
    		this.add(overText);  //게임이 종료되었습니다.
    		this.add(userName);  //userName
    		this.add(score);     //당신의 점수는 x 점
    		this.add(rank);      //당신의 rank 
    		
    		restart=gameGround.startButton();
    		restart.setText("재시작");
    		restart.setSize(100,100);
    		restart.setLocation(600,600);
    		gameGround.textInput.setVisible(false);
    		this.add(restart);
    		
    		wordInput = new JTextField();
    	    wordInput.setBounds(50, 600, 180, 30);
    	    add(wordInput);

    	    addWordButton = new JButton("단어 추가");
    	    addWordButton.setBounds(240, 600, 100, 30);
    	    add(addWordButton);

    	    addWordButton.addActionListener(new ActionListener() { //버튼을 누르면 words.txt에 단어추가
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	           String newWord = wordInput.getText().trim();  //단어의 불필요한 공백제거
    	           if (!newWord.isEmpty()) {
    	                textSource.addWord(newWord);
    	                System.out.println("단어 추가: " + newWord);
    	                wordInput.setText("");
    	           }
    	        }
    	    });	
    	    setVisible(false);
    	}
    }