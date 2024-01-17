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
				word = removeSpecialCharacters(word); //특수기호 제거 정규표현식
				wordVector.add(word);
			}
			//scanner.close();
			//JOptionPane.showMessageDialog(parent, "단어 읽기 완료");
		} catch (FileNotFoundException e) {
			System.out.println("파일 없어요.");
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
        //특수 기호 제거 정규 표현식
        word = word.replaceAll("[^a-zA-Z0-9]", "");
        return word;
    }
	
	public String next() {  //다음 단어를 랜덤하게 출력
		int index=(int)(Math.random() * size);
		return wordVector.get(index);  //랜덤한 인덱스의 단어를 리턴해준다.
	}
	
	public int VectorSize() {
		return size;
	}
	public void addWord(String wor) {
        // 새로운 단어를 wordVector에 추가
        wordVector.add(wor.toLowerCase());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("words.txt", true))) {  //words.txt 파일에 추가
            writer.println(wor.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void printRank() {  //rank.txt 출력
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
	
	public void updateRank(String name, int score) {  //점수 업데이트
        rankMap.put(name, score);

        // 파일에 업데이트된 랭킹 저장
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

        //Comparator를 이용하여 정렬
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return entryList;
    }

    public void printSortedRank() {  //정렬 후 랭크출력
        List<Map.Entry<String, Integer>> sortedRank = getSortedRank();
        for (Map.Entry<String, Integer> entry : sortedRank) {
            System.out.println("Name: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }
	
}