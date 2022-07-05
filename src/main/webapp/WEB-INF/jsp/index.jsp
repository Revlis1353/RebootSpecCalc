<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
</head>

<body>
    <!-- https://maplestory.nexon.com/Ranking/World/Total?c=닉네임&w=254 으로 유저 검색 후 크롤링하여 url 획득-->

    <form action="/search" class="search">
        <input type="text" value maxlength="14" required placeholder="캐릭터명 입력">
        <button type="submit">검색</button>
    </form>

</body>

</html>