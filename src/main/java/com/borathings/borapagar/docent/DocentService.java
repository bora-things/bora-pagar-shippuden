package com.borathings.borapagar.docent;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.docent.dto.DocentDTO;
import com.borathings.borapagar.docent.dto.DocentEvaluationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class DocentService {

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    public List<DocentDTO> findDocentsOnClassrooms(List<ClassroomEntity> classrooms) {
        List<DocentDTO> docentsResponse = new ArrayList<>();
        for (ClassroomEntity classroom : classrooms) {
            Long classroomId = classroom.getClassroomId();
            List<DocentDTO> docents = serviceRestClient
                    .get()
                    .uri("/turma/v1/turmas/" + classroomId + "/docentes")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DocentDTO>>() {
                    });

            if (docents != null && !docents.isEmpty()) {
                docentsResponse.addAll(docents);
            }
        }
        return docentsResponse;
    }


    public List<DocentEvaluationDTO> findDocentsEvaluation(String componentCode,List<ClassroomEntity> classrooms) {
        Set<DocentEvaluationDTO> docentsEvaluationSet = new HashSet<>();
        List<DocentDTO> docents = findDocentsOnClassrooms(classrooms);

        for (DocentDTO docent : docents) {
            if (docent == null || componentCode == null || docent.teacherId() == null) {
                continue;
            }


            List<DocentEvaluationDTO> docentsEvaluation = serviceRestClient
                    .get()
                    .uri("/avaliacao-institucional/v1/avaliacoes-docentes?id-unidade="+classrooms.getFirst().getUnitId()+"&id-docente="+docent.teacherId()+"&codigo-componente="+componentCode)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DocentEvaluationDTO>>() {});

            if (docentsEvaluation != null && !docentsEvaluation.isEmpty()) {
                for (DocentEvaluationDTO item : docentsEvaluation) {
                    DocentEvaluationDTO dto = new DocentEvaluationDTO();
                    dto.setEvaluationId(item.getEvaluationId());
                    dto.setTeacherName(item.getTeacherName());
                    dto.setTeacherId(item.getTeacherId());
                    dto.setUnitCode(item.getUnitCode());
                    dto.setUnitId(item.getUnitId());
                    dto.setComponentName(item.getComponentName());
                    dto.setComponentCode(item.getComponentCode());
                    dto.setClassCode(item.getClassCode());
                    dto.setClassSchedule(item.getClassSchedule());
                    dto.setStudentCount(item.getStudentCount());
                    dto.setGeneralAverage(item.getGeneralAverage());
                    dto.setGeneralStandardDeviation(item.getGeneralStandardDeviation());
                    dto.setClassYear(item.getClassYear());
                    dto.setClassPeriod(item.getClassPeriod());
                    dto.setCpf(docent.cpf());
                    docentsEvaluationSet.add(dto);
                }
            }
        }
        return docentsEvaluationSet.stream().toList();

    }


}
