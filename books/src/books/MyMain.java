package books;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Class to generate main page of CardLayout
 * @author Winter Goodman
 *
 */
class MyMain extends BookManager{
	/**
	 * Buttons and CardLayout panel
	 */
    private JButton mainToLib;
    private JButton mainToRead;
    private JPanel contentPane;
    /**
     * Background color
     */
    private final Color color = Color.decode("#3A0CA3");
    
    /**
     * Receives CardLayout panel.
     * @param contentPane
     */
    public MyMain(JPanel contentPane) {
    	this.contentPane = contentPane;
    }
    
    /**
     * Generates main panel
     * @return panel
     */
    public JPanel getPanel(){
        JPanel panel = new JPanel();
        //construct components
        mainToLib = new JButton ("Library");
        mainToLib.setFont(new Font("Serif", Font.PLAIN, 16));
        mainToLib.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                cardLayout.next(contentPane);
            }
        });
        mainToRead = new JButton("Reading List");
        mainToRead.setFont(new Font("Serif", Font.PLAIN, 16));
        mainToRead.addActionListener( new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        		cardLayout.next(contentPane);
        		cardLayout.next(contentPane);
        	}
        });
        doLayout(panel);
        return panel;
    }
    
    /**
     * Sets layout of panel
     * @param panel
     */
    public void doLayout(JPanel panel) {
    	panel.setOpaque(true);
    	panel.setPreferredSize(getPreferredSize());
        panel.setBackground(color);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
    	panel.add(mainToLib, gbc);
    	panel.add(mainToRead, gbc);
    }
}