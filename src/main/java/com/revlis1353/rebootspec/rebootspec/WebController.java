package com.revlis1353.rebootspec.rebootspec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.bean.CsvToBeanBuilder;

@Controller
@SessionAttributes("player")
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
    public String search(@ModelAttribute("FindCharacterVO") @Valid FindCharacterVO charVO, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        new CharacterValidator().validate(charVO, result);
        if(result.hasErrors()) return "index";

        Crawler crawler = new Crawler(charVO);
        Character player = new Character(charVO, crawler.getCharacterLevel(), crawler.getCharacterItemData());
        player.setCharacterImgUrl(crawler.getCharacterImgUrl());

        //index에서 form을 전달받아 characterName을 Crawler로 전달, 아이템 데이터를 받는다.
        //아이템 데이터를 정리하여 Character Data 객체에 저장 후 출력
        player.calculateSpec();
        model.addAttribute("player", player);
        return "redirect:spec";
    }

    @RequestMapping("/spec")
    public String spec(@ModelAttribute("player") Character player, Model model){
        if(player == null)
            return "redirect:index";
        //Character playertest = (Character)model.getAttribute("playertest");
        //System.out.println(playertest.getAttmag());
        model.addAttribute("FindCharacterVO", new FindCharacterVO());
        //model.addAttribute("player", player);
        return "spec";
    }

    @ResponseBody
    @RequestMapping("/spec/modify")
    public List<DataItem> loadItem(@RequestBody Map<String, Object> data, Model model) {

        String target = (String)data.get("index");
        if(target.equals("3") || target.equals("7") || target.equals("12"))
            target = "0";
        else if(target.equals("8"))
            target = "4";
        else if(target.equals("11") || target.equals("23"))
            return null;

        String targetFile = "csv/" + target + ".csv";
        ClassPathResource path = new ClassPathResource(targetFile);
        List<DataItem> items = null;

        try {
            items = new CsvToBeanBuilder<DataItem>(new InputStreamReader(new FileInputStream(path.getFile().getAbsolutePath()), "utf-8"))
            .withType(DataItem.class)
            .build()
            .parse();
            
            model.addAttribute("items", items);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyConfirm")
    public int modifyItem(@RequestBody DataItem data, BindingResult result, Model model) {
        Character player = (Character)model.getAttribute("player");
        player.modifyItem(data);
        System.out.println("mainstat: " + data.getMainstat());
        System.out.println("Attmag: " + data.getAttmag());
        System.out.println("Player's Attmag: " + player.getAttmag());
        return 0;
    }

}