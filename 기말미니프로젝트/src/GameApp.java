import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameApp {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		new GameFrame();
	}
}