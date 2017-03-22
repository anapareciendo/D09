<%--
 * display.jsp
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

<display:table name="ms" id="row" requestURI="${requestURI }"
	pagesize="5" class="displaytag">
	
	<display:column>
	  	<div>
	  	<a href="message/delete.do?messageId=${row.id}">
	  		<spring:message code="message.delete" var="deleteHeader" />
	  		<jstl:out value="${deleteHeader}" />
	  	</a>
	  	</div>
  	</display:column>
	
	<spring:message code="message.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader }" sortable="true" />
	
	<spring:message code="message.title" var="titletHeader" />
	<display:column property="title" title="${titleHeader }" sortable="false" />
	
	<spring:message code="message.text" var="textHeader" />
	<display:column property="text" title="${textHeader }" sortable="false" />
	
	<spring:message code="message.recipient" var="recipientHeader" />
	<display:column title="${recipientHeader }" sortable="false">
		<jstl:out value="${row.recipient.userAccount.username}" />
	</display:column>
	
	
</display:table>

