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

<display:table name="apps" id="row" requestURI="${requestURI }" pagesize="5" class="displaytag">
	
	<spring:message code="application.title" var="titleHeader" />
	<display:column property="demand.title" title="${titleHeader }" sortable="true" />
	
	<spring:message code="application.customer" var="customerHeader" />
	<display:column property="customer.name" title="${customerHeader }" sortable="true" />
	
	<spring:message code="application.status" var="statusHeader" />
	<display:column property="status" title="${statusHeader }" sortable="true" />
	
	<display:column>
  		<jstl:if test="${row.status == 'PENDING'}">
  			<div><a href="application/customer/accept.do?appId=${row.id}">
		  		<spring:message code="application.accept" var="acceptHeader" />
	  			<jstl:out value="${acceptHeader}" />
	  		</a></div>
	  		<div><a href="application/customer/deny.do?appId=${row.id}">
		  		<spring:message code="application.deny" var="denyHeader" />
	  			<jstl:out value="${denyHeader}" />
	  		</a></div>
  		</jstl:if>
	</display:column>
	
	
</display:table>

