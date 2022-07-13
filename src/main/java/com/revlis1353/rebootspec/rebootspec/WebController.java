package com.revlis1353.rebootspec.rebootspec;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/")
    public String mainpage(Model model){
        return "index";
    }
    
    @RequestMapping("/index")
    public String index(Model model){
        return "index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("FindCharacterVO") @Valid FindCharacterVO charVO){
        System.out.println(charVO.getcharacterName());
        Crawler crawler = new Crawler(charVO.getcharacterName());
        Character player = new Character(crawler.getCharacterLevel(), crawler.getCharacterItemData());
        //index에서 form을 전달받아 characterName을 Crawler로 전달, 아이템 데이터를 받는다.
        //아이템 데이터를 정리하여 Character Data 객체에 저장 후 출력
        player.calculateSpec();
        //player.print();
        return "index";
    }
}