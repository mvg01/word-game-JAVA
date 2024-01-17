import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroPanel extends JPanel {
    private JComboBox<String> levelComboBox;
    private JButton startButton;
    private String selected;
    private ImageIcon icon = new ImageIcon("image/sddefault.jpg");
    private Image img = icon.getImage();
    private JButton gameGuideButton;  
    
    @Override  //그래픽
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //배경에 꽉 차게
        Font font = new Font("Mapo홍대프리덤", Font.BOLD, 24);
        Font font2 = new Font("한컴 말랑말랑 Bold", Font.BOLD, 60);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString("도전할 레벨을 선택하고 Press Start 버튼을 눌러주세요.", 140, 610);
        g.setFont(font2);
        g.setColor(Color.RED);
        g.drawString("운석을 막아라", 100, 220);
    }
    
    public IntroPanel() {
        setLayout(null); // 레이아웃 관리자를 제거
        setSize(900, 800);
        
        // 레벨을 나타낼 JComboBox 생성	
        levelComboBox = new JComboBox<>(new String[]{"초보", "중수", "고수", "개발자"});
        levelComboBox.setBounds(350,440, 200, 30);
        add(levelComboBox);
        
        startButton=new JButton();
        ImageIcon startImageIcon = new ImageIcon("image/start.jpg");  //게임 시작 버튼
        Image resizedImage = startImageIcon.getImage().getScaledInstance(300, 90, Image.SCALE_SMOOTH);
        startImageIcon = new ImageIcon(resizedImage);

        startButton.setIcon(startImageIcon);
        startButton.setBounds(300, 480, 300, 90);
        add(startButton);
        
        gameGuideButton = new JButton("게임 방법");  // "게임 방법" 버튼 생성
        gameGuideButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
        gameGuideButton.setBounds(350, 620, 200, 30);
        add(gameGuideButton);

        // 시작 버튼 클릭 이벤트 처리
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String) levelComboBox.getSelectedItem();              
                // 선택한 레벨을 사용하여 속도 등을 조절하거나 필요한 동작 수행
                // 여기에 게임 시작과 관련된 코드 추가
                System.out.println("선택한 레벨: " + selectedLevel);
                selected=selectedLevel;
                IntroPanel.this.setVisible(false);
            }
        });
        gameGuideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameGuide();  // 게임 방법을 표시하는 메서드 호출
            }
        });
    }
    
    public int level() {
    	switch (selected) {  //단어 나오는 주기에 대한 속도이다.
    	case "초보":
    		return 2300;
    	
    	case "중수":
    		return 2000;
    		
    	case "고수":
    		return 1800;
    		
    	case "개발자":
    		return 1600;
    		
    	default:
    		return 0;
    	}
    }
    
    private void showGameGuide() {
        JOptionPane.showMessageDialog(this,
                        "1. 나오는 단어를 빠르게 입력하여 운석을 제거하세요.\n" +
                        "2. 빨리 입력할수록 높은 점수를 획득합니다.\n" +
                        "3. 60초의 시간 내에 최대한 많은 단어를 제거하세요.\n" +
                        "4. UFO 단어를 입력하면 단어의 낙하 속도가 감소합니다. 꼭 단어를 입력하세요!\n" +
                        "5. 보급 박스 단어를 입력하면 하트가 1개 회복되고 시간이 5초 추가 됩니다. 꼭 단어를 입력하세요!\n" +
                        "6. 100점이 오를때마다 단어의 낙하속도가 증가합니다.\n" +
                        "7. 게임이 끝난 후, 원하는 단어를 추가로 저장할 수 있습니다.\n" +
                        "8. 생명력이 0이 되면 게임이 종료됩니다.\n" ,
                "게임 방법",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
