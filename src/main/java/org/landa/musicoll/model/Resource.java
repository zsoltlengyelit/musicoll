package org.landa.musicoll.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "resource")
public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3983097817374737960L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 255)
	private String title;

	@Column(length = 255)
	private String art;

	@Column(length = 255)
	private String instrument;

	@Column(length = 255)
	private String region;

	@Column(length = 255)
	private String place;

	private boolean trans;

	@Column(length = 500)
	private String artist;

	@Column(length = 500)
	private String collector;

	@Column(length = 255)
	private String collectionTime;

	@Lob
	private String note;

	@NotNull
	private String relativePath;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(final String relativePath) {
		this.relativePath = relativePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public boolean isTrans() {
		return trans;
	}

	public void setTrans(boolean trans) {
		this.trans = trans;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((getRelativePath() == null) ? 0 : getRelativePath()
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Resource other = (Resource) obj;
		if (getRelativePath() == null) {
			if (other.getRelativePath() != null) {
				return false;
			}
		} else if (!getRelativePath().equals(other.getRelativePath())) {
			return false;
		}
		return true;
	}

}
