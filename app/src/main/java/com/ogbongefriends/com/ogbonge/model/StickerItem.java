package com.ogbongefriends.com.ogbonge.model;

import java.io.Serializable;

public class StickerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id, image, extension, category, time;

	private boolean isOnSd;

	public boolean isOnSd() {
		return isOnSd;
	}

	public void setOnSd(boolean isOnSd) {
		this.isOnSd = isOnSd;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
