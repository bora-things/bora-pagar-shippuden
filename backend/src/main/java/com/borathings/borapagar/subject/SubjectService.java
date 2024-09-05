package com.borathings.borapagar.subject;

import com.borathings.borapagar.subject.dto.request.CreateSubjectDTO;
import com.borathings.borapagar.subject.dto.request.UpdateSubjectDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    @Autowired private SubjectRepository subjectRepository;

    /**
     * Salva uma nova disciplina no banco de dados
     *
     * @param subjectDTO - Dados da disciplina
     * @return Disciplina salva
     */
    public SubjectEntity create(CreateSubjectDTO subjectDTO) {
        return subjectRepository.save(subjectDTO.toEntity());
    }

    /**
     * Retorna todas as disciplinas cadastradas
     *
     * @return Lista de disciplinas
     */
    public List<SubjectEntity> findAll() {
        return subjectRepository.findAll();
    }

    /**
     * Busca uma disciplina pelo id. Lança uma exceção caso a disciplina não seja encontrada
     *
     * @throws EntityNotFoundException se a disciplina não existir
     * @param id
     * @return Disciplina encontrada
     */
    public SubjectEntity findByIdOrError(Long id) {
        SubjectEntity subject =
                subjectRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Disciplina com id " + id + " não encontrada"));
        return subject;
    }

    /**
     * Atualiza os dados de uma disciplina
     *
     * @param id - Id da disciplina
     * @param subjectDTO - Novos dados da disciplina
     * @throws EntityNotFoundException se a disciplina não existir
     * @return Disciplina atualizada
     */
    public SubjectEntity update(Long id, UpdateSubjectDTO subjectDTO) {
        findByIdOrError(id);
        SubjectEntity subjectEntity = subjectDTO.toEntity();
        subjectEntity.setId(id);
        return subjectRepository.save(subjectEntity);
    }

    /**
     * Deleta uma disciplina pelo id
     *
     * @param id - Id da disciplina
     */
    public void delete(Long id) {
        subjectRepository.softDeleteById(id);
    }
}
