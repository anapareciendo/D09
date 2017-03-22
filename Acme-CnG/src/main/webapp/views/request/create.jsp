
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<jstl:if test="${vacio != true}">
<form:form action="request/create.do" modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="moment"/>

	<acme:textbox code="request.title" path="title"/>
	<acme:textbox code="request.description" path="description"/>
	<acme:checkbox code="request.banned" value="banned" path="banned"/>
	<acme:select items="${places}" itemLabel="address" code="request.origin" path="origin"/>
	<acme:select items="${places}" itemLabel="address" code="request.destination" path="destination"/>
	
	<input type="submit" name="save" value="<spring:message code="request.save" />" />
	<input type="button" name="cancel" value="<spring:message code="request.cancel" />" onclick="window.location='welcome/index.do'" /> <br />
	
	<div>
		<jstl:out value="${errors}"/>
	</div>
	
</form:form>
</jstl:if>

<jstl:if test="${vacio == true}">
	<spring:message code="request.places.empty" var="emptyHeader" />
	<jstl:out value="${emptyHeader}" />
</jstl:if>