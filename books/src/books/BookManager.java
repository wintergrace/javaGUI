package books;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
/*
 * CLASS TO HOLD FUNCTIONS THAT LIBRARY AND READING SHARE
 */
public class BookManager {
	/**
	 * Capitalizes strings according to title conventions.
	 * @param str
	 * @return capitalized string
	 */
	public String capitalize(String str) {
    	String[] nonCapWords = new String[] {"a", "an", "the", "an", "but", "aboard", "about", "above", "across", "after", "against", "along", "amid", "among", "anti", "around", "as", "at", "before", "behind", "below", "beneath", "beside", "besides", "between", "beyond", "but", "by", "concerning", "considering", "despite", "down", "during", "", "except", "excepting", "excluding", "following", "for", "from", "in", "inside", "into", "", "like", "minus", "", "near", "", "of", "off", "on", "onto", "opposite", "outside", "over", "", "past", "per", "plus", "regarding", "round", "", "save", "since", "", "than", "through", "to", "toward", "towards", "under", "underneath", "unlike", "until", "up", "upon", "versus", "via", "with", "within", "without", 
};
    	str = str.toLowerCase();
        String returnString = "";
        String[] elements = str.split(" ");
        for(int i = 0; i < elements.length; i++) {
        	boolean nonCap = false;
        	for(int j = 0; j < nonCapWords.length; j++) {
        		if(elements[i].equals(nonCapWords[j])) {
        			nonCap = true;
        		}
        	}
        	if(nonCap&&i!=0) {
        	}
        	else elements[i] = Character.toString(Character.toUpperCase(elements[i].charAt(0))) + elements[i].substring(1);
        	returnString+=elements[i];
        	if(i<elements.length-1) returnString+=" ";
        }
        return returnString;
    }
	/**
	 * Formats strings into Date objects
	 * @param date string
	 * @return Date object
	 */
	public Date parseDate(String date) {
    	if(date.equals("00-00-0000")) return new Date();
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
	/**
	 * Parses book object from fileString
	 * @param string
	 * @return Book object
	 */
	public Book stringToBook(String s) {
    	String[] attributes = s.split("/");
    	String title = capitalize(attributes[0]);
    	String author = capitalize(attributes[1]);
    	String seriesString = capitalize(attributes[2]);
    	String series = "";
    	int seriesPosition = 0;
    	if(!seriesString.equals("N")) {
    		series = capitalize(seriesString.split("-")[1]);
    		seriesPosition = Integer.parseInt(seriesString.split("-")[2]);
    	}
    	int pages = Integer.parseInt(attributes[3]);
    	boolean readStatus = attributes[4].equals("Y")?true:false; 
    	Date readDate = new Date();
    	if(readStatus) {
    		readDate = parseDate(attributes[5]);
    	}
    	Book book = new Book(title, author, series, seriesPosition, pages, readStatus, readDate);
    	return book;
	}
	/**
     * Returns preferred size for main panels
     * @return Dimension size
     */
	public Dimension getPreferredSize(){
    	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    	int width = (int)size.getWidth();
		int height = (int)size.getHeight();
        return (new Dimension(width*3/4, height*3/4));
    }
	/**
     * Returns preferred size for add book panels
     * @return Dimension size
     */
    public Dimension addPreferredSize() {
    	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    	int width = (int)size.getWidth();
		int height = (int)size.getHeight();
        return (new Dimension(width*1/4, height*3/4));
    }
    /**
     * Checks if a book already exists in ArrayList
     * @param book
     * @param books
     * @return 
     */
	public boolean isRepeat(Book book, ArrayList<Book> books) {
		for(int i = 0; i < books.size(); i++) {
			if(books.get(i).equals(book)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sorts ArrayList given a book attribute to sort by
	 * @param sortBy to specify which attribute
	 * @param ArrayList books
	 */
	public void sort(int sortBy, ArrayList<Book> books) {
    	int size = books.size();
    	switch(sortBy) {
    	case 0:
    	      for(int i = 0; i<size-1; i++) {
    	         for (int j = i+1; j<books.size(); j++) {
    	            if(books.get(i).compareTitle(books.get(j))>0) {
    	               Book temp = books.get(i);
    	               books.set(i, books.get(j));
    	               books.set(j, temp);
    	            }
    	         }
    	      }
    	      break;
    	case 1:
    		for(int i = 0; i<size-1; i++) {
   	         for (int j = i+1; j<books.size(); j++) {
   	            if(books.get(i).compareAuthor(books.get(j))>0) {
   	               Book temp = books.get(i);
   	               books.set(i, books.get(j));
   	               books.set(j, temp);
   	            }
   	         }
   	      	}
    		break;
    	case 2:
    		for(int i = 0; i<size-1; i++) {
   	         for (int j = i+1; j<books.size(); j++) {
   	            if(books.get(i).compareSeries(books.get(j))>0) {
   	               Book temp = books.get(i);
   	               books.set(i, books.get(j));
   	               books.set(j, temp);
   	            }
   	            
   	        }
   	      	}
    		break;
    	case 3:
    		for(int i = 0; i<size-1; i++) {
    			for (int j = i+1; j<books.size(); j++) {
      	            if(books.get(i).comparePages(books.get(j))>0) {
      	               Book temp = books.get(i);
      	               books.set(i, books.get(j));
      	               books.set(j, temp);
      	            }
      	        }
      	    }
    		break;
    	case 4:
    		for(int i = 0; i<size-1; i++) {
    			for (int j = i+1; j<books.size(); j++) {
      	            if(books.get(i).compareReadDate(books.get(j))>0) {
      	               Book temp = books.get(i);
      	               books.set(i, books.get(j));
      	               books.set(j, temp);
      	            }
      	        }
      	    }
    		break;
    	default:
    		break;
    	}
    }
	/**
	 * Saves BookTableModel data to a file
	 * @param model
	 * @param filePath
	 */
	public void saveTableData(BookTableModel model, String filePath) {
    	PrintWriter writer;
		try {
			writer = new PrintWriter(filePath);
			writer.print("");
	    	writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	ArrayList<Book> tableList = model.getBookList();
    	for(Book book: tableList) {
    		writeToFile(book, filePath);
    	}
    }
	/**
	 * Generates KeyListener to update file when table is edited
	 * @param table
	 * @param filePath
	 * @return KeyListener
	 */
	public KeyListener getTableKeyListener(JTable table, String filePath) {
		KeyListener listener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {	
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}	
			@Override
			public void keyReleased(KeyEvent e) {
				saveTableData((BookTableModel) table.getModel(), filePath);
			}
        };
        return listener;
	}
	
	
	/**
     * Imports data from file into ArrayList
     */
    public void readFile(ArrayList<Book> books, String filePath) {
    	try {
        	FileReader f=new FileReader(filePath);
            BufferedReader brk=new BufferedReader(f);       
            String s;
            while((s=brk.readLine())!=null){
            	Book book = stringToBook(s);
            	books.add(book);
            }
        }
        catch(Exception e) {
        	
        }
    }
    /**
     * Appends an individual book to a file.
     * @param book
     * @param filePath
     */
	public void writeToFile(Book book, String filePath) {
    	try {
			FileWriter writer = new FileWriter(filePath, true);
			writer.write(book.print());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
