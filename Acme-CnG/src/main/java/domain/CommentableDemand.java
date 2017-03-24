
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public abstract class CommentableDemand extends DomainEntity {

	//	-------------------Attributes----------------------------------------
	private Collection<Comment> comments;
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "commentableDemand")
	public Collection<Comment> getComments() {
		return comments;
	}
	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}


}
