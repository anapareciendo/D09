
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<jstl:if test="${vacio != true}">
<form:form action="offer/create.do" modelAttribute="offer">

	<acme:textbox code="offer.title" path="title"/>
	<acme:textbox code="offer.description" path="description"/>
	<acme:select items="${places}" itemLabel="address" code="offer.origin" path="origin"/>
	<acme:select items="${places}" itemLabel="address" code="offer.destination" path="destination"/>
	
	<input type="submit" name="save" value="<spring:message code="offer.save" />" />
	<input type="button" name="cancel" value="<spring:message code="offer.cancel" />" onclick="window.location='welcome/index.do'" /> <br />
	
	<div>
		<jstl:out value="${errors}"/>
	</div>
	
</form:form>
</jstl:if>

<jstl:if test="${vacio == true}">
	<spring:message code="offer.places.empty" var="emptyHeader" />
	<jstl:out value="${emptyHeader}" />
</jstl:if>