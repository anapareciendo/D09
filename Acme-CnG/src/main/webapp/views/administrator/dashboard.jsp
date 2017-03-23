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
			<spring:message code="dashboard.minRecipeUser"/>: <jstl:out value="${minRecipeUser}"/><br>
			<spring:message code="dashboard.maxRecipeUser"/>: <jstl:out value="${maxRecipeUser}"/><br>
			<spring:message code="dashboard.avgRecipeUser"/>: <jstl:out value="${avgRecipeUser}"/><br>
			
			<spring:message code="dashboard.userMaxAuthored"/>: 
			<jstl:forEach var="row" items="${userMaxAuthored}">
			<jstl:out value="${row.name}"/>	
			</jstl:forEach><br>
			
			
			<spring:message code="dashboard.minRecipeForContest"/>: <jstl:out value="${minRecipeForContest}"/><br>
			<spring:message code="dashboard.maxRecipeForContest"/>: <jstl:out value="${maxRecipeForContest}"/><br>
			<spring:message code="dashboard.avgRecipeForContest"/>: <jstl:out value="${avgRecipeForContest}"/><br>

			<spring:message code="dashboard.contestMoreRecipe"/>: 
			<jstl:forEach var="row" items="${contestMoreRecipe}">
			<jstl:out value="${row.title}"/>
			</jstl:forEach><br>

			<spring:message code="dashboard.avgStepRecipe"/>: <jstl:out value="${avgStepRecipe}"/><br>
			<spring:message code="dashboard.stdStepRecipe"/>: <jstl:out value="${stdStepRecipe}"/><br>
			
			<spring:message code="dashboard.avgNumberIngredient"/>: <jstl:out value="${avgNumberIngredient}"/><br>
			<spring:message code="dashboard.stdNumberIngredient"/>: <jstl:out value="${stdNumberIngredient}"/><br>

			<spring:message code="dashboard.userPopularity"/>: 
			<jstl:forEach var="row" items="${userPopularity}">
			<jstl:out value="${row.name}"/>
			</jstl:forEach><br>
			<spring:message code="dashboard.userNumberOpinion"/>: 
			<jstl:forEach var="row" items="${userNumberOpinion}">
			<jstl:out value="${row.name}"/>
			</jstl:forEach><br>
			</p>
	</div>
			
			<div>
		<h2><spring:message code="dashboard.titleSponsor"/> </h2>
		<p>
			<spring:message code="dashboard.minNumberCampaign"/>: <jstl:out value="${minNumberCampaign}"/><br>
			<spring:message code="dashboard.maxNumberCampaign"/>: <jstl:out value="${maxNumberCampaign}"/><br>
			<spring:message code="dashboard.avgNumberCampaign"/>: <jstl:out value="${avgNumberCampaign}"/><br>
			
			<spring:message code="dashboard.minActiveCampaign"/>: <jstl:out value="${minActiveCampaign}"/><br>
			<spring:message code="dashboard.maxActiveCampaign"/>: <jstl:out value="${maxActiveCampaign}"/><br>
			<spring:message code="dashboard.avgActiveCampaign"/>: <jstl:out value="${avgActiveCampaign}"/><br>
			
			<spring:message code="dashboard.companiesPerCampaigns"/>: <jstl:out value="${companiesPerCampaigns}"/><br>
			
			<spring:message code="dashboard.companiesPerMonthly"/>: <jstl:out value="${companiesPerMonthly}"/><br>
			
			<spring:message code="dashboard.avgMBillPaid"/>: <jstl:out value="${avgMBillPaid}"/><br>
			<spring:message code="dashboard.stdMBillPaid"/>: <jstl:out value="${stdMBillPaid}"/><br>
			
			<spring:message code="dashboard.avgMBillUnpaid"/>: <jstl:out value="${avgMBillUnpaid}"/><br>
			<spring:message code="dashboard.stdMBillUnpaid"/>: <jstl:out value="${stdMBillUnpaid}"/><br>

			<spring:message code="dashboard.sponsorLastMonth"/>: 
			<jstl:forEach var="row" items="${sponsorLastMonth}">
			<jstl:out value="${row.name} ${row.surname} "/>
			</jstl:forEach><br>
			
			<spring:message code="dashboard.companiesLess"/>: <jstl:out value="${companiesLess}"/><br>
			
			<spring:message code="dashboard.companiesLess90"/>: <jstl:out value="${companiesLess90}"/><br>
		</p>
	</div>
			
			<div>
		<h2><spring:message code="dashboard.titleCook"/> </h2>
		<p>
		
			<spring:message code="dashboard.minMasterClass"/>: <jstl:out value="${minMasterClass}"/><br>
			<spring:message code="dashboard.maxMasterClass"/>: <jstl:out value="${maxMasterClass}"/><br>
			<spring:message code="dashboard.avgMasterClass"/>: <jstl:out value="${avgMasterClass}"/><br>
			
			<spring:message code="dashboard.avgLearning"/>: <jstl:out value="${avgLearning}"/><br>

			<spring:message code="dashboard.masterClassPromoted"/>: <jstl:out value="${masterClassPromoted}"/><br>

			<spring:message code="dashboard.listPerMasterClass"/>: 
			<jstl:forEach var="row" items="${listPerMasterClass}">
			<jstl:out value="${row.name} ${row.surname} "/>
			</jstl:forEach><br>
			<spring:message code="dashboard.avgDemoted"/>: <jstl:out value="${avgDemoted}"/><br>
			
			<spring:message code="dashboard.avgPromoted"/>: <jstl:out value="${avgPromoted}"/><br>
			
		
		</p>
	</div>
			
		
