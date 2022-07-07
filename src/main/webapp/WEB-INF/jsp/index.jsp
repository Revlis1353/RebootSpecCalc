<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
</head>

<body>
    
    <form:form commandName="FindCharacterVO" action="/search" method="post">
        <input type="text" name="characterName"/>
        <input type="submit" name="btnSearch"></input>
    </form:form>
</body>

</html>