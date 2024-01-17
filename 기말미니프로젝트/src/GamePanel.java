import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class GamePanel extends JPanel {
	private ScorePanel scorePanel;
	private BoardPanel boardPanel = new BoardPanel();
	private boolean stopFlag=false;
	
	public boolean isStopFlag() {  //���� true�̸� ������ ��������.
	    return stopFlag;
	} 
	
	public void stopgame() {  //������ ���߰��Ѵ�.
		stopFlag=true;
	}
	synchronized public void resumegame() {
		stopFlag=false;
		notify();  //�� ��ü ������ ��� �����带 �ƿ��. �������� ���¸� �����Ų��.
	}
	
	public synchronized boolean isGameRunning() {
		return !stopFlag;
	}
	
	public synchronized void checkWait() {  
		if(stopFlag == true) {
			System.out.println("Ÿ�̸� ���� ���¿��� ��ٸ�");
			boardPanel.quitTimer();
			try {
				wait();  //�������� ���¸� ����
				System.out.println("Ÿ�̸� ����");
				boardPanel.startTimer();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
		
	public GamePanel(String name) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		scorePanel=new ScorePanel(name);
		setBackground(Color.YELLOW);
		setLayout(new BorderLayout());
		splitPanel();
	}
	
	private void splitPanel() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(900);
		add(hPane);
		
		JSplitPane vPane = new JSplitPane();
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		vPane.setDividerLocation(400);
		vPane.setTopComponent(boardPanel);
		vPane.setBottomComponent(scorePanel);
		hPane.setRightComponent(vPane);
		
		hPane.setLeftComponent(new GameGround(scorePanel,this,boardPanel));
	}
}