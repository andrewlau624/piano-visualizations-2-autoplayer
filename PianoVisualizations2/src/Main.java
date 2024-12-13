import java.awt.AWTException;
import java.io.IOException;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws AWTException, IOException {	
		
		JFrame system = new JFrame();
		system.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		system.setTitle("Piano Bot");	
		system.setAlwaysOnTop(true);
		
		PianoBot pianobot = new PianoBot();
		system.add(pianobot);
		system.pack();
		
		system.setResizable(false);
		system.setVisible(true);
	}

}
