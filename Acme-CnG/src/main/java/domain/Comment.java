
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "banned")})
public class Comment extends DomainEntity {

	//----------------------Attributes-------------------------
	private String	title;
	private Date	moment;
	private String	text;
	private int		stars;
	private boolean	banned;


	@NotNull
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotNull
	@SafeHtml
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Range(min = 0, max = 5)
	public int getStars() {
		return this.stars;
	}

	public void setStars(final int stars) {
		this.stars = stars;
	}

	public boolean getBanned() {
		return this.banned;
	}

	public void setBanned(final boolean banned) {
		this.banned = banned;
	}


	//---------------------Relationships--------------------------
	private Actor		posted;
	private Commentable commentable;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getPosted() {
		return this.posted;
	}
	public void setPosted(final Actor posted) {
		this.posted = posted;
	}

	
	@Valid
	@ManyToOne(optional = true)
	public Commentable getCommentable() {
		return commentable;
	}
	public void setCommentable(Commentable commentable) {
		this.commentable = commentable;
	}

	
}
