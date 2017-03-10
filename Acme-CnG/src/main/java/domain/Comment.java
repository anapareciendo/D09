
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	//----------------------Attributes-------------------------
	private String	title;
	private Date	moment;
	private String	text;
	private int	stars;
	private Boolean banned;


	@NotNull
	@SafeHtml
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotNull
	@SafeHtml
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Range(min = 0, max = 5)
	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}
	
	@NotNull
	public Boolean getBanned() {
		return banned;
	}

	
	public void setBanned(Boolean banned) {
		this.banned = banned;
	}



	//---------------------Relationships--------------------------
	private Actor posted;
	private Commentable commentable;

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Actor getPosted() {
		return this.posted;
	}
	public void setPosted(Actor posted){
		this.posted=posted;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Commentable getCommentable() {
		return this.commentable;
	}
	public void setCommentable(Commentable commentable){
		this.commentable=commentable;
	}
	
	
}
