
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Administrator;
import domain.Customer;
import domain.Demand;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
	
	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByUserAccountId(int id);
	
	//Level C
	
	//Ratio of offers versus requests
	@Query("select d from Demand d where d.type=0")
	Demand ratioOffer();
	@Query("select d from Demand d where d.type=1")
	Demand ratioRequest();
	
	//Average number of offers per customer.
	@Query("select avg(c.demands.size) from Customer c join c.demands d where d.type=0")
	Double avgOffersCustomer();

	//Average number request per customer.
	@Query("select avg(c.demands.size) from Customer c join c.demands d where d.type=1")
	Double avgRequestsCustomer();
	
	//Average number of applications per offer.
	@Query("select avg(d.applications.size) from Demand d where d.type=0")
	Double avgApplicationsOffer();
	
	//Average number of applications per request.
	@Query("select avg(d.applications.size) from Demand d where d.type=1")
	Double avgApplicationsRequest();
	
	//The customer who has more applications accepted
	@Query("select distinct(d.customer) from Demand d join d.applications a where d.applications.size = (select max(d.applications.size) from Demand d join d.applications a where a.status = 1) AND a.status = 1")
	Customer customerMoreApplicationAccepted();
	
	//The customer who has more applications denied.
	@Query("select distinct(d.customer) from Demand d join d.applications a where d.applications.size = (select max(d.applications.size) from Demand d join d.applications a where a.status = 2) AND a.status = 2")
	Customer customerMoreApplicationDenied();
	
	//Level B
	
	//Average number of comments per actor
	@Query("select avg(a.comments.size) from Actor a")
	Double avgCommentPerActor();
	
	//Average number of comments per offer
	@Query("select avg(d.comments.size) from Demand d where d.type=0")
	Double avgCommentPerOffer();
	
	//Average number of comments per request
	@Query("select avg(d.comments.size) from Demand d where d.type=1")
	Double avgCommentPerRequest();
	
	// Average number of comments posted by administrators
	@Query("select avg(a.postComments.size) from Administrator a")
	Double avgCommentPostAdmin();
	
	// Average number of comments posted by customer
	@Query("select avg(c.postComments.size) from Customer c")
	Double avgCommentPostCustomer();
		
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
	
	//The minimum of messages received per actor.
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
