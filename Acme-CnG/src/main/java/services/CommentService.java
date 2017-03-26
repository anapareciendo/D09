
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Comment;
import domain.Commentable;
import domain.Customer;

@Service
@Transactional
public class CommentService {

	//Managed repository
	@Autowired
	private CommentRepository	commentRepository;


	//Suppoert services
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private DemandService demandService;
	
	@Autowired
	private Validator validator;
	
	//Constructors
	public CommentService() {
		super();
	}

	//Simple CRUD methods
	public Comment create(final Actor sender, final Commentable commentable) {
		Assert.notNull(sender, "The sender cannot be null");
		Assert.notNull(commentable, "The recipient cannot be null");
		Comment res;
		res = new Comment();
		res.setCommentable(commentable);
		res.setPosted(sender);
		res.setMoment(Calendar.getInstance().getTime());
		res.setBanned(false);
		res.setStars(0);

		return res;
	}

	public Comment findOne(final int commentId) {
		final Comment res = this.commentRepository.findOne(commentId);
		return res;
	}

	public Comment save(final Comment comment) {
		Assert.notNull(comment, "The comment to save cannot be null.");
		Assert.notNull(comment.getPosted());

		Assert.isTrue(comment.getMoment() != null);
		Assert.isTrue(comment.getTitle() != null);
		Assert.isTrue(comment.getText() != null);
		Assert.isTrue(comment.getStars() >= 0 && comment.getStars() <= 5, "The stars must be between 1 and 5");

		final Comment res = this.commentRepository.save(comment);
		res.getPosted().getComments().add(res);
		res.getCommentable().getComments().add(res);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment, "The comment to delete cannot be null.");
		Assert.isTrue(this.commentRepository.exists(comment.getId()));

		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(comment.getPosted().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the comment");

		this.commentRepository.delete(comment);
	}

	//Utility Methods
	public void ban(final int commentId) {
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be an Admin for this action");

		final Comment comment = this.commentRepository.findOne(commentId);
		Assert.notNull(comment, "This id is not from a comment");

		comment.setBanned(true);
		this.commentRepository.save(comment);
	}

	public Collection<Comment> findReceivedComments(final int id) {
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b) || ua.getAuthorities().contains(a), "You must to be an autenticated for this action");

		return this.commentRepository.findReceivedComments(id);
	}

	public Collection<Comment> findMyCommentsAbout(int demandId){
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		final Authority c = new Authority();
		c.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(c), "You must to be autenticate for this action");

		return this.commentRepository.findMyCommentsAbout(ua.getId(), demandId);
	}

	public Comment reconstruct(Comment comment, BindingResult binding) {
		
		Customer posted= customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		
		Comment res = this.create(posted, comment.getCommentable());
		res.setTitle(comment.getTitle());
		res.setText(comment.getText());
		res.setStars(comment.getStars());
		
		validator.validate(comment, binding);
		
		return res;
	}
	
	public Set<Comment> findRealComments(int commentableId){
		Set<Comment> comments = new HashSet<Comment>();
		Commentable commentable = demandService.findOne(commentableId);
		if(commentable==null){
			commentable = customerService.findOne(commentableId);
		}
		if(commentable==null){
			commentable = adminService.findOne(commentableId);
		}
		
		int uaId = LoginService.getPrincipal().getId();
		Actor actor = adminService.findByUserAccountId(uaId);
		if(actor!=null){
			comments.addAll(commentable.getComments());
		}else{
			actor = customerService.findByUserAccountId(uaId);
			comments.addAll(commentRepository.findReceivedComments(commentableId));
			List<Comment> myComments = new ArrayList<Comment>();
			myComments.addAll(commentRepository.findMyCommentsAbout(uaId, commentableId));
			if(!myComments.isEmpty()){
				comments.addAll(myComments);
			}
		}
		
		return comments;
	}

}
