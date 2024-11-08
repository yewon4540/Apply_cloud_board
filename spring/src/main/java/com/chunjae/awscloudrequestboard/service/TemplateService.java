package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.dto.TempVariableDTO;
import com.chunjae.awscloudrequestboard.dto.TemplateDTO;

import java.util.List;

public interface TemplateService {
    List<TemplateDTO> getTemplateList();

    List<TempVariableDTO> getTempVariableList();

    void addTemplate(TemplateDTO templateDTO);

    void addTempVariable(TempVariableDTO tempVariableDTO);

    void deleteTemplate(Long tid);

    void deleteTempVar(Long tvid);

    void updateTemplate(Long tid, TemplateDTO templateDTO);

    void updateTempVariable(Long tvid, TempVariableDTO tempVariableDTO);
}
