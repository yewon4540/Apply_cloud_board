package com.chunjae.awscloudrequestboard.controller;

import com.chunjae.awscloudrequestboard.domain.Template;
import com.chunjae.awscloudrequestboard.dto.TempVariableDTO;
import com.chunjae.awscloudrequestboard.dto.TemplateDTO;
import com.chunjae.awscloudrequestboard.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;
    private final ModelMapper modelMapper;

    @PostMapping("/addTemplate")
    public ResponseEntity<String> addTemplate(@RequestBody TemplateDTO templateDTO) {
        templateService.addTemplate(templateDTO);
        return ResponseEntity.ok("Template added successfully");
    }

    @PutMapping("/updateTemplate")
    public ResponseEntity<String> updateTemplate(@RequestParam Long tid, @RequestBody TemplateDTO templateDTO) {
        templateService.updateTemplate(tid, templateDTO);
        return ResponseEntity.ok("Template updated successfully");
    }

    @DeleteMapping("/deleteTemplate")
    public ResponseEntity<String> deleteTemplate(@RequestParam Long tid) {
        templateService.deleteTemplate(tid);
        return ResponseEntity.ok("Template deleted successfully");
    }

    @PostMapping("/addTempVariable")
    public ResponseEntity<String> addTempVariable(@RequestParam  Long tid, @RequestBody TempVariableDTO tempVariableDTO) {

//        log.info("controller - tid: {}", tid);
//        log.info("controller - tempVariableDTO: {}", tempVariableDTO);

        TemplateDTO tempDto = new TemplateDTO();
        tempDto.setTid(tid);
        tempVariableDTO.setTemplate(modelMapper.map(tempDto, Template.class));

        templateService.addTempVariable(tempVariableDTO);
        return ResponseEntity.ok("TempVariable added successfully");
    }

    @PutMapping("/updateTempVariable")
    public ResponseEntity<String> updateTempVariable(@RequestParam Long tvid, @RequestParam Long tid, @RequestBody TempVariableDTO tempVariableDTO) {

        TemplateDTO tempDto = new TemplateDTO();
        tempDto.setTid(tid);
        tempVariableDTO.setTemplate(modelMapper.map(tempDto, Template.class));

        templateService.updateTempVariable(tvid, tempVariableDTO);
        return ResponseEntity.ok("Template updated successfully");
    }

    @DeleteMapping("/deleteTempVar")
    public ResponseEntity<String> deleteTempVar(@RequestParam Long tvid) {
        templateService.deleteTempVar(tvid);
        return ResponseEntity.ok("Template deleted successfully");
    }

}
