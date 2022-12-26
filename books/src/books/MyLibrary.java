package books;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 * Class to generate library manager panel
 * @author Winter Goodman
 */
class MyLibrary extends BookManager{
	/*
	 * Global members
	 */
	private JPanel panel;
    private JButton libToMain;
    private JButton addBook;
    private JPanel contentPane;
    private ArrayList<Book> books= new ArrayList<Book>();
    private JTable table;
    private JScrollPane tableHolder;
    private JComboBox<String> dropdown;
    private JButton sortButton;
    private int sortBy = 0;
    private int activeRow = -1;	
    private final String filePath = "books.txt";
    private final String[] columnNames = new String[] {"Title","Author","Series", "Series Position", "Pages", "Read", "Read Date"};
  	private final Class[] columnClass = new Class[] {String.class, String.class, String.class, Integer.class, Integer.class, Boolean.class, String.class};
    private final Color color = Color.decode("#7209B7");
    /**
     * Receives CardLayout panel
     */
    public MyLibrary(JPanel contentPane) {
    	this.contentPane = contentPane;
    }
    
    /**
     * Generates library panel.
     * @param panel
     */
    public JPanel getPanel(){
        //set up
    	panel = new JPanel();
        readFile(books, filePath);
        
        //back to main button
        libToMain = new JButton ("Back to Main");
        libToMain.setFont(new Font("Serif", Font.PLAIN, 16));
        libToMain.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                cardLayout.next(contentPane);
                cardLayout.next(contentPane);
            }
        });
        
        //add book button
        addBook = new JButton("Add Book");
        addBook.setFont(new Font("Serif", Font.PLAIN, 16));
        addBook.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		addBook();
        	}
        });	
       
        //dropdown 
        String[] sortOptions = {"Title", "Author", "Series", "Pages", "Read Date"};
        dropdown = new JComboBox<>(sortOptions);
        dropdown.setEditable(true);
        dropdown.setSelectedItem("Sort by");
        dropdown.setFont(new Font("Serif", Font.PLAIN, 16));
        dropdown.setMaximumSize(dropdown.getPreferredSize());
        
        //sort button
        sortButton = new JButton("Sort");
        sortButton.setFont(new Font("Serif", Font.PLAIN, 16));	
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String sort = dropdown.getItemAt(dropdown.getSelectedIndex());
                switch(sort) {
	                case "Title": sortBy = 0; break;
	                case "Author": sortBy = 1; break;
	                case "Series": sortBy = 2; break;
	                case "Pages": sortBy = 3; break;
	                case "Read Date": sortBy = 4; break;
	                default: sortBy = 0; break;
                }
            
                reloadTable();
                panel.removeAll();
                doLayout(panel);
            }
        });
        
        //table set up and key listener for delete row
        table = loadLibraryData(sortBy);
        tableHolder=new JScrollPane(table);
        table.addKeyListener(getTableKeyListener(table, filePath));
        MouseListener mouseListener = getMouseListener();
        table.addMouseListener(mouseListener);
        
        //layout
        doLayout(panel);
        return panel;
    }
    /**
     * Updates table model
     */
    public void reloadTable() {
    	BookTableModel emptyModel = new BookTableModel(new ArrayList<Book>(), columnNames, columnClass);
        table.setModel(emptyModel);
        table.revalidate();
        table = loadLibraryData(sortBy);
        tableHolder = new JScrollPane(table);
        table.addMouseListener(getMouseListener());
        table.revalidate();
        table.repaint();
        tableHolder.revalidate();
        tableHolder.repaint();
    }
    
    /**
     * Constructs mouse listener and pop up menu for table
     * @return mouseListener
     */
    public MouseListener getMouseListener() {
    	 ActionListener actionListener = new ActionListener() {
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				if(e.getActionCommand().equals("Delete")) {
 					BookTableModel model = new BookTableModel(books, columnNames, columnClass);
 					model.deleteRow(activeRow);
 					table.setModel(model);
 					saveTableData(model, filePath);
 				}
 				else {
 					BookTableModel model = new BookTableModel(books, columnNames, columnClass);
 					Book book = model.getRow(activeRow);
 					writeToFile(book, "tbrbooks.txt");
 				}
 			}
         };
    	JPopupMenu actionsMenu = new JPopupMenu("Actions");
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(actionListener);
        JMenuItem addItem = new JMenuItem("Add to To Be Read List");
        addItem.addActionListener(actionListener);
        actionsMenu.add(deleteItem);
        actionsMenu.add(addItem);
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
              checkPopup(e);
            }

            public void mouseClicked(MouseEvent e) {
              checkPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
              checkPopup(e);
            }

            private void checkPopup(MouseEvent e) {
              if (e.isPopupTrigger()) {
                Component selectedComponent = e.getComponent();
                activeRow = table.rowAtPoint(e.getPoint());
                actionsMenu.show(e.getComponent(), e.getX(), e.getY());
              }
            }
          };
          return mouseListener;
    }
    
    /**
     * Generates addBook frame
     */
    public void addBook() {
    	//set up fields and labels
		ArrayList<JTextField> textFields = new ArrayList<JTextField>();
		for(int i = 0; i < 7; i++) {
			JTextField field = new JTextField(20);
			field.setFont(new Font("Serif", Font.PLAIN, 18));
			textFields.add(field);
		}
		JTextField titleField = textFields.get(0);
		JTextField authorField = textFields.get(1);
		JTextField seriesField = textFields.get(2);
		JTextField positionField = textFields.get(3);
		JTextField pageField = textFields.get(4);
		JTextField readField = textFields.get(5);
		JTextField readDateField = textFields.get(6);
		seriesField.setText("N/A");
		positionField.setText("N/A");
		pageField.setText("0");
		readField.setText("No");
		readDateField.setText("N/A");
		ArrayList<JLabel> textLabels = new ArrayList<JLabel>();
		for(int i = 0; i < 7; i++) {
			JLabel label = new JLabel();
			label.setFont(new Font("Serif", Font.PLAIN, 18));
			textLabels.add(label);
		}
		JLabel titleLabel = textLabels.get(0);
		JLabel authorLabel = textLabels.get(1);
		JLabel seriesLabel = textLabels.get(2);
		JLabel positionLabel = textLabels.get(3);
		JLabel pageLabel = textLabels.get(4);
		JLabel readLabel = textLabels.get(5);
		JLabel readDateLabel = textLabels.get(6);
		titleLabel.setText("Title:");
		authorLabel.setText("Author:");
		seriesLabel.setText("Series: ");
		positionLabel.setText("Position:");
		pageLabel.setText("Pages:");
		readLabel.setText("Read:");
		readDateLabel.setText("Read Date:");
		
		//set up buttons
		JButton closeAddPane = new JButton("Cancel");
		JButton addAddPane = new JButton("Add another");
		JButton doneAddPane = new JButton("Done");
		closeAddPane.setFont(new Font("Serif", Font.BOLD, 18));
		addAddPane.setFont(new Font("Serif", Font.BOLD, 18));
		doneAddPane.setFont(new Font("Serif", Font.BOLD, 18));
		closeAddPane.setBackground(new Color(255, 100, 100));
		addAddPane.setBackground(new Color(45,183,252));
		doneAddPane.setBackground(new Color(107,247,96));
		
		//do layout
		JPanel addPanel = new JPanel();
		GroupLayout layout = new GroupLayout(addPanel);
		addPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)   
						   .addComponent(titleLabel)
						   .addComponent(authorLabel)
						   .addComponent(seriesLabel)
						   .addComponent(positionLabel)
						   .addComponent(pageLabel)
						   .addComponent(readLabel)
						   .addComponent(readDateLabel)
						   .addComponent(closeAddPane)
						   .addComponent(addAddPane)
						   .addComponent(doneAddPane))
				   		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)   
				   		   .addComponent(titleField)
						   .addComponent(authorField)
						   .addComponent(seriesField)
						   .addComponent(positionField)
						   .addComponent(pageField)
						   .addComponent(readField)
						   .addComponent(readDateField))
				);
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(titleLabel)
				           .addComponent(titleField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(authorLabel)
					           .addComponent(authorField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(seriesLabel)
					           .addComponent(seriesField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(positionLabel)
					           .addComponent(positionField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(pageLabel)
					           .addComponent(pageField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(readLabel)
					           .addComponent(readField))
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					           .addComponent(readDateLabel)
					           .addComponent(readDateField))
				      .addComponent(closeAddPane)
				      .addComponent(addAddPane)
				      .addComponent(doneAddPane)
				);
				
		//generate frame
		JFrame frame = new JFrame();
		frame.setContentPane(addPanel);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();   
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        
        //button actions
        closeAddPane.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.setVisible(false);
        	}
        });
        doneAddPane.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String name = capitalize(titleField.getText());
        		String author = capitalize(authorField.getText());
        		String series = seriesField.getText().equals("N/A")?"":capitalize(seriesField.getText());
        		int seriesPosition = positionField.getText().equals("N/A")?0:Integer.parseInt(positionField.getText());
        		int numPages = Integer.parseInt(pageField.getText());
        		boolean readStatus = readField.getText().equals("No")?false:true;
        		Date readDate = readField.getText().equals("No")?parseDate("00-00-0000"):parseDate(readDateField.getText());
        		Book book = new Book(name, author, series, seriesPosition, numPages, readStatus, readDate);
        		books.add(book);
        		writeToFile(book, filePath);
        		frame.setVisible(false);
        		reloadTable();
                panel.removeAll();
                doLayout(panel);
        		BookTableModel model = new BookTableModel(books, columnNames, columnClass);
        		saveTableData(model, filePath);
        	}
        });
        addAddPane.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String name = titleField.getText();
        		String author = authorField.getText();
        		String series = seriesField.getText().equals("N/A")?"":seriesField.getText();
        		int seriesPosition = positionField.getText().equals("N/A")?0:Integer.parseInt(positionField.getText());
        		int numPages = Integer.parseInt(pageField.getText());
        		boolean readStatus = readField.getText().equals("No")?false:true;
        		Date readDate = readField.getText().equals("No")?parseDate("00-00-0000"):parseDate(readDateField.getText());
        		Book book = new Book(name, author, series, seriesPosition, numPages, readStatus, readDate);
        		books.add(book);
        		writeToFile(book, filePath);
        		frame.setVisible(false);
        		reloadTable();
                panel.removeAll();
                doLayout(panel);
        		BookTableModel model = new BookTableModel(books, columnNames, columnClass);
        		saveTableData(model, filePath);
        		addBook();
        	}
        });
    }
    
    /**
     * Sets up the GroupLayout for library panel.
     * @param panel
     */
    public void doLayout(JPanel panel) {
    	panel.setOpaque(true);
    	panel.setPreferredSize(getPreferredSize());
        panel.setBackground(color);
    	GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(tableHolder)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(addBook)
						.addComponent(libToMain)
						.addComponent(dropdown)
						.addComponent(sortButton)));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(tableHolder)
						.addGroup(layout.createSequentialGroup()
							.addComponent(dropdown)
							.addComponent(sortButton)
							.addComponent(addBook)
							.addComponent(libToMain)
							));
    }
    /**
     * Loads current values of ArrayList books into model for table
     * @param sortBy
     * @return updated JTable
     */
    public JTable loadLibraryData(int sortBy) {
    	sort(sortBy, books);
    	BookTableModel model = new BookTableModel(books, columnNames, columnClass);
    	return new JTable(model);
    }
}