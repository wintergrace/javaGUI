package books;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date {
	//class members
	private int month;
	private int day;
	private int year;
	
	/**
	 * Default constructor 
	 */
	public Date() {
		this.month = 0;
		this.day = 0;
		this.year = 0;
	}
	/**
	 * Constructor with data
	 * @param month
	 * @param day
	 * @param year
	 */
	public Date(int month, int day, int year) {
		this.month = month;
		this.day = day;
		this.year = year;
	}
	/*
	 * Getters and setters
	 */
	public int getMonth() {
		return this.month;
	}
	public int getDay() {
		return this.day;
	}
	public int getYear() {
		return this.year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * Returns display string for Date
	 * @return dateString
	 */
	public String getString() {
		String monthString = getMonth() >= 10? Integer.toString(getMonth()):"0" + Integer.toString(getMonth());
		String dayString = getDay() >= 10? Integer.toString(getDay()):"0" + Integer.toString(getDay());
		String yearString = getYear() != 0? Integer.toString(getYear()):"0000";
		return monthString + "-" + dayString+ "-" + yearString;
	}
	/**
	 * Determines if a date is valid between 1900 and current date
	 * @return
	 */
	public boolean checkValid() {
		if(this.month<1||this.month>12) return false;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
		LocalDateTime now = LocalDateTime.now();
		int currYear = Integer.parseInt(dtf.format(now));
		if(this.year < 1900||this.year > currYear) return false;
		if(this.day<1) return false;
		switch(this.month) {
		case 2:
			if(this.day > 28) return false;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if(this.day > 30) return false;
			break;
		default:
			if(this.day>31) return false;
			break;
		}
		return true;
	}
	/**
	 * Compares two dates according to compare() conventions
	 * @param date
	 * @return int compare
	 */
	public int compareDate(Date date) {
		if(this.year > date.year) return -1;
		if(this.year < date.year) return 1;
		if(this.month > date.month) return -1;
		if(this.month < date.month) return 1;
		if(this.day > date.day) return -1;
		if(this.day < date.day) return 1;
		return 0;
	}
}
