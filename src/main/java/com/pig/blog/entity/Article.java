package com.pig.blog.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "article")
public class Article implements Serializable {

	private static final long serialVersionUID = -6996106414753458810L;
	
	public Article() {
		super();
	}
	
	/**
	 * OITI
	 * Only id and title.
	 * 
	 * @param id
	 * @param title
	 */
	public Article(Integer id, String title, String imageUrl) {
		super();
		this.id = id;
		this.title = title;
		this.imageUrl = imageUrl;
	}
	
	/**
	 * NCTC
	 * No content, tags and comments.
	 * 
	 * @param id
	 * @param imageUrl
	 * @param introduce
	 * @param title
	 * @param click
	 * @param date
	 * @param isShow
	 */
	public Article(Integer id, String imageUrl, String introduce, String title, Integer click,
			Date date, Boolean isShow) {
		super();
		this.id = id;
		this.imageUrl = imageUrl;
		this.introduce = introduce;
		this.title = title;
		this.click = click;
		this.date = date;
		this.isShow = isShow;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column
	private String imageUrl;

	@Column
	private String introduce;
	
	@Column
	private String title;
	
	@Column
	private String content;

	@Column
	private Integer click;
	
	@Transient
	private Integer commentNumber;

	@Column
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column
	private Boolean isShow;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "article_tag", joinColumns = @JoinColumn(name = "article_id"),
	inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@Fetch(FetchMode.SUBSELECT)
	private Collection<Tag> tags = new ArrayList<Tag>();
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<Comment> comments = new ArrayList<Comment>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}
	
	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Collection<Tag> getTags() {
		return tags;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
