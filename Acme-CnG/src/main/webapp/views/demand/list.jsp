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

<display:table name="demand" id="row" requestURI="${requestURI }"
	pagesize="5" class="displaytag">
	
	<%-- <display:column>
	  	<div>
	  	<a href="message/delete.do?messageId=${row.id}">
	  		<spring:message code="message.delete" var="deleteHeader" />
	  		<jstl:out value="${deleteHeader}" />
	  	</a>
	  	</div>
  	</display:column> --%>
	
	<spring:message code="demand.title" var="titletHeader" />
	<display:column property="title" title="${titleHeader }" sortable="true" />
	
	<spring:message code="demand.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader }" sortable="true" />
	
	<spring:message code="demand.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader }" sortable="false" />
	
	<spring:message code="demand.banned" var="bannedHeader" />
	<display:column property="banned" title="${bannedHeader }" sortable="true" />
	
	<spring:message code="demand.origin" var="originHeader" />
	<display:column title="${originHeader }" sortable="false">
		<jstl:out value="${row.origin.address}" />
	</display:column>
	
	<spring:message code="demand.destination" var="destinationHeader" />
	<display:column title="${destinationHeader }" sortable="false">
		<jstl:out value="${row.destination.address}" />
	</display:column>
	
	
</display:table>

