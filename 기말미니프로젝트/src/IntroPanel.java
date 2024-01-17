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
    
    @Override  //�׷���
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null); //��濡 �� ����
        Font font = new Font("Mapoȫ��������", Font.BOLD, 24);
        Font font2 = new Font("���� �������� Bold", Font.BOLD, 60);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString("������ ������ �����ϰ� Press Start ��ư�� �����ּ���.", 140, 610);
        g.setFont(font2);
        g.setColor(Color.RED);
        g.drawString("��� ���ƶ�", 100, 220);
    }
    
    public IntroPanel() {
        setLayout(null); // ���̾ƿ� �����ڸ� ����
        setSize(900, 800);
        
        // ������ ��Ÿ�� JComboBox ����	
        levelComboBox = new JComboBox<>(new String[]{"�ʺ�", "�߼�", "���", "������"});
        levelComboBox.setBounds(350,440, 200, 30);
        add(levelComboBox);
        
        startButton=new JButton();
        ImageIcon startImageIcon = new ImageIcon("image/start.jpg");  //���� ���� ��ư
        Image resizedImage = startImageIcon.getImage().getScaledInstance(300, 90, Image.SCALE_SMOOTH);
        startImageIcon = new ImageIcon(resizedImage);

        startButton.setIcon(startImageIcon);
        startButton.setBounds(300, 480, 300, 90);
        add(startButton);
        
        gameGuideButton = new JButton("���� ���");  // "���� ���" ��ư ����
        gameGuideButton.setFont(new Font("���Ļ�浸��", Font.BOLD, 18));
        gameGuideButton.setBounds(350, 620, 200, 30);
        add(gameGuideButton);

        // ���� ��ư Ŭ�� �̺�Ʈ ó��
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String) levelComboBox.getSelectedItem();              
                // ������ ������ ����Ͽ� �ӵ� ���� �����ϰų� �ʿ��� ���� ����
                // ���⿡ ���� ���۰� ���õ� �ڵ� �߰�
                System.out.println("������ ����: " + selectedLevel);
                selected=selectedLevel;
                IntroPanel.this.setVisible(false);
            }
        });
        gameGuideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameGuide();  // ���� ����� ǥ���ϴ� �޼��� ȣ��
            }
        });
    }
    
    public int level() {
    	switch (selected) {  //�ܾ� ������ �ֱ⿡ ���� �ӵ��̴�.
    	case "�ʺ�":
    		return 2300;
    	
    	case "�߼�":
    		return 2000;
    		
    	case "���":
    		return 1800;
    		
    	case "������":
    		return 1600;
    		
    	default:
    		return 0;
    	}
    }
    
    private void showGameGuide() {
        JOptionPane.showMessageDialog(this,
                        "1. ������ �ܾ ������ �Է��Ͽ� ��� �����ϼ���.\n" +
                        "2. ���� �Է��Ҽ��� ���� ������ ȹ���մϴ�.\n" +
                        "3. 60���� �ð� ���� �ִ��� ���� �ܾ �����ϼ���.\n" +
                        "4. UFO �ܾ �Է��ϸ� �ܾ��� ���� �ӵ��� �����մϴ�. �� �ܾ �Է��ϼ���!\n" +
                        "5. ���� �ڽ� �ܾ �Է��ϸ� ��Ʈ�� 1�� ȸ���ǰ� �ð��� 5�� �߰� �˴ϴ�. �� �ܾ �Է��ϼ���!\n" +
                        "6. 100���� ���������� �ܾ��� ���ϼӵ��� �����մϴ�.\n" +
                        "7. ������ ���� ��, ���ϴ� �ܾ �߰��� ������ �� �ֽ��ϴ�.\n" +
                        "8. ������� 0�� �Ǹ� ������ ����˴ϴ�.\n" ,
                "���� ���",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
