import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class GameFrame extends JFrame {
	private GamePanel gamePanel = null;
	private String userName;
	
	public GameFrame() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		setTitle("게임");
		setSize(1200, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //닫기를 통해서 응용프로그램 자동 종료
		makeIntro();  //처음 플레이어 시작 화면
		makeToolbar();  //메뉴툴바 (일시정지, 다시시작)
		gamePanel = new GamePanel(userName);
		getContentPane().add(gamePanel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	private void makeIntro() {
		Scanner sc=new Scanner(System.in);
		while(true) {
			userName=JOptionPane.showInputDialog(this, "player의 이름을 입력하세요:");
			if(userName==null) 
				System.exit(0);
			else if (userName.trim().isEmpty())  //아무것도 입력하지 않았을 경우
				JOptionPane.showMessageDialog(this,"다시 입력하세요.");
			else
				break;
		}
	}
	
	public void makeToolbar() {
		JToolBar bar = new JToolBar();
		getContentPane().add(bar, BorderLayout.NORTH);
		Font font=new Font(getFont().getFamily(),Font.BOLD,20);
		JButton b = new JButton("다시 시작");
		b.setFont(font);
		bar.add(b);
		b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.resumegame();
                System.out.println(gamePanel.isStopFlag());
            }
        });
		JButton playBtn = new JButton("일시정지");
		playBtn.setFont(font);
		bar.add(playBtn);
		
		playBtn.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				gamePanel.stopgame();
				System.out.println(gamePanel.isStopFlag());
	        }
		});
		
		bar.setFloatable(false);
	}
	
	//이미지의 크기를 설정한 숫자로 만들어준다.
	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}