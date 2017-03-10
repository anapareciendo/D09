
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends Commentable {

	//	-------------------Attributes----------------------------------------
	private String	name;
	private String	surname;
	private String	email;
	private String	phone;


	@NotBlank
	@SafeHtml
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@NotBlank
	@Email
	@SafeHtml
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Pattern(regexp = "([+][0-9]{3})[ ]*([(][0-9]{3}[)])?[ ]*([0-9a-zA-Z][ -]*){4,}")
	@SafeHtml
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	//----------------Relationships------------------------------------------
	private Collection<Comment>	postComments;
	private Collection<Message> senderMessages;
	private Collection<Message> receivedMessages;
	private UserAccount		userAccount;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "posted")
	public Collection<Comment> getPostComments() {
		return postComments;
	}
	public void setPostComments(Collection<Comment> postComments) {
		this.postComments = postComments;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "sender")
	public Collection<Message> getSenderMessages() {
		return senderMessages;
	}
	
	public void setSenderMessages(Collection<Message> senderMessages) {
		this.senderMessages = senderMessages;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "recipient")
	public Collection<Message> getReceivedMessages() {
		return receivedMessages;
	}
	
	public void setReceivedMessages(Collection<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}
	
	@Valid
	@NotNull
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
}
