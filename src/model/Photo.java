package model;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;

public class Photo implements Serializable{
	
	/**
	 * name of directory to store data
	 */
	public static final String storeDir = "dat";
	
	/**
	 * name of file to store data
	 */
	public static final String storeFile = "serialization.dat";
	
	
	static final long serialVersionUID = 1L ;
	
	/**
	 * list of tags for the photo
	 */
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * caption of the photo
	 */
	private String caption;
	
	/**
	 * filepath of the photo
	 */
	private File filepath;
	
	/**
	 * date of the photo
	 */
	private Date date;
	
	/**
	 * name of the photo
	 */
	private String photoName;
	
	/**
	 * Photo constructor
	 * @param filepath the photos filepath
	 * @param caption caption of the photo
	 * @param date date of the photo
	 */
	public Photo(File filepath, String caption, Date date) {
		this.filepath = filepath;
		this.caption = caption;
		this.date = date;
		this.tags = new ArrayList<Tag>();
	}
	
	/**
	 * Photo constructor
	 * @param filepath the photos filepath
	 */
	public Photo(File filepath) {
		this.filepath = filepath;
		this.date = new Date(this.filepath.lastModified());
	}
	
	/**
	 * Photo constructor
	 * @param photoName the name of the photo
	 */
	public Photo(String photoName) {
		this.photoName = photoName;
		//this.date = new Date(this.filepath.lastModified());
	}

	/**
	 * gets the list of tags of a photo
	 * @return returns arraylist of tags for the photo
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}

	/**
	 * sets the list of tags for a photo
	 * @param tags list of tags
	 */
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * gets the caption of the photo
	 * @return returns caption of the photo
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * sets the caption of the photo
	 * @param caption caption to be set for the photo
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * gets the filepath of the photo
	 * @return filepath filepath of the photo
	 */
	public File getFilepath() {
		//Image image = new Image(filepath.toString());
		return filepath;
	}

	/**
	 * sets the filepath of the photo
	 * @param filepath filepath that will be set for the photo
	 */
	public void setFilepath(File filepath) {
		this.filepath = filepath;
	}

	/**
	 * gets the date of the photo
	 * @return date of the photo
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * sets the date of the photo
	 * @param date photo will be set to this date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * adds a tag to this photo
	 * @param tag tag that will be added to the list of tags for the photo
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * removes a tag for this photo
	 * @param tag tag that will be removed from the list of tags for the photo
	 */
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}
	
}
