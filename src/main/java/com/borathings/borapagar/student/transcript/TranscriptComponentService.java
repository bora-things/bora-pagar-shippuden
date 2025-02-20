package com.borathings.borapagar.student.transcript;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.transcript.dto.TranscriptComponentDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranscriptComponentService {

    @Autowired
    private TranscriptComponentRepository repository;

    public List<TranscriptComponentEntity> batchInsertDTOs(
            List<TranscriptComponentDTO> componentDTOs, StudentEntity student) {
        List<TranscriptComponentEntity> componentEntities = componentDTOs.stream()
                .map(dto -> {
                    return TranscriptComponentEntity.builder()
                            .absences(dto.absences())
                            .registerDate(dto.registerDate())
                            .sigaaClassId(dto.sigaaClassId())
                            .period(dto.period())
                            .year(dto.year())
                            .student(student)
                            .build();
                })
                .collect(Collectors.toList());

        return repository.saveAll(componentEntities);
    }
}
