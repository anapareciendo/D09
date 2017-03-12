package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Actor;
import domain.Comment;
import domain.Commentable;

@Service
@Transactional
public class CommentService {

	//Managed repository
	@Autowired
	private CommentRepository commentRepository;
	

	
	/*@Autowired
	private Validator validator;*/
	
	//Constructors
	public CommentService() {
		super();
	}
	
	//Simple CRUD methods
	public Comment create(Actor sender, Commentable commentable) {
		Assert.notNull(sender, "The sender cannot be null");
		Assert.notNull(commentable, "The recipient cannot be null");
		Comment res;
		res = new Comment();
		res.setCommentable(commentable);
		res.setPosted(sender);
		res.setMoment(Calendar.getInstance().getTime());
		res.setStars(1);
		
		return res;
	}
	
	public Collection<Comment> findAll() {
		Collection<Comment> res = commentRepository.findAll();
		return res;
	}

	public Comment findOne(int commentId) {
		Comment res = commentRepository.findOne(commentId);
		return res;
	}
	
	public Comment save(Comment comment) {
		Assert.notNull(comment, "The comment to save cannot be null.");
		Assert.isTrue(comment.getStars()>0&&comment.getStars()<=5,"The stars must be between 1 and 5");
		Comment res = commentRepository.save(comment);
		res.setMoment(Calendar.getInstance().getTime());
		
		return res;
	}
	

	
	
	

}

