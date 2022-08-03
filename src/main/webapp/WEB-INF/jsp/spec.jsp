<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <script src="http://code.jquery.com/jquery-latest.js"></script> 
    <script>

        var items = null;
        var modifyIndex = 0;

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
            modifyIndex = i;
            var datatoSend = {"index": i+""};

            $.ajax({
                url: "/spec/modify",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify(datatoSend),
                success: function(data){
                    if(data == null || data.length == 0){
                        var itemhtml = "<option value='-1' selected>아래 스탯을 입력해주세요</option>";
                        document.getElementById("selectItem").innerHTML = itemhtml;
                    }
                    else{
                        items = data;
                        var itemhtml = "<option value='-1' selected>아이템을 선택해주세요</option>";
                        for(var index = 0; index < data.length; index++){
                            itemhtml += "<option value= '" + index +"'>"+ data[index].itemName + "</option>";
                        }
                        document.getElementById("selectItem").innerHTML = itemhtml;
                    }
                    var div1 = document.getElementById("bannerFull");
                    div1.style.visibility = "visible";
                },
                error: function(){
                    alert("err");
                }
            });

            var options = "";
            options += "<option value=\"trashval\">없음</option>"
            options += "<option value=\"mainstatPercent\">${player.STATSSELECTER[player.mainstatSel]}%</option>"
            options += "<option value=\"substat1Percent\">${player.STATSSELECTER[player.substat1Sel]}%</option>"
            options += "<option value=\"substat2Percent\">${player.STATSSELECTER[player.substat2Sel]}%</option>"
            options += "<option value=\"allstatPercent\">올스탯%</option>";
            if(modifyIndex == 2 || modifyIndex == 13 || modifyIndex == 16){
                options += "<option value=\"attmagPercent\">${player.ATTSELECTER[player.attmagSel]}%</option>";
                options += "<option value=\"penetrate\">몬스터 방어율 무시</option>";
                options += "<option value=\"dmg\">데미지%</option>";
                if(modifyIndex != 2){
                    options += "<option value=\"bossDMG\">보스 공격시 데미지</option>";
                }
            }
            else if(modifyIndex == 20){
                options += "<option value=\"critDMG\">크리티컬 데미지</option>";
            }
            var selectBoxes = document.getElementsByClassName("modifyPotential");
            for(var count = 0; count < selectBoxes.length; count++){
                selectBoxes[count].innerHTML = options;
            }
        }

        function modifyCancel(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "hidden";
            
            //Reset
            items = null;
            document.getElementById("mainstat").innerText = 0;
            document.getElementById("substat1").innerText = 0;
        }

        function modifyConfirm(){
            var mainstat = Number(document.getElementById("mainstat").innerText) + Number($("#modifyMainstat").val());
            var substat1 = Number(document.getElementById("substat1").innerText) + Number($("#modifySubstat1").val());
            var substat2 = Number(document.getElementById("substat2").innerText) + Number($("#modifySubstat2").val());
            var attmag = Number(document.getElementById("attmag").innerText) + Number($("#modifyAttmag").val());
            var pureattmag = Number(document.getElementById("attmag").innerText);
            var allstatPercent = Number($("#modifyallstatPercent").val());
            var reqLev = items[parseInt($('#selectItem').val())].reqLev;
            var starforce = Number($("#modifyStarforce").val());

            var datatoSend = {"modifyIndex": modifyIndex, "mainstat": mainstat, "substat1": substat1, "substat2": substat2,
                             "attmag": attmag, "allstatPercent": allstatPercent, "penetrate": 0, "pureattmag": pureattmag, "reqLev": reqLev, "starforce": starforce};
            
            for(var i = 0; i < 3; i++){
                var potentialData = $('#modifyPotential' + i).val();
                var potentialValue = Number($("#modifyPotentialInput" + i).val());

                if(potentialData == "penetrate"){
                    datatoSend[potentialData] = (10000 - (100 - datatoSend[potentialData]) * (100 - potentialValue)) / 100.0;
                }
                else if(potentialData in datatoSend){
                    datatoSend[potentialData] += potentialValue;
                }
                else{
                    datatoSend[potentialData] = potentialValue;
                }
            }

            $.ajax({
                url: "/spec/modifyConfirm",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify(datatoSend),
                success: function(data){
                    var div1 = document.getElementById("bannerFull");
                    div1.style.visibility = "hidden";
                    $("#mainContent").load("/spec #mainContentInner");
                },
                error: function(){
                    alert("err");
                }
            });
        }

        function dynamicStats(){
            if(items == null || items.length == 0){
                document.getElementById("mainstat").innerText = test;
                //...
            }
            var selectVal = $('#selectItem').val();
            if(selectVal < 0) return;
            console.log("Select Changed!: " + selectVal);
            document.getElementById("mainstat").innerText = items[parseInt(selectVal)].mainstat;
            document.getElementById("substat1").innerText = items[parseInt(selectVal)].substat1;
            document.getElementById("substat2").innerText = items[parseInt(selectVal)].substat2;
            document.getElementById("attmag").innerText = items[parseInt(selectVal)].attmag;
        }

        function navibtn1(){
            var target = document.getElementById("navfindForm");
            if(target.style.visibility == 'visible'){
                target.style.visibility = 'hidden';
                target.style.display = 'none';
            }
            else{
                target.style.visibility = 'visible';
                target.style.display = 'block';
            }
        }

        function validate(form){
            var flag = true;
            if(document.getElementById("navCharacterName").value.length == 0){
                document.getElementById("errorname").style.visibility = 'visible';
                document.getElementById("errorname").style.display = 'block';
                flag = false;
            }
            else{
                document.getElementById("errorname").style.visibility = 'hidden';
                document.getElementById("errorname").style.display = 'none';
            }
            var attmag = document.getElementById("attmagSel");
            if(attmag.options[attmag.selectedIndex].value == 0){
                document.getElementById("errorattmag").style.visibility = 'visible';
                document.getElementById("errorattmag").style.display = 'block';
                flag = false;
            }
            else{
                document.getElementById("errorattmag").style.visibility = 'hidden';
                document.getElementById("errorattmag").style.display = 'none';
            }
            var mainstat = document.getElementById("mainstatSel");
            if(mainstat.options[mainstat.selectedIndex].value == 0){
                document.getElementById("errormainstat").style.visibility = 'visible';
                document.getElementById("errormainstat").style.display = 'block';
                flag = false;
            }
            else{
                document.getElementById("errormainstat").style.visibility = 'hidden';
                document.getElementById("errormainstat").style.display = 'none';
            }
            var substat1 = document.getElementById("substat1Sel");
            if(substat1.options[substat1.selectedIndex].value == 0){
                document.getElementById("errorsubstat1").style.visibility = 'visible';
                document.getElementById("errorsubstat1").style.display = 'block';
                flag = false;
            }
            else{
                document.getElementById("errorsubstat1").style.visibility = 'hidden';
                document.getElementById("errorsubstat1").style.display = 'none';
            }
            return flag;
        }
    </script>
    <link rel="stylesheet" href="resource/css/board.css" type="text/css">
