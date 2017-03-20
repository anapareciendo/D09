
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;
import domain.Offer;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c where c.banned=false and c.commentable.id=?1")
	Collection<Comment> findReceivedComments(int id);

	@Query("select c from Comment c where c.banned=false")
	Collection<Offer> findNoBannedComments();

}
