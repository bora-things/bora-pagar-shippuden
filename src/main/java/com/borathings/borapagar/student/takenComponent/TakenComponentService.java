package com.borathings.borapagar.student.takenComponent;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.takenComponent.dto.TakenComponentDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TakenComponentService {

    @Autowired
    private TakenComponentRepository repository;

    @Transactional
    public List<TakenComponentEntity> batchInsertDTOs(List<TakenComponentDTO> componentDTOs, StudentEntity student) {
        Map<String, TakenComponentDTO> uniqueDTOsMap = componentDTOs.stream()
                .collect(Collectors.toMap(
                        dto -> dto.componentId() + "-" + dto.period() + "-" + dto.year(),
                        dto -> dto,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new));

        List<TakenComponentDTO> filteredDTOs = new ArrayList<>(uniqueDTOsMap.values());

        List<TakenComponentEntity> componentEntities = filteredDTOs.stream()
                .map(dto -> {
                    return TakenComponentEntity.builder()
                            .absences(dto.absences())
                            .registerDate(dto.registerDate())
                            .finalGrade(dto.finalGrade())
                            .componentId(dto.componentId())
                            .sigaaClassId(dto.sigaaClassId())
                            .situation(dto.registrationSituationId())
                            .period(dto.period())
                            .year(dto.year())
                            .student(student)
                            .build();
                })
                .collect(Collectors.toList());

        repository.deleteAllByStudent(student);
        return repository.saveAll(componentEntities);
    }

    public List<TakenComponentEntity> findByStudent(StudentEntity student) {
        return repository.findAllByStudent(student);
    }

    public Optional<TakenComponentEntity> findByComponentId(Integer id) {
        return repository.findByComponentId(id);
    }
}