</head>

<body>
    <div id="bannerFull">
        <div id="bannerBackground">
            <div id="bannerModify">
                <select id="selectItem" onchange="dynamicStats();">
                </select>
                <div>    기본스탯               추가옵션</div>
                <div><span>스타포스: </span><input type="number" id="modifyStarforce"></div>
                <div><span>${player.STATSSELECTER[player.mainstatSel]}: </span><span id="mainstat">0</span> + <input type="number" id="modifyMainstat"></div>
                <div><span>${player.STATSSELECTER[player.substat1Sel]}: </span><span id="substat1">0</span> + <input type="number" id="modifySubstat1"></div>
                <div><span>${player.STATSSELECTER[player.substat2Sel]}: </span><span id="substat2">0</span> + <input type="number" id="modifySubstat2"></div>
                <div><span>${player.ATTSELECTER[player.attmagSel]}: </span><span id="attmag">0</span> + <input type="number" id="modifyAttmag"></div>
                <div><span>올스탯%: </span><input type="number" id="modifyallstatPercent"></div>
                <hr>
                <div>잠재능력</div>
                <table>
                    <tr>
                        <c:forEach begin="0" end="2" var="count" varStatus="status">
                        </tr><tr>
                            <div>
                                <td>
                                    <select class="modifyPotential" id="modifyPotential${count}"></select>
                                </td>
                                <td>
                                    <input type="number" id="modifyPotentialInput${count}">
                                </td>
                            </div>
                        </c:forEach>
                    </tr>
                </table>
                <div>
                    <button onclick="modifyConfirm();">apply</button>
                    <button onclick="modifyCancel();">cancel</button>
                </div>
                
            </div>
        </div>
    </div>
    <div class="navi">
        <div>REBOOT Spec Calculator</div>
        <div class="navibutton" id="navibtn1" onclick="navibtn1();">다른 캐릭터 검색</div>
        <form:form modelAttribute="FindCharacterVO" action="/search" method="post" id="navfindForm" onsubmit="return validate(this);">
            <div>
                <input type="text" name="characterName" id="navCharacterName" placeholder="캐릭터명 입력"/>
            </div>
            <div class="errormsg" id="errorname">필수 항목입니다.</div>
            <div class="navFormSelect">
                <form:select path="attmagSel">
                    <form:option value="0" label="공/마 선택"/>
                    <form:options items="${attmagSel}"/>
                </form:select>
                <div class="errormsg" id="errorattmag">필수 항목입니다.</div>
                <form:select path="mainstatSel">
                    <form:option value="0" label="주스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
                <div class="errormsg" id="errormainstat">필수 항목입니다.</div>
                <form:select path="substat1Sel">
                    <form:option value="0" label="부스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
                <div class="errormsg" id="errorsubstat1">필수 항목입니다.</div>
                <form:select path="substat2Sel">
                    <form:option value="0" label="추가 부스탯 선택"/>
                    <form:options items="${statSel}"/>
                </form:select>
            </div>
            <button type="submit" name="btnSearch" id="navBtnSearch" onclick="">검색</button>
        </form:form>
        <div class="navibutton" id="navibtn2" onclick="navibtn2();">ABOUT</div>

    </div>
    <div id="mainContent">
        <div id="mainContentInner">
            <div class="characterSpecOuter">
                <table>
                    <tr>
                        <td>
                            <div class="characterImg">
                                <img src="${player.characterImgUrl}">
                            </div>
                        </td>
                        <td>
                            <div class="characterSpec">
                                <p>캐릭터명: ${player.characterName}</p>
                                <table id="playerSpecTable">
                                    <tr>
                                        <td>순수 ${player.STATSSELECTER[player.mainstatSel]}: ${player.mainstat}</td><td>순수 ${player.STATSSELECTER[player.substat1Sel]}: ${player.substat1}</td><c:if test="${player.substat2Sel >= 0}"><td>순수 ${player.STATSSELECTER[player.substat2Sel]}: ${player.substat2}</td></c:if>
                                    </tr><tr>
                                        <td>${player.STATSSELECTER[player.mainstatSel]}%: ${player.mainstatPercent}</td><td>${player.STATSSELECTER[player.substat1Sel]}%: ${player.substat1Percent}</td><c:if test="${player.substat2Sel >= 0}"><td>${player.STATSSELECTER[player.substat2Sel]}%: ${player.substat2Percent}</td></c:if><td>올스탯%: ${player.allstatPercent}</td>
                                    </tr><tr>
                                        <td>${player.ATTSELECTER[player.attmagSel]}: ${player.attmag}</td><td>${player.ATTSELECTER[player.attmagSel]}%: ${player.attmagPercent}</td>
                                    </tr><tr>
                                        <td>총 ${player.STATSSELECTER[player.mainstatSel]}: ${player.totalmainstat}</td><td>총 ${player.STATSSELECTER[player.substat1Sel]}: ${player.totalsubstat1}</td><c:if test="${player.substat2Sel >= 0}"><td>총 ${player.STATSSELECTER[player.substat2Sel]}: ${player.totalsubstat2}</td></c:if><td>총 ${player.ATTSELECTER[player.attmagSel]}: ${player.totalattmag}</td>
                                    </tr><tr>
                                        <td colspan="2">보스 몬스터 공격 시 데미지: ${player.bossDMG}%</td><td>데미지: ${player.dmg}</td>
                                    </tr><tr>
                                        <td colspan="2">몬스터 방어율 무시: ${player.penetrate}</td><td>크리티컬 데미지: ${player.critDMG}</td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table class="itemDescripterOuter">
                    <tr><td>
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
                                            <td class="itemTable" onclick="clickimage('item' + '${itemIndex}');"><img src="${player.equipeditem[itemIndex].itemImg}"></td>
                                            <c:set var="itemIndex" value="${itemIndex + 1}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tr>
                        </table>
                    </td><td>
                        <c:forEach begin="0" end="24" var="count" varStatus="status">
                            <div class="items" id="item${count}">
                                <div id="itemDescription">
                                <p>아이템명: ${player.equipeditem[count].itemName}</p>
                                <p>제한레벨: ${player.equipeditem[count].reqLev}</p>
                                <p>${player.STATSSELECTER[player.mainstatSel]}: ${player.equipeditem[count].mainstat}</p>
                                <p>${player.STATSSELECTER[player.substat1Sel]}: ${player.equipeditem[count].substat1}</p>
                                <p>${player.STATSSELECTER[player.substat2Sel]}: ${player.equipeditem[count].substat2}</p>
                                <p>${player.STATSSELECTER[player.mainstatSel]}%: ${player.equipeditem[count].mainstatPercent}</p>
                                <p>${player.STATSSELECTER[player.substat1Sel]}%: ${player.equipeditem[count].substat1Percent}</p>
                                <p>${player.STATSSELECTER[player.substat2Sel]}%: ${player.equipeditem[count].substat2Percent}</p>
                                <p>올스탯퍼: ${player.equipeditem[count].allstatPercent}</p>
                                <p>${player.ATTSELECTER[player.attmagSel]}: ${player.equipeditem[count].attmag}</p>
                                <p>${player.ATTSELECTER[player.attmagSel]}%: ${player.equipeditem[count].attmagPercent}</p>
                                <p>크리티컬 데미지: ${player.equipeditem[count].critDMG}</p>
                                <p>보스 몬스터 공격시 데미지: ${player.equipeditem[count].bossDMG}</p>
                                <p>몬스터 방어율 무시: ${player.equipeditem[count].penetrate}</p>
                                <button onclick="modify('${count}');">수정</button>
                                <%-- TODO: If user modify items, these item's border color will change to red --%>
                                <%-- TODO: If user press the button, change items image. Finally 'apply' button will recalculate stats. --%>
                                <%-- TODO: 'Compare to previous stats' function will useful. --%>
                                </div>
                            </div>
                        </c:forEach>
                    </td></tr>
                </table>
            </div>
        </div>
    </div>
</body>

</html>