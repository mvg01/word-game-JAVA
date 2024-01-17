import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScorePanel extends JPanel {
	private int score = 0;
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel nameLabel;
	private TextSource textSource = new TextSource(this);
	private List<Map.Entry<String, Integer>> sortedRank;
	private JLabel rankLabel = new JLabel();
	String userName;
	private ImageIcon icon = new ImageIcon("image/rank.jpg");
    private Image img = icon.getImage();

    @Override  //그래픽
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //배경에 꽉 차게
    }
	
	public ScorePanel(String name) {
		setLayout(null);
		setBackground(Color.PINK);
		this.userName=name;
		nameLabel=new JLabel(userName+"님 현재 점수: ");
		
		Font font1=new Font("한컴 바겐세일 B",Font.BOLD,20);
		Font font2=new Font("HY크리스탈M",Font.BOLD,16);
		
		nameLabel.setBounds(50,10,200,100);
		scoreLabel.setBounds(210,10,100,100);
		rankLabel.setBounds(70,80,200,200);
		
		scoreLabel.setFont(font1);
		nameLabel.setFont(font2);
		
		add(nameLabel);
		add(scoreLabel);
		
		rankLabel.setFont(font1);
		add(rankLabel);
	}
	
	public String userInfo() {  //userName return
		return userName;
	}
	
	public int scoreResult() {  //현재 점수 return
		return score;
	}
	
	public void scoreInit() {  //점수를 0점으로 초기화
		score=0;
	}
	
	public void increase(int plus) {  //plus값 만큼 점수 증가
		score += plus;
		scoreLabel.setText(Integer.toString(score));
	}
	
	public void restart() {  //다시 시작했을 경우, 점수 새로 그림
		scoreLabel.setText(Integer.toString(score));
		repaint();
	}
	
	public void updateHighScores(TextSource textSource) {  //최고점수판 다시 그림
		sortedRank=textSource.getSortedRank();
		displayRank();
    }
	
	private void displayRank() {
	    //현재까지의 정렬된 랭크 정보를 표시
	    StringBuilder rankText = new StringBuilder("<html>High Scores<br>");
	    for (int i = 0; i < Math.min(sortedRank.size(), 5); i++) {
	        Map.Entry<String, Integer> entry = sortedRank.get(i);
	        rankText.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br>");
	    }
	    rankText.append("</html>");
	    rankLabel.setText(rankText.toString());
	    revalidate();
	    repaint();
	}
}
