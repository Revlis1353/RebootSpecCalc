package com.revlis1353.rebootspec.rebootspec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @ModelAttribute("statSel")
    public Map<Integer, String> statSelector(){
        LinkedHashMap<Integer,String> statSel = new LinkedHashMap<Integer,String>();
        statSel.put(1, "STR");
        statSel.put(2, "DEX");
        statSel.put(3, "INT");
        statSel.put(4, "LUK");
        return statSel;
    }

    @ModelAttribute("attmagSel")
    public Map<Integer, String> attmagSelector(){
        LinkedHashMap<Integer,String> statSel = new LinkedHashMap<Integer,String>();
        statSel.put(1, "공격력");
        statSel.put(2, "마력");
        return statSel;
    }

    //TODO: Add checkbox to decide whether apply hyperstat or not

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasenames("message.label");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @RequestMapping("/")
    public String mainpage(Model model){
        return "redirect:index";
    }
    
    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("FindCharacterVO", new FindCharacterVO());
        return "index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("FindCharacterVO") @Valid FindCharacterVO charVO, BindingResult result, RedirectAttributes redirectAttributes){
        new CharacterValidator().validate(charVO, result);
        if(result.hasErrors())
            return "index";
        System.out.println(charVO.getcharacterName());
        Crawler crawler = new Crawler(charVO);
        Character player = new Character(charVO.getcharacterName(), crawler.getCharacterLevel(), crawler.getCharacterItemData());
        //index에서 form을 전달받아 characterName을 Crawler로 전달, 아이템 데이터를 받는다.
        //아이템 데이터를 정리하여 Character Data 객체에 저장 후 출력
        player.calculateSpec();
        player.print();
        redirectAttributes.addFlashAttribute("character", player);
        return "redirect:spec";
    }

    @RequestMapping("/spec")
    public String spec(@ModelAttribute("character") Character player, Model model){
        if(player == null)
            return "redirect:index";
        model.addAttribute("player", player);
        System.out.println(player.getAttmag());
        return "spec";
    }

    @RequestMapping("/spec/{id}")
    public String loadItem(@PathVariable String id, Model model) {
        //TODO: Load csv file and show data to user
        return "spec";
    }
}