package books;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;

public class BookTableModel extends AbstractTableModel {
	//class members
	private ArrayList<Book> bookList;
	//private final String[] columnNames = new String[] {"Title","Author","Series", "Series Position", "Pages", "Read", "Read Date"};
	//private final Class[] columnClass = new Class[] {String.class, String.class, String.class, Integer.class, Integer.class, Boolean.class, String.class};
	private String[] columnNames;
	private Class[] columnClass;
	private int type; //Type 1 is library table, 2 is reading table, 3 is TBR table
	/**
	 * Constructor. Loads given ArrayList into model.
	 * @param bookList
	 */
	public BookTableModel(ArrayList<Book> bookList, String[] columnNames, Class[] columnClass) {
		this.bookList = bookList;
		this.columnNames =columnNames;
		this.columnClass = columnClass;
		if(Arrays.asList(columnNames).contains("Read")) type=1;
		else if(Arrays.asList(columnNames).contains("Read Date")) type=2;
		else type = 3;
	}
	/**
	 * Clears the model.
	 */
	public void deleteAllRows() {
		this.bookList = new ArrayList<Book>();
	}
	/**
	 * Returns the current model data.
	 * @return bookList
	 */
	public ArrayList<Book> getBookList(){
		return this.bookList;
	}
	/**
	 * Returns Book object at given row of table.
	 * @param row
	 * @return book
	 */
	public Book getRow(int row) {
		return bookList.get(row);
	}
	/**
	 * Removes selected Book object from model.
	 * @param row
	 */
	public void deleteRow(int row) {
		bookList.remove(row);
	}
	/**
	 * Returns give column name.
	 * @param column
	 * @return columnName
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	/**
	 * Returns given column class.
	 * @param column
	 * @return column class
	 */
	@Override 
	public Class<?> getColumnClass(int column) {
		return columnClass[column];
	}
	/**
	 * Returns the number of columns.
	 * @return column number
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	/**
	 * Return the length of the model's ArrayList.
	 * @return size
	 */
	@Override
	public int getRowCount() {
		return bookList.size();
	}
	/**
	 * Returns attribute of Book object at given row and column.
	 * @param rowIndex
	 * @param columnIndex
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = bookList.get(rowIndex);
		switch(type) {
		case 1:
			switch(columnIndex) {
			case 0: 
				return book.getTitle(); 
			case 1: 
				return book.getAuthor(); 
			case 2:
				return book.getSeries();
			case 3: 
				return book.getSeriesPosition();
			case 4:
				return book.getNumPages();
			case 5:
				return book.getReadStatus();
			case 6:
				return book.getReadDate().getString().equals("00-00-0000")?"N/A":book.getReadDate().getString();
			default: 
				return null;
			}
		case 2:
			switch(columnIndex) {
			case 0: 
				return book.getTitle(); 
			case 1: 
				return book.getAuthor(); 
			case 2:
				return book.getSeries();
			case 3: 
				return book.getSeriesPosition();
			case 4:
				return book.getReadDate().getString().equals("00-00-0000")?"N/A":book.getReadDate().getString();
			default: 
				return null;
			}
		case 3:
			switch(columnIndex) {
			case 0: 
				return book.getTitle(); 
			case 1: 
				return book.getAuthor(); 
			case 2:
				return book.getSeries();
			case 3: 
				return book.getSeriesPosition();
			default: 
				return null;
			}
		}
		return book;
	}
	/**
	 * Makes table fully editable
	 * @param rowIndex
	 * @param columnIndex
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	@Override
	public void setValueAt(Object val, int rowIndex, int columnIndex) {
		Book book = bookList.get(rowIndex);
		int length = columnNames.length;
		if(length==7) {
		switch(columnIndex) {
		case 0: 
			book.setTitle((String) val);
			break;
		case 1:
			book.setAuthor((String) val);
			break;
		case 2:
			book.setSeries((String) val);
			break;
		case 3:
			book.setSeriesPosition((Integer) val);
			break;
		case 4: 
			book.setNumPages((Integer) val);
			break;
		case 5:
			book.setReadStatus((Boolean) val);
			break;
		case 6:
			book.setReadDate(parseDate((String) val));
			break;
		}}
		else if(length ==5) {
			switch(columnIndex) {
			case 0: 
				book.setTitle((String) val);
				break;
			case 1:
				book.setAuthor((String) val);
				break;
			case 2:
				book.setSeries((String) val);
				break;
			case 3:
				book.setSeriesPosition((Integer) val);
				break;
			case 4:
				book.setReadDate(parseDate((String) val));
				break;
			}
		}
		else if(length==4) {
			switch(columnIndex) {
			case 0: 
				book.setTitle((String) val);
				break;
			case 1:
				book.setAuthor((String) val);
				break;
			case 2:
				book.setSeries((String) val);
				break;
			case 3:
				book.setSeriesPosition((Integer) val);
				break;
			}
		}
	}
	/**
	 * Formats string into Date object.
	 * @param  date String
	 * @return Date object
	 */
	public Date parseDate(String date) {
		if(date.equals("00-00-0000")|| date.equals("N/A")) return new Date();
    	Pattern mmddyyyy = Pattern.compile("^\\d{2}.\\d{2}.\\d{4}$", Pattern.CASE_INSENSITIVE);
		Matcher matchermmddyyyy = mmddyyyy.matcher(date);
		boolean matchmmddyyyy = matchermmddyyyy.find();
		Pattern yyyymmdd = Pattern.compile("^\\d{4}.\\d{2}.\\d{2}$", Pattern.CASE_INSENSITIVE);
		Matcher matcheryyyymmdd = yyyymmdd.matcher(date);
		boolean matchyyyymmdd = matcheryyyymmdd.find();
		Pattern mmyyyy = Pattern.compile("^\\d{2}.\\d{4}$", Pattern.CASE_INSENSITIVE);
		Matcher matchermmyyyy = mmyyyy.matcher(date);
		boolean matchmmyyyy = matchermmyyyy.find();
		Pattern yyyy = Pattern.compile("\\d{4}", Pattern.CASE_INSENSITIVE);
		Matcher matcheryyyy = yyyy.matcher(date);
		boolean matchyyyy = matcheryyyy.find();
		int month = 0; int day = 0; int year = 0;
		if(matchmmddyyyy) {
			month = Integer.parseInt(date.substring(0, 2));
			day = Integer.parseInt(date.substring(3, 5));
			year = Integer.parseInt(date.substring(6));
		}
		else if(matchyyyymmdd) {
			month = Integer.parseInt(date.substring(5, 7));
			day = Integer.parseInt(date.substring(8));
			year = Integer.parseInt(date.substring(0,4));
		}
		else if(matchmmyyyy) {
			month = Integer.parseInt(date.substring(0, 2));
			day = 1;
			year = Integer.parseInt(date.substring(3));
		}
		else if(matchyyyy) {
			month = 1;
			day = 1;
			year = Integer.parseInt(date);
		}
		Date returnDate = new Date(month, day, year);
		return returnDate;
	}
	
	
}
