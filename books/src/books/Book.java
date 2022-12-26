package books;

public class Book {
	//class members
	private String title;
	private String author;
	private String series;
	private int seriesPosition;
	private int numPages;
	private boolean readStatus;
	private Date readDate;
	/**
	 * Constructor with full data for a Book object
	 * @param title
	 * @param author
	 * @param series
	 * @param seriesPosition
	 * @param numPages
	 * @param readStatus
	 * @param readDate
	 */
	public Book(String title, String author, String series, int seriesPosition, int numPages, boolean readStatus, Date readDate) {
		this.title = title;
		this.author = author;
		this.series = series;
		this.seriesPosition = seriesPosition;
		this.numPages = numPages;
		this.readStatus = readStatus;
		this.readDate = readDate;
	}
	/**
	 * Default constructor for Book object
	 */
	public Book() {
		this.title = "";
		this.author = "";
		this.series = "";
		this.seriesPosition = 0;
		this.numPages = 0;
		this.readStatus = false;
		this.readDate = new Date();
	}
	/*
	 * Getters and setters
	 */
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getSeries() {
		return series;
	}
	public int getSeriesPosition() {
		return seriesPosition;
	}
	public int getNumPages() {
		return numPages;
	}
	public boolean getReadStatus() {
		return readStatus;
	}
	public Date getReadDate() {
		return readDate;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public void setSeriesPosition(int seriesPosition) {
		this.seriesPosition = seriesPosition;
	}
	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}
	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	/**
	 * Compares titles of two books according to compare() conventions
	 * @param book
	 * @return int compare
	 */
	public int compareTitle(Book book) {
		String theTitle1 = this.getTitle();
		String theTitle2 = book.getTitle();
		if(theTitle1.split(" ")[0].equals("The")) {
			theTitle1 = theTitle1.substring(4);
		}
		if(theTitle2.split(" ")[0].equals("The")) {
			theTitle2 = theTitle2.substring(4);
		}
		return theTitle1.compareTo(theTitle2);
	}
	/**
	 * Compares authors of two books according to compare() conventions
	 * @param book
	 * @return int compare
	 */
	public int compareAuthor(Book book) {
		String lastName1 = this.getAuthor().split(" ")[this.getAuthor().split(" ").length-1];
		String lastName2 = book.getAuthor().split(" ")[book.getAuthor().split(" ").length-1];
		int compare = lastName1.compareTo(lastName2);
		if(compare == 0) return this.compareSeries(book);
		return compare;
	}
	/**
	 * Compares series of two books according to compare() conventions
	 * @param book
	 * @return int compare
	 */
	public int compareSeries(Book book) {
		String series1 = this.getSeries();
		String series2 = book.getSeries();
		if(series1.equals("")&&series2.equals("")) return this.compareTitle(book);
		else if(series1.equals("")&&!series2.equals("")) return 1;
		else if(!series1.equals("")&&series2.equals("")) return -1;
		else if(series1.compareTo(series2) ==0) {
			if(this.getSeriesPosition()<book.getSeriesPosition()) {
				return -1;
			}
			else return 1;
		}
		else {
			String theSeries1 = this.getSeries();
			String theSeries2 = book.getSeries();
			if(theSeries1.split(" ")[0].equals("The")) {
				theSeries1 = theSeries1.substring(4);
			}
			if(theSeries2.split(" ")[0].equals("The")) {
				theSeries2 = theSeries2.substring(4);
			}
			return theSeries1.compareTo(theSeries2);
		}
	}
	/**
	 * Compares pages of two books according to compare() conventions
	 * @param book
	 * @return int compare
	 */
	public int comparePages(Book book) {
		if(this.getNumPages()>book.getNumPages()) {
			return 1;
		}
		else if(this.getNumPages()==book.getNumPages()) {
			return this.compareTitle(book);
		}
		else return -1;
	}
	/**
	 * Compares read dates of two books according to compare() conventions
	 * @param book
	 * @return int compare
	 */
	public int compareReadDate(Book book) {
		int compare = this.getReadDate().compareDate(book.getReadDate());
		if(compare == 0) return this.compareTitle(book);
		else return compare;
	}
	/**
	 * Checks if two books have the same title and author
	 * @param book
	 * @return true if same, false if not same
	 */
	public boolean equals(Book book) {
		return this.getTitle().equals(book.getTitle())&&this.getAuthor().equals(book.getAuthor());
	}
	/**
	 * Returns a string for writing to files
	 * @return bookString
	 */
	public String print() {
		String seriesString = series.equals("")?"N":"Y-"+series+"-" + seriesPosition;
		String readString = readStatus==true?"Y":"N";
		return title + "/" + author + "/"+seriesString+"/" + numPages +"/" + readString +"/" + readDate.getString()+"\n";
	}
}
