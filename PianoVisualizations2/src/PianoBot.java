import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class PianoBot extends JPanel implements Runnable {

	final int FPS = 60;
	
	final int pauseInterval = 400;
	
	Thread thread;
	Robot robot;
	
	Path fileName = Path.of("src/Song");
	File file = new File("src/Song");
	String song;
	
	public PianoBot() throws IOException, AWTException {
		this.setPreferredSize(new Dimension(100, 100));
		
		song = Files.readString(fileName);
		song = song.replace("\n", "").replace("\r", "").replace(" ", "").replace("[", "").replace("]", "").replace("--", "=").replace("!", "|1").replace("@", "|2").replace("#", "|3").replace("$", "|4").replace("%", "|5").replace("^", "|6").replace("&", "|7").replace("*", "|8").replace("(", "|9").replace(")", "|0").replace("{", "").replace("}", "");
		
		robot = new Robot();
		
		thread = new Thread(this);
		thread.start();
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double drawInterval = 1000000000 / FPS;
		long lastTime = System.nanoTime();
		long currentTime;
		double delta = 0;
		
		try {
			runBot();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(thread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) * drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				repaint();
				delta--;
			}
		}
	}
	public void runBot() throws InterruptedException {
		if(thread != null) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i < song.length(); i++) {
				
				if (Character.isUpperCase(song.charAt(i))) {
					robot.keyPress(KeyEvent.VK_SHIFT);
				}
				
				if(song.charAt(i) == '=') {
					Thread.sleep(pauseInterval * 2);
					continue;
				} 
				else if(song.charAt(i) == '-') {
					Thread.sleep(pauseInterval / 2);
					continue;
				}
				else if(i-1 >= 0 && song.charAt(i-1) != '-' && song.charAt(i-1) != '='){
					Thread.sleep(pauseInterval);
				}
				
				if(song.charAt(i) == '[') {
					
					for(int j = i + 1; j < song.length(); j++) {
						thread.wait();
						if(song.charAt(j) == ']') {
							i = j;
							thread.notifyAll();
							break;
						}
						if(song.charAt(j) == '|') {
							robot.keyPress(KeyEvent.VK_SHIFT);
							robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j+1)));
							robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j+1)));
							robot.keyRelease(KeyEvent.VK_SHIFT);
							j++;
							continue;
						}
						
						if(Character.isUpperCase(song.charAt(j))) {
							robot.keyPress(KeyEvent.VK_SHIFT);
							robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j)));
							robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j)));
							robot.keyRelease(KeyEvent.VK_SHIFT);
							j++;
							continue;
						}
						
						robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j)));
						robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(song.charAt(j)));
					}
					
				} 
				else {		
					
					if(song.charAt(i) == '|') {
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(song.charAt(i+1)));
						robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(song.charAt(i+1)));
						robot.keyRelease(KeyEvent.VK_SHIFT);
						i++;
						continue;
					}
					
					robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(song.charAt(i)));
					robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(song.charAt(i)));
		
				}
				
				robot.keyRelease(KeyEvent.VK_SHIFT);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		
		g2.drawString("hi", 50, 50);
	}
}	
