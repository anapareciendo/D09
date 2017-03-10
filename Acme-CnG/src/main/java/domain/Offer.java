
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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Offer extends Commentable {

	//----------------------Attributes-------------------------
	private String	title;
	private String	description;
	private Date	moment;
	private Boolean banned;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@SafeHtml
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public Boolean getBanned() {
		return banned;
	}

	
	public void setBanned(Boolean banned) {
		this.banned = banned;
	}



	//---------------------Relationships--------------------------
	private Customer customer;
	private Place place;

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Customer getCustomer() {
		return this.customer;
	}
	public void setCustomer(Customer customer){
		this.customer=customer;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Place getPlacer() {
		return this.place;
	}
	public void setPlacer(Place place){
		this.place=place;
	}
	
	
}
