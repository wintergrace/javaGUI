package books;
import java.awt.*;
import javax.swing.*;
/**
 * Class to manage and run CardLayout
 * @author Winter Goodman
 *
 */
public class MainFrame{
	/**
	 * Class types and panels
	 */
	MyMain myMain;
	MyLibrary myLibrary;
    MyReading myReading;
    JPanel mainPanel;
    JPanel libraryPanel;
    JPanel readingPanel;
    /**
     * Constructor called by main
     */
    public MainFrame() {}
    
    /**
     * Function to get panels and display JFrame
     */
    public void displayGUI() {
    	JFrame frame = new JFrame();
    	JPanel contentPane = new JPanel();
    	myMain = new MyMain(contentPane);
    	myLibrary = new MyLibrary(contentPane);
    	myReading = new MyReading(contentPane);
    	mainPanel = myMain.getPanel();
    	libraryPanel = myLibrary.getPanel();
    	readingPanel = myReading.getPanel();
    	contentPane.setLayout(new CardLayout());
    	contentPane.add(mainPanel);
    	contentPane.add(libraryPanel);
    	contentPane.add(readingPanel);
    	frame.add(contentPane);
    	frame.pack();
    	frame.setVisible(true);
    }
    
    /**
     * Calls displayGUI()
     * @param args
     */
    public static void main(String... args){
    	MainFrame main = new MainFrame();
    	main.displayGUI();
    }
}