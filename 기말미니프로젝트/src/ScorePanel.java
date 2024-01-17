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

    @Override  //�׷���
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //��濡 �� ����
    }
	
	public ScorePanel(String name) {
		setLayout(null);
		setBackground(Color.PINK);
		this.userName=name;
		nameLabel=new JLabel(userName+"�� ���� ����: ");
		
		Font font1=new Font("���� �ٰռ��� B",Font.BOLD,20);
		Font font2=new Font("HYũ����ŻM",Font.BOLD,16);
		
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
	
	public int scoreResult() {  //���� ���� return
		return score;
	}
	
	public void scoreInit() {  //������ 0������ �ʱ�ȭ
		score=0;
	}
	
	public void increase(int plus) {  //plus�� ��ŭ ���� ����
		score += plus;
		scoreLabel.setText(Integer.toString(score));
	}
	
	public void restart() {  //�ٽ� �������� ���, ���� ���� �׸�
		scoreLabel.setText(Integer.toString(score));
		repaint();
	}
	
	public void updateHighScores(TextSource textSource) {  //�ְ������� �ٽ� �׸�
		sortedRank=textSource.getSortedRank();
		displayRank();
    }
	
	private void displayRank() {
	    //��������� ���ĵ� ��ũ ������ ǥ��
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
