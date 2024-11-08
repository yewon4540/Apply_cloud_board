package com.chunjae.awscloudrequestboard.controller;

import com.chunjae.awscloudrequestboard.dto.TempVariableDTO;
import com.chunjae.awscloudrequestboard.dto.TemplateDTO;
import com.chunjae.awscloudrequestboard.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final TemplateService templateService;

    @GetMapping("/dash")
    public String dash(Model model) {
        model.addAttribute("view","admin/dash");
        return "index";
    }

    @GetMapping("/variable")
    public String variable(Model model) {

        List<TemplateDTO> tlist = templateService.getTemplateList();

        List<TempVariableDTO> tvlist = templateService.getTempVariableList();

        model.addAttribute("tlist", tlist);

        model.addAttribute("tvlist", tvlist);

        log.info("controller_list..........{}", tvlist.get(0).getTemplate().getTid());

        model.addAttribute("view","admin/variable");
        return "index";
    }

    @GetMapping("/apply")
    public String apply(Model model) {
        model.addAttribute("view","admin/apply");
        return "index";
    }


    @GetMapping("/etc")
    public String etc(Model model) {
        model.addAttribute("view","admin/etc");
        return "index";
    }
}
