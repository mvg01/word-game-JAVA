import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TextSource {
	
	private Vector<String> wordVector = new Vector<String>(26000);
	private HashMap<String, Integer> rankMap = new HashMap<>();
	private Scanner scanner;
	private int size;
	public TextSource(Component parent) {
		try {
			scanner=new Scanner(new FileReader("words.txt"));
			while(scanner.hasNext()) {
				String word = scanner.nextLine().toLowerCase();
				word = removeSpecialCharacters(word); //Ư����ȣ ���� ����ǥ����
				wordVector.add(word);
			}
			//scanner.close();
			//JOptionPane.showMessageDialog(parent, "�ܾ� �б� �Ϸ�");
		} catch (FileNotFoundException e) {
			System.out.println("���� �����.");
			System.exit(0);
		}
		
		try {
			scanner=new Scanner(new FileReader("rank.txt"));
			while(scanner.hasNext()) {
				String name = scanner.next();
				int score = scanner.nextInt();
				rankMap.put(name, score);
			}
			
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
		
		this.size = wordVector.size();
	}
	
	private String removeSpecialCharacters(String word) {
        //Ư�� ��ȣ ���� ���� ǥ����
        word = word.replaceAll("[^a-zA-Z0-9]", "");
        return word;
    }
	
	public String next() {  //���� �ܾ �����ϰ� ���
		int index=(int)(Math.random() * size);
		return wordVector.get(index);  //������ �ε����� �ܾ �������ش�.
	}
	
	public int VectorSize() {
		return size;
	}
	public void addWord(String wor) {
        // ���ο� �ܾ wordVector�� �߰�
        wordVector.add(wor.toLowerCase());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("words.txt", true))) {  //words.txt ���Ͽ� �߰�
            writer.println(wor.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void printRank() {  //rank.txt ���
        for (String name : rankMap.keySet()) {
            int score = rankMap.get(name);
            System.out.println("Name: " + name + ", Score: " + score);
        }
    }
	
	public void rankCheck() {
		try {
			Scanner scanner=new Scanner(new FileReader("rank.txt"));
			while(scanner.hasNext()) {
				String name = scanner.next();
				int score = scanner.nextInt();
				rankMap.put(name, score);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
	}
	
	public void updateRank(String name, int score) {  //���� ������Ʈ
        rankMap.put(name, score);

        // ���Ͽ� ������Ʈ�� ��ŷ ����
        try (PrintWriter writer = new PrintWriter(new FileWriter("rank.txt"))) {
            for (Map.Entry<String, Integer> entry : rankMap.entrySet()) {
                writer.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public List<Map.Entry<String, Integer>> getSortedRank() {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(rankMap.entrySet());

        //Comparator�� �̿��Ͽ� ����
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return entryList;
    }

    public void printSortedRank() {  //���� �� ��ũ���
        List<Map.Entry<String, Integer>> sortedRank = getSortedRank();
        for (Map.Entry<String, Integer> entry : sortedRank) {
            System.out.println("Name: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }
	
}