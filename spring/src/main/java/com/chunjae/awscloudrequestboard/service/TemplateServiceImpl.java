package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.domain.TempVariable;
import com.chunjae.awscloudrequestboard.domain.Template;
import com.chunjae.awscloudrequestboard.dto.TempVariableDTO;
import com.chunjae.awscloudrequestboard.dto.TemplateDTO;
import com.chunjae.awscloudrequestboard.repository.TempVariableRepository;
import com.chunjae.awscloudrequestboard.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {
    private final TemplateRepository templateRepository;
    private final TempVariableRepository tempVariableRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<TemplateDTO> getTemplateList() {
        List<Template> templateList = templateRepository.findAll(Sort.by(Sort.Direction.DESC, "tid"));
        List<TemplateDTO> templateDTOList = templateList.stream()
                .map(item -> modelMapper.map(item, TemplateDTO.class))
                .collect(Collectors.toList());
        return templateDTOList;
    }

    @Override
    public List<TempVariableDTO> getTempVariableList() {
        List<TempVariable> tempVariableList = tempVariableRepository.findAll(Sort.by(Sort.Direction.DESC, "tvid"));

        List<TempVariableDTO> tempVariableDTOList = tempVariableList.stream()
                .map(item->modelMapper.map(item, TempVariableDTO.class))
                .collect(Collectors.toList());

//        log.info("service_lists..............{}",tempVariableList.get(0).getTemplate().getTid());
        return tempVariableDTOList;
    }

    @Override
    public void addTemplate(TemplateDTO templateDTO) {
        Template template = modelMapper.map(templateDTO, Template.class);

        log.info("tid: {}",template.getTid());

        templateRepository.save(template);
    }

    @Override
    public void addTempVariable(TempVariableDTO tempVariableDTO) {
        TempVariable tempVariable = modelMapper.map(tempVariableDTO, TempVariable.class);

        tempVariableRepository.save(tempVariable);
    }

    @Override
    public void deleteTemplate(Long tid) {
        templateRepository.deleteById(tid); // tid로 항목을 삭제
    }

    @Override
    public void deleteTempVar(Long tvid) {
        tempVariableRepository.deleteById(tvid); // tvid로 항목을 삭제
    }

    @Override
    public void updateTemplate(Long tid, TemplateDTO templateDTO) {
        Template template = templateRepository.findById(tid)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        modelMapper.map(templateDTO, template);
        templateRepository.save(template);
    }

    @Override
    public void updateTempVariable(Long tvid, TempVariableDTO tempVariableDTO) {
        TempVariable tempVariable = tempVariableRepository.findById(tvid)
                .orElseThrow(() -> new RuntimeException("TempVariable not found"));
        modelMapper.map(tempVariableDTO, tempVariable);
        tempVariableRepository.save(tempVariable);
    }
}
