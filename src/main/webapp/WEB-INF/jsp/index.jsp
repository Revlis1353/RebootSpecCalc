<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
    <link rel="stylesheet" href="resource/css/board.css" type="text/css">
    <script>
        function ShowBanner(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "visible";
        }
        function HideBanner(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "hidden"; 
            location.reload();
        }
    </script>
    <title></title>
</head>

<body>
    <div id="bannerFull">
        <div id="bannerBackground">
            <div id="bannerBox">
                <h1 id="bannerText">검색중입니다</h1>
                <div id="searchCancel" onclick="HideBanner();">
                    <p id="searchCancelText">취소</p>
                </div>
            </div>  
        </div>
    </div>
    <div id="formCharacter">
        <h1 id="indexTitle">리부트 스펙 계산기</h1>
        <form:form modelAttribute="FindCharacterVO" action="/search" method="post">
            <div>
                <input type="text" name="characterName" id="characterName" placeholder="캐릭터명 입력"/>
            </div>
            <div class="errorouter">
                <form:errors path="characterName" class="error"/>
            </div>
            <div id="formSelect">
                <form:select path="attmagSel" class="indexStatSelector">
                    <form:option value="0" label="공/마 선택"/>
                    <form:options items="${attmagSel}"/>
                </form:select>
                <form:errors path="attmagSel" class="error"/>
                <form:select path="mainstatSel" class="indexStatSelector">
                    <form:option value="0" label="주스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
                <form:errors path="mainstatSel" class="error"/>
                <form:select path="substat1Sel" class="indexStatSelector">
                    <form:option value="0" label="부스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
                <form:errors path="substat1Sel" class="error"/>
                <form:select path="substat2Sel" class="indexStatSelector">
                    <form:option value="0" label="추가 부스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
            </div>
            <button type="submit" name="btnSearch" id="btnSearch" onclick="ShowBanner();">검색</button>
        </form:form>
    </div>
</body>

</html>