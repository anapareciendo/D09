
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
	
	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByUserAccountId(int id);
	
	//Level C
	
	//Ratio of offers versus requests
	//select o from Offer o;
	//select r from Request r;
	
	//Average number of offers and request per customer.
	//select avg(c.offers.size) from Customer c;
	//Select avg(c.requests.size) from Customer c;
	
	//Average number of applications per offer or request.
	//select avg(a.demand.size) from Application a;
	
	//The customer who has more applications accepted
	//select distinct(d.customer) from Demand d join d.application a where d.application.size = (select max(d.application.size) from Demand d join d.application a where a.status = 1) AND r.status = 1
	
	//The customer who has more applications denied.
	//Probar la de arriba
	
	//Level B
	
	//Average number of comments per actor
	@Query("select avg(a.comments.size) from Actor a")
	Double avgCommentPerActor();
	
	
	//Average number of comments per offer
	//Average number of comments per request
	//Probar la primera
	
	// Average number of comments posted by administrators
	@Query("select avg(a.postComments.size) from Administrator a")
	Double avgCommentPostAdmin();
	
	// Average number of comments posted by customer
	@Query("select avg(c.postComments.size) from Customer c")
	Double avgCommentPostCustomer();
	
	//The actors who have posted ±10% the average number of comments per actor
	
	//Level A
	
	//The minimum, the average, and the maximum number of messages sent per actor
	@Query("select min(a.senderMessages.size) from Actor a")
	Double minSentMessagePerActor();
	
	//The maximum number of messages sent per actor
	@Query("select max(a.senderMessages.size) from Actor a")
	Double maxSentMessagePerActor();
	
	//The average of messages sent per actor
	@Query("select avg(a.senderMessages.size) from Actor a")
	Double avgSentMessagePerActor();
	
	//The minimum, the average, and the maximum number of messages received per actor.
	@Query("select min(a.receivedMessages.size) from Actor a")
	Double minReciveMessagePerActor();
	
	//The maximum number of messages received per actor.
	@Query("select max(a.receivedMessages.size) from Actor a")
	Double maxReciveMessagePerActor();
	
	//The average of messages received per actor.
	@Query("select avg(a.receivedMessages.size) from Actor a")
	Double avgReciveMessagePerActor();
	
	
	//The actors who have sent more messages
	@Query("select distinct(m.sender) from Message m join m.sender a where a.senderMessages.size = (select max(a.senderMessages.size) from Actor a)")
	Collection<Actor> actorsSentMoreMessages();
	
	//The actors who have got more messages
	@Query("select distinct(m.sender) from Message m join m.sender a where a.receivedMessages.size = (select max(a.receivedMessages.size) from Actor a)")
	Collection<Actor> actorsGotMoreMessages();
	
	
}
