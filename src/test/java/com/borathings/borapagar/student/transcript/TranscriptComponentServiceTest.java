package com.borathings.borapagar.student.transcript;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.transcript.dto.TranscriptComponentDTO;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TranscriptComponentServiceTest {

    @Mock
    private TranscriptComponentRepository repository;

    @InjectMocks
    private TranscriptComponentService service;

    private StudentEntity student;
    private List<TranscriptComponentDTO> componentDTOs;
    private List<TranscriptComponentEntity> expectedEntities;

    @BeforeEach
    void setUp() {
        student = StudentEntity.builder().id(1L).build();
        LocalDate date1 = LocalDate.of(2023, 1, 15);
        LocalDate date2 = LocalDate.of(2023, 1, 20);

        componentDTOs = Arrays.asList(
                new TranscriptComponentDTO(
                        2023,
                        1,
                        date1.toEpochDay(),
                        5, // absences
                        101, // componentId
                        1001, // studentId
                        2001, // registrationId
                        1, // registrationSituationId
                        "REG", // integralizationKindId
                        101 // sigaaClassId (int, not String)
                        ),
                new TranscriptComponentDTO(
                        2023, // year
                        1, // period
                        date2.toEpochDay(),
                        2, // absences
                        102, // componentId
                        1001, // studentId
                        2002, // registrationId
                        1, // registrationSituationId
                        "REG", // integralizationKindId
                        102 // sigaaClassId
                        ));

        expectedEntities = Arrays.asList(
                TranscriptComponentEntity.builder()
                        .absences(5)
                        .registerDate(date1.toEpochDay())
                        .sigaaClassId(101)
                        .period(1)
                        .year(2023)
                        .student(student)
                        .build(),
                TranscriptComponentEntity.builder()
                        .absences(2)
                        .registerDate(date2.toEpochDay())
                        .sigaaClassId(102)
                        .period(1)
                        .year(2023)
                        .student(student)
                        .build());
        when(repository.saveAll(anyList())).thenReturn(expectedEntities);
    }

    @Test
    void testBatchInsertDTOs() {
        List<TranscriptComponentEntity> result = service.batchInsertDTOs(componentDTOs, student);

        verify(repository, times(1)).saveAll(anyList());

        assertNotNull(result);
        assertEquals(componentDTOs.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            TranscriptComponentEntity actual = result.get(i);
            TranscriptComponentDTO dto = componentDTOs.get(i);

            assertEquals(dto.absences(), actual.getAbsences());
            assertEquals(dto.registerDate(), actual.getRegisterDate());
            assertEquals(dto.sigaaClassId(), actual.getSigaaClassId());
            assertEquals(dto.period(), actual.getPeriod());
            assertEquals(dto.year(), actual.getYear());
            assertEquals(student, actual.getStudent());
        }
    }

    @Test
    void testBatchInsertDTOs_EmptyList() {
        List<TranscriptComponentEntity> result = service.batchInsertDTOs(List.of(), student);

        verify(repository, times(1)).saveAll(anyList());
        assertNotNull(result);
    }
}
