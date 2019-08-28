<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>"${topic.getNameOfTopic()}"</title>
</head>
<body>
<div align="center">
<h2>${topic.getNameOfTopic()} </h2>
	<table>
            <tr>
                <th><a href="<c:url value="/home" />">Home</a></th>
                <th><a href="<c:url value="/logout?logout=true" />">Logout</a></th>
            </tr>
        </table>
	<c:if test="${error != null}">
		<div style="color: red; font-style: italic">${error.getCause()}</div>
	</c:if>
	<c:if test="${dataTransfer == null}">
	<table border="1">
            <tr>

                <th>Message</th>
                <th style="width: 154px;">Author</th>
                <th style="width: 154px;">Date</th>
                <th style="width: 154px;">Edit</th>
            </tr>
            <c:forEach var="message" items="${topic.getSortHistories()}">
                <tr>

                    <td>${message.getStory()}</td>
                    <td>${message.getClientName()}</td>
                    <td><label>${message.getDate()}</label><br></td>
 <td></form>
        <form id="removeMessage" action="/Chat/client/removeMessage" method="post">
         <input type="hidden" name="idTopic" value="${topic.getId()}">
         <input type="hidden" name="idMessage" value="${message.getId()}">
             <input type="submit" value="removeMessage" style="width: 110px;">
        </form></td>
                </tr>
            </c:forEach>
        </table>
	
	
	
		<form id="sendMessage" action="/Chat/client/topicAddMessage"
			method="post">
			<input type="hidden" name="idTopic" value="${topic.getId()}"> <input
				name="story" placeholder="message" style="height: 65px; width: 446px"> <input type="submit"
				value="send" form="sendMessage" style="width: 90px;">
		</form>
	</c:if>

    </div>
</body>
</html>