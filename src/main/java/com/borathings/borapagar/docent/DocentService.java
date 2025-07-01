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

    public List<Long> findDocentsOnClassrooms(List<ClassroomEntity> classrooms) {
        List<Long> docentIds = new ArrayList<>();

        for (ClassroomEntity classroom : classrooms) {
            Long classroomId = classroom.getClassroomId();
            List<DocentDTO> docents = serviceRestClient
                    .get()
                    .uri("/turma/v1/turmas/" + classroomId + "/docentes")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DocentDTO>>() {});

            for (DocentDTO docent : docents) {
                docentIds.add(docent.teacherId());
            }
        }

        return docentIds;
    }


    public List<DocentEvaluationDTO> findDocentsEvaluation(String componentCode,List<ClassroomEntity> classrooms) {
        int unitId=6069; //Buscar apenas no IMD
        Set<DocentEvaluationDTO> docentsEvaluationSet = new HashSet<>();
        List<Long> docentsIds = findDocentsOnClassrooms(classrooms);
        for (Long docentId : docentsIds) {
            if (docentId == null || componentCode == null) {
                continue;
            }

            List<DocentEvaluationDTO> docentsEvaluation = serviceRestClient
                    .get()
                    .uri("/avaliacao-institucional/v1/avaliacoes-docentes?id-unidade="+unitId+"&id-docente="+docentId+"&codigo-componente="+componentCode)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DocentEvaluationDTO>>() {});

            if(docentsEvaluation!=null && docentsEvaluation.size()>0){
                docentsEvaluationSet.addAll(docentsEvaluation);
            }
        }
        return docentsEvaluationSet.stream().toList();

    }


}
