<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <script src="http://code.jquery.com/jquery-latest.js"></script> 
    <script>

        var items = null;
        var weaponAdditional = null;
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
                        
                        if(i == 13){
                            document.getElementById("modifyCellAttmag").innerHTML = "<select id='modifyAttmagSelect'></select>";
                            $.ajax({
                                url: "/spec/modifyWeapon",
                                type: "POST",
                                success: function(data){
                                    weaponAdditional = data;
                                },
                                error: function(){
                                    alert("err");
                                }
                            });
                        }
                        else{
                            document.getElementById("modifyCellAttmag").innerHTML = "<input type='number' class='modifyInput' id='modifyAttmag'>";
                        }
                    }
                    var div1 = document.getElementById("bannerFull");
                    div1.style.visibility = "visible";
                    var div2 = document.getElementById("bannerModify");
                    div2.style.visibility = "visible";
                    div2.style.display = "block";
                },
                error: function(){
                    alert("err");
                }
            });

            document.getElementById("mainstat").innerText = 0;
            document.getElementById("substat1").innerText = 0;
            document.getElementById("attmag").innerText = 0;
            document.getElementById("modifyMainstat").value = "";
            document.getElementById("modifySubstat1").value = "";
            if("${player.substat2Sel}" >= 0){
                document.getElementById("substat2").innerText = 0;
                document.getElementById("modifySubstat2").value = "";
            }
            console.log("${player.substat2Sel}");
            document.getElementById("modifyallstatPercent").value = "";
            document.getElementById("modifyStarforce").value = "";
            document.getElementById("modifyPotential0").value = "";
            document.getElementById("modifyPotential1").value = "";
            document.getElementById("modifyPotential2").value = "";
            document.getElementById("modifyPotentialInput0").value = "";
            document.getElementById("modifyPotentialInput1").value = "";
            document.getElementById("modifyPotentialInput2").value = "";

            var options = "";
            options += "<option value=\"trashval\">없음</option>"
            options += "<option value=\"mainstatPercent\">${player.STATSSELECTER[player.mainstatSel]}%</option>"
            options += "<option value=\"substat1Percent\">${player.STATSSELECTER[player.substat1Sel]}%</option>"
            if("${player.substat2Sel}" >= 0){
                options += "<option value=\"substat2Percent\">${player.STATSSELECTER[player.substat2Sel]}%</option>"
            }
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
            var div2 = document.getElementById("bannerModify");
            div2.style.visibility = "hidden";
            div2.style.display = "none";
            
            //Reset
            items = null;
            document.getElementById("mainstat").innerText = 0;
            document.getElementById("substat1").innerText = 0;
        }

        function modifyConfirm(){
            var mainstat = Number(document.getElementById("mainstat").innerText) + Number($("#modifyMainstat").val());
            var substat1 = Number(document.getElementById("substat1").innerText) + Number($("#modifySubstat1").val());
            
            if(modifyIndex == 13){
                var attmag = Number(document.getElementById("attmag").innerText) + Number($("#modifyAttmagSelect").val());
            }
            else{
                var attmag = Number(document.getElementById("attmag").innerText) + Number($("#modifyAttmag").val());
            }
            var pureattmag = Number(document.getElementById("attmag").innerText);
            var allstatPercent = Number($("#modifyallstatPercent").val());
            var reqLev = items[parseInt($('#selectItem').val())].reqLev;
            var starforce = Number($("#modifyStarforce").val());
            var itemName = items[parseInt($('#selectItem').val())].itemName;
            var itemImg = items[parseInt($('#selectItem').val())].itemImg;
            var penetrate = items[parseInt($('#selectItem').val())].penetrate;
            var bossDMG = items[parseInt($('#selectItem').val())].bossDMG;
            var set = items[parseInt($('#selectItem').val())].set;

            var datatoSend = {"itemName": itemName, "modifyIndex": modifyIndex, "mainstat": mainstat, "substat1": substat1, "substat2": substat2,
                             "attmag": attmag, "allstatPercent": allstatPercent, "penetrate": penetrate, "pureattmag": pureattmag, "reqLev": reqLev, "starforce": starforce,
                            "itemImg": itemImg, "bossDMG":bossDMG, "set":set};

            if("${player.substat2Sel}" >= 0) {
                var substat2 = Number(document.getElementById("substat2").innerText) + Number($("#modifySubstat2").val());
                datatoSend["substat2"] = substat2;
            }
            
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
                    var div2 = document.getElementById("bannerModify");
                    div2.style.visibility = "hidden";
                    div2.style.display = "none";
                    $("#mainContent").load("/spec #mainContentInner");
                },
                error: function(){
                    alert("err");
                }
            });
        }

        function dynamicStats(){
            if(items == null || items.length == 0){
                document.getElementById("mainstat").innerText = "0";
                //...
            }
            var selectVal = $('#selectItem').val();
            if(selectVal < 0) return;
            document.getElementById("mainstat").innerText = items[parseInt(selectVal)].mainstat;
            document.getElementById("substat1").innerText = items[parseInt(selectVal)].substat1;
            if("${player.substat2Sel}" >= 0){
                document.getElementById("substat2").innerText = items[parseInt(selectVal)].substat2;
            }
            document.getElementById("attmag").innerText = items[parseInt(selectVal)].attmag;

            if(modifyIndex == 13){
                var inner = "<option value='0'>0</option>";
                inner += "<option value='" + weaponAdditional[parseInt(selectVal)].additional5 + "'>" + weaponAdditional[parseInt(selectVal)].additional5 + "</option>";
                inner += "<option value='" + weaponAdditional[parseInt(selectVal)].additional4 + "'>" + weaponAdditional[parseInt(selectVal)].additional4 + "</option>";
                inner += "<option value='" + weaponAdditional[parseInt(selectVal)].additional3 + "'>" + weaponAdditional[parseInt(selectVal)].additional3 + "</option>";
                inner += "<option value='" + weaponAdditional[parseInt(selectVal)].additional2 + "'>" + weaponAdditional[parseInt(selectVal)].additional2 + "</option>";
                inner += "<option value='" + weaponAdditional[parseInt(selectVal)].additional1 + "'>" + weaponAdditional[parseInt(selectVal)].additional1 + "</option>";
                document.getElementById("modifyAttmagSelect").innerHTML = inner;
            }
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

        function modifyApply(){
            $.ajax({
                url: "/spec/modifyApply",
                type: "POST",
                contentType: 'application/json',
                success: function(data){
                    $("#mainContent").load("/spec #mainContentInner");
                },
                error: function(){
                    alert("err");
                }
            });
        }

        function modifyReset(){
            $.ajax({
                url: "/spec/modifyReset",
                type: "POST",
                contentType: 'application/json',
                success: function(data){
                    $("#mainContent").load("/spec #mainContentInner");
                },
                error: function(){
                    alert("err");
                }
            });
        }

        function modifyHyperstat(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "visible";
            var div2 = document.getElementById("bannerHyperstat");
            div2.style.visibility = "visible";
            div2.style.display = "block";
        }

        function modifyConfirmHyperstat(){
            var mainstat = Number(document.getElementById("hypermainstat").innerText);
            var attmag = Number(document.getElementById("hyperAttmag").innerText);
            var bossDMG = Number(document.getElementById("hyperBossDMG").innerText);
            var critDMG = Number(document.getElementById("hyperCritDMG").innerText);
            var dmg = Number(document.getElementById("hyperDMG").innerText);
            var penetrate = Number(document.getElementById("hyperPenetrate").innerText);

            var datatoSend = {"fixedMainstat": mainstat, "attmag": attmag, "bossDMG": bossDMG,
                                "critDMG": critDMG, "dmg": dmg, "penetrate": penetrate};

            $.ajax({
                url: "/spec/modifyHyperstat",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify(datatoSend),
                success: function(data){
                    var div1 = document.getElementById("bannerFull");
                    div1.style.visibility = "hidden";
                    var div2 = document.getElementById("bannerHyperstat");
                    div2.style.visibility = "hidden";
                    div2.style.display = "none";
                    $("#mainContent").load("/spec #mainContentInner");
                },
                error: function(){
                    alert("err");
                }
            });
        }
        function modifyCancelHyperstat(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "hidden";
            var div2 = document.getElementById("bannerHyperstat");
            div2.style.visibility = "hidden";
            div2.style.display = "none";
        }

        function hyperstatcheckValidation(obj){
            if(obj.value > 15){
                obj.value = 15;
            }
            else if(obj.value < 0){
                obj.value = 0;
            }
        }
        function onchangeHypermainstat(obj){
            hyperstatcheckValidation(obj);
            document.getElementById("hypermainstat").innerText = 30 * Number(obj.value);
        }
        function onchangeHyperCritDMG(obj){
            hyperstatcheckValidation(obj);
            document.getElementById("hyperCritDMG").innerText = Number(obj.value);
        }
        function onchangeHyperPenetrate(obj){
            hyperstatcheckValidation(obj);
            document.getElementById("hyperPenetrate").innerText = 3 * Number(obj.value);
        }
        function onchangeHyperDMG(obj){
            hyperstatcheckValidation(obj);
            document.getElementById("hyperDMG").innerText = 3 * Number(obj.value);
        }
        function onchangeHyperBossDMG(obj){
            hyperstatcheckValidation(obj);
            if(obj.value >= 6){
                document.getElementById("hyperBossDMG").innerText = 15 + 4 * (Number(obj.value) - 5);
            }
            else{
                document.getElementById("hyperBossDMG").innerText = 3 * Number(obj.value);
            }
        }
        function onchangeHyperAttmag(obj){
            document.getElementById("hyperAttmag").innerText = 3 * Number(obj.value);
        }

        function modifyUnion(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "visible";
            var div2 = document.getElementById("bannerUnion");
            div2.style.visibility = "visible";
            div2.style.display = "block";
        }

        function modifyConfirmUnion(){
            var mainstat = Number($("#unionMainstat").val()) + Number($("#additionalMainstat").val());
            var fixedMainstat = Number($("#additionalFixedMainstat").val());
            var substat1 = Number($("#unionSubstat1").val()) + Number($("#additionalSubstat1").val());
            var substat2 = Number($("#unionSubstat2").val()) + Number($("#additionalSubstat2").val());
            var attmag = Number($("#unionAttmag").val()) + Number($("#additionalAttmag").val());
            var bossDMG = Number($("#unionBossDMG").val()) + Number($("#additionalBossDMG").val());
            var critDMG = Number($("#unionCritDMG").val()) + Number($("#additionalCritDMG").val());
            var dmg = Number($("#additionalDMG").val());
            var penetrate = (10000 - (100 - Number($("#unionPenetrate").val())) * (100 - Number($("#additionalPenetrate").val()))) / 100.0;
            penetrate = (10000 - (100 - penetrate) * (100 - Number($("#blasterPenetrate").val()))) / 100.0;
            if(document.getElementById("zeroLink").checked){
                penetrate = (10000 - (100 - penetrate) * 90) / 100.0;
            }
            if(document.getElementById("hoyoungLink").checked){
                penetrate = (10000 - (100 - penetrate) * 90) / 100.0;
            }
            if(document.getElementById("lumiLink").checked){
                penetrate = (10000 - (100 - penetrate) * 85) / 100.0;
            }

            

            var datatoSend = {"mainstat": mainstat, "fixedMainstat": fixedMainstat, "substat1": substat1, "substat2": substat2, 
                            "attmag": attmag, "bossDMG": bossDMG, "critDMG": critDMG, "dmg": dmg, "penetrate": penetrate};

            var defense = Number($("#inputDefense").val());
            var mapleSoldier = document.getElementById("mapleSoldier").checked? 1 : 0;
            var dataUnionBannerEtc = JSON.stringify({"mapleSoldier": mapleSoldier, "defense": defense});

            $.ajax({
                url: "/spec/modifyUnionBannerEtc",
                type: "POST",
                contentType: 'application/json',
                data: dataUnionBannerEtc,
                success: function(data){
                    $.ajax({
                        url: "/spec/modifyUnion",
                        type: "POST",
                        contentType: 'application/json',
                        data: JSON.stringify(datatoSend),
                        success: function(data){
                            var div1 = document.getElementById("bannerFull");
                            div1.style.visibility = "hidden";
                            var div2 = document.getElementById("bannerUnion");
                            div2.style.visibility = "hidden";
                            div2.style.display = "none";
                            $("#mainContent").load("/spec #mainContentInner");
                        },
                        error: function(){
                            alert("err");
                        }
                    });
                },
                error: function(){
                    alert("err");
                }
            });
        }

        function modifyCancelUnion(){
            var div1 = document.getElementById("bannerFull");
            div1.style.visibility = "hidden";
            var div2 = document.getElementById("bannerUnion");
            div2.style.visibility = "hidden";
            div2.style.display = "none";
        }

    </script>
    <link rel="stylesheet" href="resource/css/board.css" type="text/css">
</head>

<body>
    <div id="bannerFull">
        <div id="bannerBackground">
            <div id="bannerModify" class="banner">
                <select id="selectItem" onchange="dynamicStats();">
                </select>
                <br>스타포스: <input type="number" class="modifyInput" id="modifyStarforce">성
                <table class="modifyTable">
                    <tr>
                        <td></td><td>기본스탯</td><td></td><td>추가옵션</td>
                    </tr>
                    <tr>
                        <td>${player.STATSSELECTER[player.mainstatSel]}: </td><td id="mainstat">0</td><td> + </td><td><input type="number" class="modifyInput" id="modifyMainstat"></td>
                    </tr>
                    <tr>
                        <td>${player.STATSSELECTER[player.substat1Sel]}: </td><td id="substat1">0</td><td> + </td><td><input type="number" class="modifyInput" id="modifySubstat1"></td>
                    </tr>
                    <c:if test="${player.substat2Sel >= 0}">
                        <tr>
                            <td>${player.STATSSELECTER[player.substat2Sel]}: </td><td id="substat2">0</td><td> + </td><td><input type="number" class="modifyInput" id="modifySubstat2"></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>${player.ATTSELECTER[player.attmagSel]}: </td><td id="attmag">0</td><td> + </td><td id="modifyCellAttmag"></td>
                    </tr>
                    <tr>
                        <td>올스탯%: </td><td></td><td></td><td><input type="number" class="modifyInput" id="modifyallstatPercent"></td>
                    </tr>
                </table>    
                <hr class="line">
                <div>잠재능력</div>
                <table id="modifyPotentialTable">
                    <tr>
                        <c:forEach begin="0" end="2" var="count" varStatus="status">
                        </tr><tr>
                            <div>
                                <td>
                                    <select class="modifyPotential" id="modifyPotential${count}"></select>
                                </td>
                                <td> : </td>
                                <td>
                                    <input class="modifyInput" type="number" id="modifyPotentialInput${count}">
                                </td>
                            </div>
                        </c:forEach>
                    </tr>
                </table>
                <div id="modifyButtons">
                    <button class="buttons" onclick="modifyConfirm();">확인</button>
                    <button class="buttons" onclick="modifyCancel();">취소</button>
                </div>
            </div>
            <div id="bannerHyperstat" class="banner">
                <div class="bannerTitle">하이퍼스탯 설정</div>
                <table id="tableHyperStat">
                    <tr><td>${player.STATSSELECTER[player.mainstatSel]}</td><td><input type="number" min="0" max="15" onchange="onchangeHypermainstat(this);"></td><td>→</td><td id="hypermainstat">0</td><td></td></tr>
                    <tr><td>크리 데미지</td><td><input type="number" min="0" max="15" onchange="onchangeHyperCritDMG(this);"></td><td>→</td><td id="hyperCritDMG">0</td><td>%</td></tr>
                    <tr><td>방어율 무시</td><td><input type="number" min="0" max="15" onchange="onchangeHyperPenetrate(this);"></td><td>→</td><td id="hyperPenetrate">0</td><td>%</td></tr>
                    <tr><td>데미지</td><td><input type="number" min="0" max="15" onchange="onchangeHyperDMG(this);"></td><td>→</td><td id="hyperDMG">0</td><td>%</td></tr>
                    <tr><td>보스데미지</td><td><input type="number" min="0" max="15" onchange="onchangeHyperBossDMG(this);"></td><td>→</td><td id="hyperBossDMG">0</td><td>%</td></tr>
                    <tr><td>공격력 / 마력</td><td><input type="number" min="0" max="15" onchange="onchangeHyperAttmag(this);"></td><td>→</td><td id="hyperAttmag">0</td><td></td></tr>
                </table>
                <div id="buttonsHyperStat">
                    <button class="buttons" onclick="modifyConfirmHyperstat();">확인</button>
                    <button class="buttons" onclick="modifyCancelHyperstat();">취소</button>
                </div>
            </div>
            <div id="bannerUnion" class="banner">
                <div class="bannerTitle">유니온 / 추가스펙 설정</div>
                <table id="tableUnion">
                    <colgroup>
                        <col width="*">
                        <col width="75px">
                        <col width="50px">
                        <col width="75px">
                    </colgroup>
                    <tr><td></td><td class="tdCenter">유니온</td><td></td><td class="tdCenter">추가스펙</td></tr>
                    <tr><td>${player.STATSSELECTER[player.mainstatSel]}</td><td><input type="number" class="inputstat" id="unionMainstat"></td><td> / 75 + </td><td><input type="number" class="inputstat" id="additionalMainstat"></td></tr>
                    <tr><td>고정 ${player.STATSSELECTER[player.mainstatSel]}</td><td></td><td></td><td><input type="number" class="inputstat" id="additionalFixedMainstat"></td></tr>
                    <tr><td>${player.STATSSELECTER[player.substat1Sel]}</td><td><input type="number" class="inputstat" id="unionSubstat1"></td><td> / 75 + </td><td><input type="number" class="inputstat" id="additionalSubstat1"></td></tr>
                    <c:if test="${player.substat2Sel >= 0}"><tr><td>${player.STATSSELECTER[player.substat2Sel]}</td><td><input type="number" class="inputstat" id="unionSubstat2"></td><td> / 75 + </td><td><input type="number" class="inputstat" id="additionalSubstat2"></td></tr></c:if>
                    <tr><td>${player.ATTSELECTER[player.attmagSel]}</td><td><input type="number" class="inputstat" id="unionAttmag"></td><td> / 15 + </td><td><input type="number" class="inputstat" id="additionalAttmag"></td></tr>
                    <tr><td>크리티컬 데미지</td><td><input type="number" class="inputstat" id="unionCritDMG"></td><td> / 20 + </td><td><input type="number" class="inputstat" id="additionalCritDMG"></td></tr>
                    <tr><td>방어율 무시</td><td><input type="number" class="inputstat" id="unionPenetrate"></td><td> / 40 + </td><td><input type="number" class="inputstat" id="additionalPenetrate"></td></tr>
                    <tr><td>보스 데미지</td><td><input type="number" class="inputstat" id="unionBossDMG"></td><td> / 40 + </td><td><input type="number" class="inputstat" id="additionalBossDMG"></td></tr>
                    <tr><td>데미지</td><td></td><td></td><td><input type="number" class="inputstat" id="additionalDMG"></td></tr>
                </table>
                <hr class="line">
                <div>
                    <div class="additionalLink">메이플 용사 적용 <input type="checkbox" id="mapleSoldier"></div>
                    <div class="additionalLink">블래스터 유니온 추가방무 <input type="number" class="inputstat" id="blasterPenetrate"></div>
                    <div class="additionalLink"><span class="link">제로 링크(방무 10%)<input type="checkbox" id="zeroLink"></span><span class="link">호영 링크(방무 10%)<input type="checkbox" id="hoyoungLink"></span><span>루미 링크(방무 15%)<input type="checkbox" id="lumiLink"></span></div>
                    
                </div>
                <hr class="line">
                <div id="bannerEnemy">
                    <div class="additionalLink">적 방어율: <input type="number" value="300" class="inputDefense" id="inputDefense"></div>
                </div>
                <div>
                    <button class="buttons" onclick="modifyConfirmUnion();">적용</button>
                    <button class="buttons" onclick="modifyCancelUnion();">취소</button>
                </div>
            </div>
        </div>
    </div>
    <%-- 
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
    --%>
    <div id="mainContent">
        <div id="mainContentInner">
            <div class="characterSpecOuter">
                <table class="characterSpecInner">
                    <tr>
                        <td>
                            <div class="characterImg">
                                <img src="${player.characterImgUrl}">
                            </div>
                        </td>
                        <td>
                            <div class="characterSpec">
                                <p><span id="characterName">캐릭터명: ${player.characterName}</span><button class="buttons" id="buttonHyper" onclick="modifyHyperstat();">하이퍼스탯 설정</button><button class="buttons" id="buttonUnion" onclick="modifyUnion();">유니온/추가스펙 설정</button></p>
                                <table id="playerSpecTable">
                                    <tr class="playerSpecRow">
                                        <td>순수 ${player.STATSSELECTER[player.mainstatSel]}: ${player.mainstat} → ${playerCompare.mainstat}</td><td>${player.STATSSELECTER[player.mainstatSel]}%: ${player.mainstatPercent}% → ${playerCompare.mainstatPercent}%</td><td>총 ${player.STATSSELECTER[player.mainstatSel]}: ${player.totalmainstat} → ${playerCompare.totalmainstat}</td>
                                    </tr><tr class="playerSpecRow">
                                        <td>순수 ${player.STATSSELECTER[player.substat1Sel]}: ${player.substat1} → ${playerCompare.substat1}</td><td>${player.STATSSELECTER[player.substat1Sel]}%: ${player.substat1Percent}% → ${playerCompare.substat1Percent}%</td><td>총 ${player.STATSSELECTER[player.substat1Sel]}: ${player.totalsubstat1} → ${playerCompare.totalsubstat1}</td>
                                    <c:if test="${player.substat2Sel >= 0}">
                                        </tr><tr class="playerSpecRow">
                                            <td>순수 ${player.STATSSELECTER[player.substat2Sel]}: ${player.substat2} → ${playerCompare.substat2}</td><td>${player.STATSSELECTER[player.substat2Sel]}%: ${player.substat2Percent}% → ${playerCompare.substat2Percent}%</td><td>총 ${player.STATSSELECTER[player.substat2Sel]}: ${player.totalsubstat2} → ${playerCompare.totalsubstat2}</td>
                                    </c:if>
                                    </tr><tr class="playerSpecRow">
                                        <td>고정 ${player.STATSSELECTER[player.mainstatSel]}: ${player.fixedMainstat} → ${playerCompare.fixedMainstat}</td><td>올스탯%: ${player.allstatPercent}% → ${playerCompare.allstatPercent}%</td>
                                    </tr><tr class="playerSpecRow">
                                        <td>${player.ATTSELECTER[player.attmagSel]}: ${player.attmag} → ${playerCompare.attmag}</td><td>${player.ATTSELECTER[player.attmagSel]}%: ${player.attmagPercent}% → ${playerCompare.attmagPercent}%</td><td>총 ${player.ATTSELECTER[player.attmagSel]}: ${player.totalattmag} → ${playerCompare.totalattmag}</td>
                                    </tr><tr class="playerSpecRow">
                                        <td colspan="2">보스 몬스터 공격 시 데미지: ${player.bossDMG}% → ${playerCompare.bossDMG}%</td><td>데미지: ${player.dmg}% → ${playerCompare.dmg}%</td>
                                    </tr><tr class="playerSpecRow">
                                        <td colspan="2">몬스터 방어율 무시: ${player.penetrate}% → ${playerCompare.penetrate}%</td><td>크리티컬 데미지: ${player.critDMG}% → ${playerCompare.critDMG}%</td>
                                    </tr><tr>
                                        <td class="specResult">예상 화력 증가량: ${dmgResult}%</td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="itemDescripterOuter">
                <table class="itemDescripterTable">
                    <tr><td>
                        <table id="itemTableOuter">
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
                                            <td class="itemTable" onclick="clickimage('item' + '${itemIndex}');">
                                                <c:if test="${playerCompare.equipeditem[itemIndex].isModified == 1}"><div class="itemModifiedBorder"></c:if>
                                                <img src="${playerCompare.equipeditem[itemIndex].itemImg}">
                                                <c:if test="${playerCompare.equipeditem[itemIndex].isModified == 1}"></div></c:if>
                                            </td>
                                            <c:set var="itemIndex" value="${itemIndex + 1}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tr>
                        </table>
                    </td><td id="itemDescriptCell">
                        <div id="itemDescriptBorder">
                            <c:forEach begin="0" end="24" var="count" varStatus="status">
                                <div class="items" id="item${count}">
                                    <div class="itemDescription">
                                        <div id="itemTitle">${playerCompare.equipeditem[count].itemName}</div>
                                        <div>제한레벨: ${playerCompare.equipeditem[count].reqLev}</div>
                                        <div class="itemStatSeparator"><div class="itemStat">${playerCompare.STATSSELECTER[playerCompare.mainstatSel]}: ${playerCompare.equipeditem[count].mainstat}</div><div class="itemStat">${playerCompare.STATSSELECTER[playerCompare.mainstatSel]}%: ${playerCompare.equipeditem[count].mainstatPercent}%</div></div>
                                        <div class="itemStatSeparator"><div class="itemStat">${playerCompare.STATSSELECTER[playerCompare.substat1Sel]}: ${playerCompare.equipeditem[count].substat1}</div><div class="itemStat">${playerCompare.STATSSELECTER[playerCompare.substat1Sel]}%: ${playerCompare.equipeditem[count].substat1Percent}%</div></div>
                                        <c:if test="${playerCompare.substat2Sel >= 0}"><div class="itemStatSeparator"><div class="itemStat">${playerCompare.STATSSELECTER[playerCompare.substat2Sel]}: ${playerCompare.equipeditem[count].substat2}</div><div class="itemStat">${playerCompare.STATSSELECTER[player.substat2Sel]}%: ${playerCompare.equipeditem[count].substat2Percent}%</div></div></c:if>
                                        <div>올스탯%: ${playerCompare.equipeditem[count].allstatPercent}%</div>
                                        <div class="itemStatSeparator"><div class="itemStat">${playerCompare.ATTSELECTER[playerCompare.attmagSel]}: ${playerCompare.equipeditem[count].attmag}</div><div class="itemStat">${playerCompare.ATTSELECTER[playerCompare.attmagSel]}%: ${playerCompare.equipeditem[count].attmagPercent}%</div></div>
                                        <div>크리티컬 데미지: ${playerCompare.equipeditem[count].critDMG}%</div>
                                        <div>보스 몬스터 공격시 데미지: ${playerCompare.equipeditem[count].bossDMG}%</div>
                                        <div>몬스터 방어율 무시: ${playerCompare.equipeditem[count].penetrate}%</div>
                                        <div id="itemDescriptButtonOuter"><button id="itemDescriptButton" class="buttons" onclick="modify('${count}');">수정</button></div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </td></tr>
                </table>
                <div id="applybuttons">
                    <button class="buttons" id="buttonsApplyItem" onclick="modifyApply();">임시저장</button>
                    <button class="buttons" id="buttonsApplyItem" onclick="modifyReset();">초기화</button>
                </div>
            </div>
        </div>
    </div>
</body>

</html>