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

<security:authorize access="hasRole('CUSTOMER')">

	<form:form action="demand/customer/search.do">
		<spring:message code="demand.search" />
		
		<input type="hidden" name="type" value="${type }">
		<input type="text" name="keyword">
		
		<spring:message code="demand.go.search" var="goSearchText"/>
		<input type="submit" name="search" value="${goSearchText}"/>
	</form:form>

</security:authorize>


<display:table name="demand" id="row" requestURI="${requestURI }"
	pagesize="5" class="displaytag">
	
	<security:authorize access="hasRole('ADMIN')">
	<display:column>
		<jstl:if test="${row.banned == false}">
	  		<div>
	  			<a href="demand/admin/ban.do?demandId=${row.id}">
					<spring:message code="demand.ban" var="banHeader" />
	  				<jstl:out value="${banHeader}" />
	  			</a>
	  		</div>
	  	</jstl:if>
	</display:column>
	</security:authorize>
	
	<spring:message code="demand.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader }" sortable="true" />
	
	<spring:message code="demand.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader }" sortable="true" />
	
	<spring:message code="demand.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader }" sortable="false" />
	
	<security:authorize access="hasRole('ADMIN')">
	<spring:message code="demand.banned" var="bannedHeader" />
	<display:column property="banned" title="${bannedHeader }" sortable="true" />
	</security:authorize>
	
	<spring:message code="demand.origin" var="originHeader" />
	<display:column title="${originHeader }" sortable="false">
		<jstl:out value="${row.origin.address}" />
	</display:column>
	
	<spring:message code="demand.destination" var="destinationHeader" />
	<display:column title="${destinationHeader }" sortable="false">
		<jstl:out value="${row.destination.address}" />
	</display:column>
	
	<security:authorize access="hasRole('CUSTOMER')">
	<display:column>
		<div>
			<a href="demand/customer/apply.do?demandId=${row.id}">
				<spring:message code="demand.apply" var="appHeader" />
				<jstl:out value="${appHeader}" />
			</a>
		</div>
	</display:column>
	</security:authorize>
	
	<display:column>
	  		<div>
	  			<a href="display/demand.do?demandId=${row.id}">
					<spring:message code="demand.display" var="displayHeader" />
	  				<jstl:out value="${displayHeader}" />
	  			</a>
	  		</div>
	</display:column>
	
</display:table>

