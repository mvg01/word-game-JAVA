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
	
	public boolean isStopFlag() {  //현재 true이면 게임은 멈춰진다.
	    return stopFlag;
	} 
	
	public void stopgame() {  //게임을 멈추게한다.
		stopFlag=true;
	}
	synchronized public void resumegame() {
		stopFlag=false;
		notify();  //이 객체 떄문에 잠든 스레드를 꺠운다. 스레드의 상태를 변경시킨다.
	}
	
	public synchronized boolean isGameRunning() {
		return !stopFlag;
	}
	
	public synchronized void checkWait() {  
		if(stopFlag == true) {
			System.out.println("타이머 중지 상태에서 기다림");
			boardPanel.quitTimer();
			try {
				wait();  //스레드의 상태를 변경
				System.out.println("타이머 시작");
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