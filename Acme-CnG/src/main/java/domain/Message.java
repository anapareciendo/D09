package domain;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	//----------------------Attributes-------------------------
	private Date moment;
	private String title;
	private String text;
	private Collection<String> attachments;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyy HH:mm")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment){
		this.moment=moment;
	}
	
	@NotNull
	public String getTitle() {
		return title;
	}
	public void setTitle(String title){
		this.title=title;
	}
	
	@NotNull
	public String getText() {
		return text;
	}
	public void setText(String text){
		this.text=text;
	}
	
	@ElementCollection
	@NotNull
	public Collection<String> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(Collection<String> attachments) {
		this.attachments = attachments;
	}


	//---------------------Relationships--------------------------
	private Actor sender;
	private Actor recipient;
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Actor getSender() {
		return this.sender;
	}
	public void setSender(Actor sender){
		this.sender=sender;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Actor getRecipient() {
		return recipient;
	}
	public void setRecipient(Actor recipient){
		this.recipient=recipient;
	}
}
