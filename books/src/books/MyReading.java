package books;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

class MyReading extends BookManager{
	/*
	 * Global members
	 * Note on naming r[variable name] = reading variable t[variablename] = tbr variable
	 */
	private JPanel panel;
    private JButton readToMain;
    private JButton rAddBook;
    private JButton tAddBook;
    private JPanel contentPane;
    private ArrayList<Book> rBooks= new ArrayList<Book>();
    private ArrayList<Book> tBooks= new ArrayList<Book>();
    private JTable rTable;
    private JScrollPane rTableHolder;
    private JTable tTable;
    private JScrollPane tTableHolder;
    private JComboBox<String> rDropdown;
    private JComboBox<String> tDropdown;
    private JButton rSortButton;
    private JButton tSortButton;
    private int rSortBy = 0;
    private int tSortBy = 0;
    private int rActiveRow = -1;
    private int tActiveRow = -1;
    private final String rFilePath = "readingbooks.txt";
    private final String tFilePath = "tbrbooks.txt";
    private final String[] rColumnNames = new String[] {"Title","Author","Series", "Series Position", "Read Date"};
  	private final Class[] rColumnClass = new Class[] {String.class, String.class, String.class, Integer.class, String.class};
  	private final String[] tColumnNames = new String[] {"Title","Author","Series", "Series Position"};
   	private final Class[] tColumnClass = new Class[] {String.class, String.class, String.class, Integer.class};
    private final Color color = Color.decode("#F72585");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	LocalDateTime now = LocalDateTime.now();
	private final String currDate = dtf.format(now);
    /**
     * Receives CardLayout panel
     * @param contentPane
     */
    public MyReading(JPanel contentPane) {
    	this.contentPane = contentPane;
    }
    
