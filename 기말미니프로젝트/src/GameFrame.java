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
		setTitle("����");
		setSize(1200, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //�ݱ⸦ ���ؼ� �������α׷� �ڵ� ����
		makeIntro();  //ó�� �÷��̾� ���� ȭ��
		makeToolbar();  //�޴����� (�Ͻ�����, �ٽý���)
		gamePanel = new GamePanel(userName);
		getContentPane().add(gamePanel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	private void makeIntro() {
		Scanner sc=new Scanner(System.in);
		while(true) {
			userName=JOptionPane.showInputDialog(this, "player�� �̸��� �Է��ϼ���:");
			if(userName==null) 
				System.exit(0);
			else if (userName.trim().isEmpty())  //�ƹ��͵� �Է����� �ʾ��� ���
				JOptionPane.showMessageDialog(this,"�ٽ� �Է��ϼ���.");
			else
				break;
		}
	}
	
	public void makeToolbar() {
		JToolBar bar = new JToolBar();
		getContentPane().add(bar, BorderLayout.NORTH);
		Font font=new Font(getFont().getFamily(),Font.BOLD,20);
		JButton b = new JButton("�ٽ� ����");
		b.setFont(font);
		bar.add(b);
		b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.resumegame();
                System.out.println(gamePanel.isStopFlag());
            }
        });
		JButton playBtn = new JButton("�Ͻ�����");
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
	
	//�̹����� ũ�⸦ ������ ���ڷ� ������ش�.
	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}