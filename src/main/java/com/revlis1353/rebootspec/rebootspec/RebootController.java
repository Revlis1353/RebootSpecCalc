package com.revlis1353.rebootspec.rebootspec;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RebootController {
    
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/search")
    public String search(){
        //index에서 form을 전달받아 characterName을 Crawler로 전달, 아이템 데이터를 받는다.
        //아이템 데이터를 정리하여 출력
        return "0";
    }
}