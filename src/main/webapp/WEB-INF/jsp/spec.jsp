<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <script>
        function clickimage(i){
            var div1 =  document.getElementsByClassName("items");
            for(var loop = 0; loop < div1.length; loop++){
                div1[loop].style.display = "none";
                div1[loop].style.visibility = "hidden";
            }
            var target = document.getElementById(i);
            target.style.visibility = "visible";
            target.style.display = "flex";
        }

        function modify(i){

        }
    </script>
    <link rel="stylesheet" href="resource/css/board.css" type="text/css">
</head>

<body>
    <div>
        <p>캐릭터명: ${player.characterName}</p>
        <p>주스탯: ${player.mainstat}, 부스탯1: ${player.substat1}, 부스탯2: ${player.substat2}</p>
        <p>주스탯퍼: ${player.mainstatPercent}, 부스탯1퍼: ${player.substat1Percent}, 부스탯2퍼: ${player.substat2Percent}, 올스탯퍼: ${player.allstatPercent}</p>
        <p>공마: ${player.attmag}, 공마퍼: ${player.attmagPercent}</p>
        <table>
            <tr style="height:0px">
                <c:set var="itemIndex" value="0"/>
                <c:forEach begin="0" end="29" var="count" varStatus="status">
                    <c:if test="${count % 5 == 0}">
                        </tr><tr class="itemTable">
                    </c:if>
                    <c:choose>
                        <c:when test="${count == 1 || count == 3 || count == 8 || count == 25 || count == 26}">
                            <td class="itemTable"> </td>
                        </c:when>
                        <c:otherwise>
                            <td class="itemTable"><img src="${player.equipeditem[itemIndex].itemImg}" onclick="clickimage('item' + '${itemIndex}');"></td>
                            <c:set var="itemIndex" value="${itemIndex + 1}"/>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>
    </div>
    <c:forEach begin="0" end="24" var="count" varStatus="status">
        <div class="items" id="item${count}">
            <div id="itemDescription">
            <p>아이템명: ${player.equipeditem[count].itemName}</p>
            <p>제한레벨: ${player.equipeditem[count].reqLev}</p>
            <p>주스탯: ${player.equipeditem[count].mainstat}</p>
            <p>부스탯1: ${player.equipeditem[count].substat1}</p>
            <p>부스탯2: ${player.equipeditem[count].substat2}</p>
            <p>주스탯퍼: ${player.equipeditem[count].mainstatPercent}</p>
            <p>부스탯1퍼: ${player.equipeditem[count].substat1Percent}</p>
            <p>부스탯2퍼: ${player.equipeditem[count].substat2Percent}</p>
            <p>올스탯퍼: ${player.equipeditem[count].allstatPercent}</p>
            <p>공격력/마력: ${player.equipeditem[count].attmag}</p>
            <p>공격력퍼/마력퍼: ${player.equipeditem[count].attmagPercent}</p>
            <p>크리티컬 데미지: ${player.equipeditem[count].critDMG}</p>
            <p>보스 몬스터 공격시 데미지: ${player.equipeditem[count].bossDMG}</p>
            <p>몬스터 방어율 무시: ${player.equipeditem[count].penetrate}</p>
            <form action="/spec/${count}" method="get">
                <button type="submit">수정</button>
            </form>
            <%-- TODO: If user modify items, these item's border color will change to red --%>
            <%-- TODO: If user press the button, change items image. Finally 'apply' button will recalculate stats. --%>
            <%-- TODO: 'Compare to previous stats' function will useful. --%>
            </div>
        </div>
    </c:forEach>
</body>

</html>