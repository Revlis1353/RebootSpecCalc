<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
    <link rel="stylesheet" href="resource/css/board.css" type="text/css">
</head>

<body>
    <div>
        <p>캐릭터명: ${player.characterName}</p>
        <p>주스탯: ${player.mainstat}, 부스탯1: ${player.substat1}, 부스탯2: ${player.substat2}</p>
        <p>주스탯퍼: ${player.mainstatPercent}, 부스탯1퍼: ${player.substat1Percent}, 부스탯2퍼: ${player.substat2Percent}, 올스탯퍼: ${player.allstatPercent}</p>
        <p>공마: ${player.attmag}, 공마퍼: ${player.attmagPercent}</p>
    </div>
</body>

</html>