    /**
     * Creates library panel.
     * @param panel
     */
    public JPanel getPanel(){
        //set up
    	panel = new JPanel();
        readReadFile(); 
        readFile(tBooks, tFilePath);
        
        //back to main button
        readToMain = new JButton ("Back to Main");
        readToMain.setFont(new Font("Serif", Font.PLAIN, 16));
        readToMain.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                cardLayout.next(contentPane);
            }
        });
        
        //add book buttons
        rAddBook = new JButton("Add Book");
        rAddBook.setFont(new Font("Serif", Font.PLAIN, 16));
        rAddBook.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		addBook(0);
        	}
        });	
        tAddBook = new JButton("Add Book");
        tAddBook.setFont(new Font("Serif", Font.PLAIN, 16));
        tAddBook.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		addBook(1);
        	}
        });	
       
        //dropdowns
        String[] rSortOptions = {"Title", "Author", "Series", "Read Date"};
        rDropdown = new JComboBox<>(rSortOptions);
        rDropdown.setEditable(true);
        rDropdown.setSelectedItem("Sort by");
        rDropdown.setFont(new Font("Serif", Font.PLAIN, 16));
        rDropdown.setMaximumSize(rDropdown.getPreferredSize());
        String[] tSortOptions = {"Title", "Author", "Series"};
        tDropdown = new JComboBox<>(rSortOptions);
        tDropdown.setEditable(true);
        tDropdown.setSelectedItem("Sort by");
        tDropdown.setFont(new Font("Serif", Font.PLAIN, 16));
        tDropdown.setMaximumSize(rDropdown.getPreferredSize());
        
        //sort buttons
        rSortButton = new JButton("Sort");
        rSortButton.setFont(new Font("Serif", Font.PLAIN, 16));	
        rSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String sort = rDropdown.getItemAt(rDropdown.getSelectedIndex());
                switch(sort) {
	                case "Title": rSortBy = 0; break;
	                case "Author": rSortBy = 1; break;
	                case "Series": rSortBy = 2; break;
	                case "Pages": rSortBy = 3; break;
	                case "Read Date": rSortBy = 4; break;
	                default: rSortBy = 0; break;
                }
            	reloadRTable();
            }
        });
        tSortButton = new JButton("Sort");
        tSortButton.setFont(new Font("Serif", Font.PLAIN, 16));	
        tSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String sort = tDropdown.getItemAt(tDropdown.getSelectedIndex());
                switch(sort) {
	                case "Title": tSortBy = 0; break;
	                case "Author": tSortBy = 1; break;
	                case "Series": tSortBy = 2; break;
	                case "Pages": tSortBy = 3; break;
	                case "Read Date": tSortBy = 4; break;
	                default: tSortBy = 0; break;
                }
                reloadTTable();
            }
        });
        
        //table set up and key listener for delete row
        rTable = loadData(rSortBy, 0);
        rTableHolder=new JScrollPane(rTable);
        rTable.addKeyListener(getTableKeyListener(rTable, rFilePath));
        MouseListener rMouseListener = getRMouseListener();
        rTable.addMouseListener(rMouseListener);
        tTable = loadData(tSortBy, 1);
        tTableHolder=new JScrollPane(tTable);
        tTable.addKeyListener(getTableKeyListener(tTable, tFilePath));
        MouseListener tMouseListener = getTMouseListener();
        tTable.addMouseListener(tMouseListener);
        
        //layout
        doLayout(panel);
        return panel;
    }
    /**
     * Special readFile for reading books. Scans library books to see if there are any new read books in addition to reading off readingbooks.txt
     */
    public void readReadFile() {
    	try {
        	FileReader f=new FileReader(rFilePath);
            BufferedReader brk=new BufferedReader(f);       
            String s;
            while((s=brk.readLine())!=null){
            	Book book = stringToBook(s);
            	rBooks.add(book);
            }
            FileReader fLib = new FileReader("books.txt");
            BufferedReader brkLib=new BufferedReader(fLib);
            String sLib;
            while((sLib = brkLib.readLine())!=null) {
            	Book libBook = stringToBook(sLib);
            	if(libBook.getReadStatus()&&!isRepeat(libBook, rBooks)) rBooks.add(libBook);
            }
        }
        catch(Exception e) {
        	
        }
    }
    /**
     * reloads and revalidatse reading table
     */
    public void reloadRTable() {
    	int sortBy = rSortBy;
    	String[] columnNames = rColumnNames;
    	Class[] columnClass = rColumnClass;
    	BookTableModel emptyModel = new BookTableModel(new ArrayList<Book>(), columnNames, columnClass);
        rTable.setModel(emptyModel);
        rTable.revalidate();
        rTable = loadData(sortBy, 0);
        rTableHolder = new JScrollPane(rTable);
        MouseListener mouseListener = getRMouseListener();
        rTable.addMouseListener(mouseListener);
        rTable.revalidate();
        rTable.repaint();
        rTableHolder.revalidate();
        rTableHolder.repaint();
        panel.removeAll();
        doLayout(panel);
    }
    /**
     * reloads and revalidates TBR table
     */
    public void reloadTTable() {
    	int sortBy = tSortBy;
    	String[] columnNames = tColumnNames;
    	Class[] columnClass = tColumnClass;
    	BookTableModel emptyModel = new BookTableModel(new ArrayList<Book>(), columnNames, columnClass);
        tTable.setModel(emptyModel);
        tTable.revalidate();
        tTable = loadData(sortBy, 1);
        tTableHolder = new JScrollPane(tTable);
        MouseListener mouseListener = getTMouseListener();
        tTable.addMouseListener(mouseListener);
        tTable.revalidate();
        tTable.repaint();
        tTableHolder.revalidate();
        tTableHolder.repaint();
        panel.removeAll();
        doLayout(panel);
        panel.removeAll();
        doLayout(panel);
    }
    /**
     * Returns the mouse listener for Delete/Move to Library function for reading table.
     * @return MouseListener
     */
    public MouseListener getRMouseListener() {
    	 ActionListener actionListener = new ActionListener() {
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				if(e.getActionCommand().equals("Delete")) {
 					BookTableModel model = new BookTableModel(rBooks, rColumnNames, rColumnClass);
 					model.deleteRow(rActiveRow);
 					rTable.setModel(model);
 					saveTableData(model, rFilePath);
 				}
 				else {
 					BookTableModel model = new BookTableModel(rBooks, rColumnNames, rColumnClass);
 					Book book = model.getRow(rActiveRow);
 					writeToFile(book, "books.txt");
 				}
 			}
         };
    	JPopupMenu actionsMenu = new JPopupMenu("Actions");
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(actionListener);
        JMenuItem addItem = new JMenuItem("Add to Library");
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
                rActiveRow = rTable.rowAtPoint(e.getPoint());
                actionsMenu.show(e.getComponent(), e.getX(), e.getY());
              }
            }
          };
          return mouseListener;
    }
    /**
     * Returns the mouse listener for Delete/Move to Read function for TBR table.
     * @return MouseListener
     */
    public MouseListener getTMouseListener() {
   	 ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Delete")) {
					BookTableModel model = new BookTableModel(tBooks, tColumnNames, tColumnClass);
					model.deleteRow(tActiveRow);
					tTable.setModel(model);
					saveTableData(model, tFilePath);
				}
				else {
					BookTableModel model = new BookTableModel(tBooks, tColumnNames, tColumnClass);
					Book book = model.getRow(tActiveRow);
					book.setReadStatus(true);
					book.setReadDate(parseDate(currDate));
					System.out.println(model.getRowCount());
					model.deleteRow(tActiveRow);
					System.out.println(model.getRowCount());
					tTable.setModel(model);
					saveTableData(model, tFilePath);
					reloadTTable();
					writeToFile(book, rFilePath);
					reloadRTable();
				}
			}
        };
       JPopupMenu actionsMenu = new JPopupMenu("Actions");
       JMenuItem deleteItem = new JMenuItem("Delete");
       deleteItem.addActionListener(actionListener);
       JMenuItem addItem = new JMenuItem("Move to Read");
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
               tActiveRow = tTable.rowAtPoint(e.getPoint());
               actionsMenu.show(e.getComponent(), e.getX(), e.getY());
             }
           }
         };
         return mouseListener;
   }
    /**
     * Generates addBook popup frame
     */
    public void addBook(int type) {
    	ArrayList<Book> books = (type==0)?rBooks:tBooks;
    	String[] columnNames = (type==0)?rColumnNames:tColumnNames;
    	Class[] columnClass = (type==0)?rColumnClass:tColumnClass;
    	String filePath = (type==0)?rFilePath:tFilePath;
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
		if(type == 0) {
			readField.setText("Yes");
			
			readDateField.setText(currDate);
		}
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
        		if(type==0) reloadRTable();
        		else reloadTTable();
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
        		if(type==0) reloadRTable();
        		else reloadTTable();
        		BookTableModel model = new BookTableModel(books, columnNames, columnClass);
        		saveTableData(model, filePath);
        		addBook(type);
        	}
        });
    }
    /**
     * Sets up the GroupLayout for library panel.
     * @param panel
     */
    public void doLayout(JPanel panel) {
    	panel.setPreferredSize(getPreferredSize());
    	panel.setOpaque(true);
        panel.setBackground(color);	
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(rTableHolder)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(rAddBook)
						.addComponent(rDropdown)
						.addComponent(rSortButton))
				.addComponent(tTableHolder)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(tAddBook)
						.addComponent(tDropdown)
						.addComponent(tSortButton)
						.addComponent(readToMain))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(rTableHolder)
						.addGroup(layout.createSequentialGroup()
							.addComponent(rDropdown)
							.addComponent(rSortButton)
							.addComponent(rAddBook)
							)
						.addComponent(tTableHolder)
						.addGroup(layout.createSequentialGroup()
								.addComponent(tDropdown)
								.addComponent(tSortButton)
								.addComponent(tAddBook)
								.addComponent(readToMain))
						);
    }
    /**
     * Loads current values of ArrayList books into model for table
     * @param sortBy
     * @return updated JTable
     */
    public JTable loadData(int sortBy, int type) {
    	ArrayList<Book> books = (type==0)?rBooks:tBooks;
    	String[] columnNames = (type==0)?rColumnNames:tColumnNames;
    	Class[] columnClass = (type==0)?rColumnClass:tColumnClass;
    	sort(sortBy, books);
    	BookTableModel model = new BookTableModel(books, columnNames, columnClass);
    	return new JTable(model);
    }
}