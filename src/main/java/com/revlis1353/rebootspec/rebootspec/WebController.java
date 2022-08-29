package com.revlis1353.rebootspec.rebootspec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.opencsv.bean.CsvToBeanBuilder;

@Controller
@SessionAttributes({"player", "playerCompare"})
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
    public String search(@ModelAttribute("FindCharacterVO") @Valid FindCharacterVO charVO, BindingResult result, Model model){
        new CharacterValidator().validate(charVO, result);
        if(result.hasErrors()) return "index";

        charVO.reduce1Selector();

        Crawler crawler;
        Character player;

        String targetFile = "csv/class.csv";
        ClassPathResource path = new ClassPathResource(targetFile);
        List<DataItem> classes = null;

        try {
            crawler = new Crawler(charVO);
            player = new Character(charVO, crawler.getCharacterLevel(), crawler.getCharacterItemData());

            player.setCharacterImgUrl(crawler.getCharacterImgUrl());
            player.setBaseFixedMainstat(crawler.getArcaneMainstat());
            player.setHyperstat(crawler.getCharacterHyperstat());

            //Get class spec data from csv
            classes = new CsvToBeanBuilder<DataItem>(new InputStreamReader(path.getInputStream(), "utf-8"))
            .withType(DataItem.class)
            .build()
            .parse();

            String playerClass = crawler.getCharacterClass();

            for(int i = 0; i < classes.size(); i++){
                if(classes.get(i).getItemName().equals(playerClass)){
                    player.setClassSpec(classes.get(i));
                    break;
                }
            }

            player.calculateSpec();
            model.addAttribute("player", player);

            Character playerCompare = new Character(player);
            model.addAttribute("playerCompare", playerCompare);
            
        } catch (Exception e) {
            e.printStackTrace();
            charVO.add1Selector();
            result.rejectValue("characterName", "openInformation");
            return "index";
        }
       
        return "redirect:spec";
    }

    @RequestMapping("/spec")
    public String spec(@ModelAttribute("player") Character player, Model model){
        if(player == null)
            return "redirect:index";
        compareDamage(model);
        //model.addAttribute("FindCharacterVO", new FindCharacterVO());
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
            items = new CsvToBeanBuilder<DataItem>(new InputStreamReader(path.getInputStream(), "utf-8"))
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
    @RequestMapping("/spec/modifyWeapon")
    public List<DataWeapon> loadWeaponAdditional(Model model) {

        String targetFile = "csv/13additional.csv";
        ClassPathResource path = new ClassPathResource(targetFile);
        
        List<DataWeapon> items = null;

        try {
            items = new CsvToBeanBuilder<DataWeapon>(new InputStreamReader(path.getInputStream(), "utf-8"))
            .withType(DataWeapon.class)
            .build()
            .parse();
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
        Character playerCompare = (Character)model.getAttribute("playerCompare");
        data.applyStarforce();
        data.setIsModified(1);
        playerCompare.modifyItem(data);
        return 0;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyApply")
    public int applySpec(Model model) {
        Character playerCompare = (Character)model.getAttribute("playerCompare");
        playerCompare.clearEquipmentsModifyLog();
        model.addAttribute("player", playerCompare);
        Character newplayerCompare = new Character(playerCompare);
        model.addAttribute("playerCompare", newplayerCompare);
        return 0;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyReset")
    public int resetSpec(Model model) {
        Character player = (Character)model.getAttribute("player");
        Character playerCompare = new Character(player);
        model.addAttribute("playerCompare", playerCompare);
        return 0;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyHyperstat")
    public int modifyHyperstat(@RequestBody HyperstatVO data, BindingResult result, Model model) {
        Character player = (Character)model.getAttribute("player");
        Character playerCompare = (Character)model.getAttribute("playerCompare");

        player.setHyperstat(data);
        player.calculateSpec();

        playerCompare.setHyperstat(data);
        playerCompare.calculateSpec();
        return 0;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyUnion")
    public int modifyUnion(@RequestBody DataItem data, BindingResult result, Model model) {
        Character player = (Character)model.getAttribute("player");
        Character playerCompare = (Character)model.getAttribute("playerCompare");
        
        player.setUnion(data);
        player.calculateSpec();

        playerCompare.setUnion(data);
        playerCompare.calculateSpec();
        return 0;
    }

    @ResponseBody
    @RequestMapping("/spec/modifyMapleSoldier")
    public int modifyMapleSoldier(@RequestBody Map<String, Integer> data, BindingResult result, Model model) {
        Character player = (Character)model.getAttribute("player");
        Character playerCompare = (Character)model.getAttribute("playerCompare");

        //int isActive = Integer.parseInt(data);
        
        player.setMapleSoldier(data.get("mapleSoldier"));
        playerCompare.setMapleSoldier(data.get("mapleSoldier"));
        return 0;
    }

    public void compareDamage(Model model){
        Character player = (Character)model.getAttribute("player");
        Character playerCompare = (Character)model.getAttribute("playerCompare");

        int playerStat = player.getTotalmainstat() * 4 + player.getTotalsubstat1() + player.getTotalsubstat2();
        int playerCompareStat = playerCompare.getTotalmainstat() * 4 + playerCompare.getTotalsubstat1() + playerCompare.getTotalsubstat2();
        float statRatio = playerCompareStat / (float)playerStat;
        
        float attmagRatio = playerCompare.getTotalattmag() / (float)player.getTotalattmag();

        int playerDamage = 100 + player.getDmg() + player.getBossDMG();
        int playerCompareDamage = 100 + playerCompare.getDmg() + playerCompare.getBossDMG();
        float damageRatio = playerCompareDamage / (float)playerDamage;

        float critRatio = (135 + playerCompare.getCritDMG()) / (float)(135 + player.getCritDMG()); 

        float playerPenetrate = 100 - 300 * (100 - player.getPenetrate());
        float playerComparePenetrate = 100 - 300 * (100 - playerCompare.getPenetrate());
        float penetrateRatio = playerComparePenetrate / playerPenetrate;

        float dmgResult = Math.round((statRatio * attmagRatio * damageRatio * critRatio * penetrateRatio - 1) * 10000) / 100.0f;
        model.addAttribute("dmgResult", dmgResult);
    }
}