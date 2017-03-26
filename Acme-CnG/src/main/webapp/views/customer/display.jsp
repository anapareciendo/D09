<%--
 * list.jsp
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

	<div>
		<h2>
			<jstl:out value="${actor.name}" />
	  		<jstl:out value="${actor.surname}" />
		</h2>
		<h4><jstl:out value="${actor.email}" /></h4>
		<h4><jstl:out value="${actor.phone}" /></h4>
	</div>
	
	<display:table name="comments" id="comment">	
		<spring:message code="demand.comment.autor" var="autorHeader" />
		<display:column property="posted.userAccount.username" title="${autorHeader}" sortable="false"/>
		
		<spring:message code="demand.comment.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}" sortable="false"/>
		
		<spring:message code="demand.comment.text" var="textHeader" />
		<display:column property="text" title="${textHeader}" sortable="false"/>
		
		<spring:message code="demand.comment.moment" var="momentHeader" />
		<display:column property="moment" title="${momentHeader}" sortable="false"/>
		
		<spring:message code="demand.comment.stars" var="starsHeader" />
		<display:column property="stars" title="${starsHeader}" sortable="false"/>
		
		<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${comment.banned == false}">
			<div>
	  			<a href="comment/admin/ban.do?commentId=${comment.id}">
					<spring:message code="demand.comment.ban" var="banHeader" />
	  				<jstl:out value="${banHeader}" />
	  			</a>
	  		</div>
	  		</jstl:if>
		</display:column>
		</security:authorize>
	</display:table>
	
	<security:authorize access="hasRole('CUSTOMER')">
	<div>
	  	<a href="comment/customer/post.do?commentableId=${actor.id}">
			<spring:message code="demand.comment.post" var="postHeader" />
	  		<jstl:out value="${postHeader}" />
		</a>
	</div>
	</security:authorize>
