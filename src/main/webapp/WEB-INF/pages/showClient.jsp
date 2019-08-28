<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>show client for admin</title>
</head>
<body>


	<div align="center">

		<h1>The client of a Chat</h1>
		<h2>
			<a>${client.getData()}</a>
		</h2>
		<table>
			<tr>
				<th><a href="<c:url value="/home" />">Home</a></th>
				<th><a href="<c:url value="/logout?logout=true" />">Logout</a></th>
			</tr>
		</table>
		<!-- use param.error assuming FormLoginConfigurer#failureUrl contains the query parameter error -->

		<c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
                  Reason: <c:out
				value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
		</c:if>
		<c:if test="${error != null}">
			<div style="color: red; font-style: italic">${error.getCause()}</div>
		</c:if>
        <form id="addTopic" action="/Chat/client/addTopic" method="post">
         <input type="hidden" name="idClient" value="${client.getId()}">
            <input name="nameOfTopic"  placeholder="name od Topic"> <input type="submit" value="addTopic" style="width: 85px;">
        </form>

		<table border="1">
			<tr>

				<th>Topics</th>
				<th style="width: 150px;">Count of message</th>
				<th style="width: 150px;">last date</th>
			</tr>
			<c:forEach var="topic" items="${topics}">
				<tr>

					<td><a href="${topic.getLink()}">${topic.getNameOfTopic()}</a></td>
					<td><label>${topic.getHistoriesSize()}</label><br></td>
					<td>${topic.getDate()}</td>
        <td></form>
                    <form id="removeTopic" action="/Chat/client/deleteTopic" method="post">
                    <input type="hidden" name="idTopic" value="${topic.getId()}">
                    <input type="submit" value="removeTopic" style="width: 110px;">
        </form></td>
				</tr>
			</c:forEach>
		</table>
		
		
	</div>
</body>
</html>