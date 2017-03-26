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
		<h2><spring:message code="dashboard.titleUser"/> </h2>
		<p>
			<spring:message code="dashboard.ratioDemand"/>: <jstl:out value="${ratioDemand}"/><br>
			<spring:message code="dashboard.avgOffersCustomer"/>: <jstl:out value="${avgOffersCustomer}"/><br>
			<spring:message code="dashboard.avgRequestsCustomer"/>: <jstl:out value="${avgRequestsCustomer}"/><br>
			<spring:message code="dashboard.avgApplicationsOffer"/>: <jstl:out value="${avgApplicationsOffer}"/><br>
			<spring:message code="dashboard.avgApplicationsRequest"/>: <jstl:out value="${avgApplicationsRequest}"/><br>
			<spring:message code="dashboard.customerMoreApplicationAccepted"/>: <jstl:out value="${customerMoreApplicationAccepted}"/><br>
			<spring:message code="dashboard.customerMoreApplicationDenied"/>: <jstl:out value="${customerMoreApplicationDenied}"/><br>
			<spring:message code="dashboard.avgCommentPerActor"/>: <jstl:out value="${avgCommentPerActor}"/><br>
			<spring:message code="dashboard.avgCommentPerOffer"/>: <jstl:out value="${avgCommentPerOffer}"/><br>
			<spring:message code="dashboard.avgCommentPerRequest"/>: <jstl:out value="${avgCommentPerRequest}"/><br>
			<spring:message code="dashboard.avgCommentPostAdmin"/>: <jstl:out value="${avgCommentPostAdmin}"/><br>
			<spring:message code="dashboard.avgCommentPostCustomer"/>: <jstl:out value="${avgCommentPostCustomer}"/><br>
			<spring:message code="dashboard.minSentMessagePerActor"/>: <jstl:out value="${minSentMessagePerActor}"/><br>
			<spring:message code="dashboard.maxSentMessagePerActor"/>: <jstl:out value="${maxSentMessagePerActor}"/><br>
			<spring:message code="dashboard.avgSentMessagePerActor"/>: <jstl:out value="${avgSentMessagePerActor}"/><br>
			<spring:message code="dashboard.minReciveMessagePerActor"/>: <jstl:out value="${minReciveMessagePerActor}"/><br>
			<spring:message code="dashboard.maxReciveMessagePerActor"/>: <jstl:out value="${maxReciveMessagePerActor}"/><br>
			<spring:message code="dashboard.avgSentMessagePerActor"/>: <jstl:out value="${avgReciveMessagePerActor}"/><br>
			
			
			<spring:message code="dashboard.actorsSentMoreMessages"/>:<br> 
			<jstl:forEach var="row" items="${actorsSentMoreMessages}">
			<jstl:out value="${row.name}"/>	<br>
			</jstl:forEach><br>
			
			<spring:message code="dashboard.actorsGotMoreMessages"/>:<br> 
			<jstl:forEach var="row" items="${actorsGotMoreMessages}">
			<jstl:out value="${row.name}"/>	<br>
			</jstl:forEach><br>
			
			
		</p>
	</div>
			