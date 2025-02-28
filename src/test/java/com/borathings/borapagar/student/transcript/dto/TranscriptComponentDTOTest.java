package com.borathings.borapagar.student.transcript.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class TranscriptComponentDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testConstructorAndAccessors() {
        int year = 2023;
        int period = 1;
        long registerDate = LocalDate.of(2023, 1, 15).toEpochDay();
        int absences = 5;
        int componentId = 101;
        int studentId = 1001;
        int registrationId = 2001;
        int registrationSituationId = 1;
        String integralizationKindId = "REG";
        int sigaaClassId = 201;

        TranscriptComponentDTO dto = new TranscriptComponentDTO(
                year,
                period,
                registerDate,
                absences,
                componentId,
                studentId,
                registrationId,
                registrationSituationId,
                integralizationKindId,
                sigaaClassId);

        assertEquals(year, dto.year());
        assertEquals(period, dto.period());
        assertEquals(registerDate, dto.registerDate());
        assertEquals(absences, dto.absences());
        assertEquals(componentId, dto.componentId());
        assertEquals(studentId, dto.studentId());
        assertEquals(registrationId, dto.registrationId());
        assertEquals(registrationSituationId, dto.registrationSituationId());
        assertEquals(integralizationKindId, dto.integralizationKindId());
        assertEquals(sigaaClassId, dto.sigaaClassId());
    }

    @Test
    void testEquality() {
        long registerDate = LocalDate.of(2023, 1, 15).toEpochDay();

        TranscriptComponentDTO dto1 =
                new TranscriptComponentDTO(2023, 1, registerDate, 5, 101, 1001, 2001, 1, "REG", 201);

        TranscriptComponentDTO dto2 =
                new TranscriptComponentDTO(2023, 1, registerDate, 5, 101, 1001, 2001, 1, "REG", 201);

        TranscriptComponentDTO differentDto =
                new TranscriptComponentDTO(2023, 2, registerDate, 5, 101, 1001, 2001, 1, "REG", 201);

        assertEquals(dto1, dto2, "Equal DTOs should be equal");
        assertNotEquals(dto1, differentDto, "DTOs with different values should not be equal");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Equal DTOs should have equal hash codes");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json =
                """
				{
				    "ano": 2023,
				    "periodo": 1,
				    "data-cadastro": 19372,
				    "faltas": 5,
				    "id-componente": 101,
				    "id-discente": 1001,
				    "id-matricula-componente": 2001,
				    "id-situacao-matricula": 1,
				    "id-tipo-integralizacao": "REG",
				    "id-turma": 201
				}
				""";

        TranscriptComponentDTO dto = objectMapper.readValue(json, TranscriptComponentDTO.class);

        assertEquals(2023, dto.year());
        assertEquals(1, dto.period());
        assertEquals(19372, dto.registerDate());
        assertEquals(5, dto.absences());
        assertEquals(101, dto.componentId());
        assertEquals(1001, dto.studentId());
        assertEquals(2001, dto.registrationId());
        assertEquals(1, dto.registrationSituationId());
        assertEquals("REG", dto.integralizationKindId());
        assertEquals(201, dto.sigaaClassId());
    }

    @Test
    void testJsonSerialization() throws Exception {
        long epochDay = LocalDate.of(2023, 1, 15).toEpochDay();
        TranscriptComponentDTO dto = new TranscriptComponentDTO(2023, 1, epochDay, 5, 101, 1001, 2001, 1, "REG", 201);

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"ano\":2023"));
        assertTrue(json.contains("\"periodo\":1"));
        assertTrue(json.contains("\"data-cadastro\":" + epochDay));
        assertTrue(json.contains("\"faltas\":5"));
        assertTrue(json.contains("\"id-componente\":101"));
        assertTrue(json.contains("\"id-discente\":1001"));
        assertTrue(json.contains("\"id-matricula-componente\":2001"));
        assertTrue(json.contains("\"id-situacao-matricula\":1"));
        assertTrue(json.contains("\"id-tipo-integralizacao\":\"REG\""));
        assertTrue(json.contains("\"id-turma\":201"));
    }

    @Test
    void testToString() {
        long registerDate = LocalDate.of(2023, 1, 15).toEpochDay();
        TranscriptComponentDTO dto =
                new TranscriptComponentDTO(2023, 1, registerDate, 5, 101, 1001, 2001, 1, "REG", 201);

        String dtoString = dto.toString();

        assertTrue(dtoString.contains("year=2023"));
        assertTrue(dtoString.contains("period=1"));
        assertTrue(dtoString.contains("registerDate=" + registerDate));
        assertTrue(dtoString.contains("absences=5"));
        assertTrue(dtoString.contains("componentId=101"));
        assertTrue(dtoString.contains("studentId=1001"));
        assertTrue(dtoString.contains("registrationId=2001"));
        assertTrue(dtoString.contains("registrationSituationId=1"));
        assertTrue(dtoString.contains("integralizationKindId=REG"));
        assertTrue(dtoString.contains("sigaaClassId=201"));
    }

    @Test
    void testNullIntegralizationKindId() {
        TranscriptComponentDTO dto = new TranscriptComponentDTO(2023, 1, 19372L, 5, 101, 1001, 2001, 1, null, 201);

        assertNull(dto.integralizationKindId(), "integralizationKindId should allow null values");
    }
}
