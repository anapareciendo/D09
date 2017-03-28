<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="images/logo.png" alt="Acme-CnG Co., Inc." />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>									
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="security/signin.do"><spring:message code="master.page.signin" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message code="master.page.message" /></a>
				<ul>
					<li class="arrow"></li>
						<li><a href="message/list.do"><spring:message code="master.page.message.list" /></a></li>
						<li><a href="message/send.do"><spring:message code="master.page.message.send" /></a></li>
				</ul>
			</li>
			
			<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message code="master.page.demand" /></a>
				<ul>
					<li class="arrow"></li>
						<li><a href="offer/create.do"><spring:message code="master.page.offer.create" /></a></li>
						<li><a href="request/create.do"><spring:message code="master.page.request.create" /></a></li>
						<li><a href="demand/customer/offer.do"><spring:message code="master.page.offer.apply" /></a></li>
						<li><a href="demand/customer/request.do"><spring:message code="master.page.request.apply" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message code="master.page.application" /></a>
				<ul>
					<li class="arrow"></li>
						<li><a href="application/customer/offer.do"><spring:message code="master.page.app.offer" /></a></li>
						<li><a href="application/customer/request.do"><spring:message code="master.page.app.request" /></a></li>
				</ul>
			</li>
			</security:authorize>
			
			<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message code="master.page.banner" /></a>
				<ul>
					<li class="arrow"></li>
						<li><a href="banner/edit.do"><spring:message code="master.page.banner.edit" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.demand" /></a>
				<ul>
					<li class="arrow"></li>
						<li><a href="demand/admin/list.do"><spring:message code="master.page.demand.list" /></a></li>
				</ul>
			</li>
			</security:authorize>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

