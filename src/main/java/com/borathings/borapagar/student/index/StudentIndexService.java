package com.borathings.borapagar.student.index;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.student.StudentEntity;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class StudentIndexService {

    private final StudentIndexRepository studentIndexRepository;

    @Qualifier("userRestClient")
    private final RestClient userRestClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    @Transactional
    public CompletableFuture<Void> fetchIndexes(StudentEntity student) {
        try {

            List<IndexDTO> indexes = userRestClient
                    .get()
                    .uri("/discente/v1/indices-discentes?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<IndexDTO>>() {});

            List<StudentIndexEntity> studentIndexEntities = indexes.stream()
                    .map(idx -> StudentIndexEntity.builder()
                            .student(student)
                            .value(idx.value())
                            .name(IndexEnum.fromId(idx.indexId().intValue()).name())
                            .indexId(idx.indexId())
                            .studentIndexId(idx.studentIndexId())
                            .build())
                    .collect(Collectors.toList());

            studentIndexRepository.deleteAllByStudent(student);
            studentIndexRepository.saveAll(studentIndexEntities);

        } catch (Exception ex) {
            logger.error("Exception at fetchIndexes: {}", ex.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public Map<Long, BigDecimal> fetchRobustIeaMap(Set<Long> studentIds) {
        if (studentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<StudentIndexEntity> indexes = studentIndexRepository.findAllByNameEqualsAndStudentIdIn("IEA", studentIds);

        return indexes.stream()
                .filter(index -> index.getValue() != null && !index.getValue().isBlank())
                .collect(Collectors.toMap(
                        index -> index.getStudent().getStudentId(),
                        index -> {
                            try {
                                return new BigDecimal(index.getValue());
                            } catch (NumberFormatException e) {
                                System.err.println("IEA mal formatado para aluno "
                                        + index.getStudent().getStudentId()
                                        + ": "
                                        + index.getValue());
                                return BigDecimal.ZERO;
                            }
                        },
                        (existing, replacement) -> replacement));
    }
}
