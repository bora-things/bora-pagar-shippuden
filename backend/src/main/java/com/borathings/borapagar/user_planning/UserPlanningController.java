package com.borathings.borapagar.user_planning;

import com.borathings.borapagar.user_planning.dto.CreateUserPlanningDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping("/user/planning")
public interface UserPlanningController {

    /**
     * Faz o usuário adicionar a disciplina no seu planejamento
     *
     * @param planningDto - CreateUserPlanningDTO - DTO com as informações do elemento do
     *     planejamento
     * @param subjectId - Long - ID da disciplina
     * @return ResponseEntity<UserPlanningEntity> - Elemento do planejamento criado
     */
    @PostMapping("/subject/{subjectId}")
    public ResponseEntity<UserPlanningEntity> createPlanning(
            @RequestBody @Valid CreateUserPlanningDTO planningDto, @PathVariable Long subjectId);

    /**
     * Retorna todos os elementos do planejamento do usuário autenticado
     *
     * @return ResponseEntity<List<UserPlanning>> - Elementos do planejamento do usuário
     */
    @GetMapping
    public ResponseEntity<List<UserPlanningEntity>> findPlanningFromAuthenticatedUser();

    /**
     * Retorna informações sobre um interesse específico do usuário
     *
     * @param subjectId - Long - Id da disciplina que o usuário manifestou interesse
     * @return ResponseEntity<UserPlanningEntity> - Entidade com informações sobre o interesse do
     *     usuário nesta disciplina
     */
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<UserPlanningEntity> findSpecificElement(@PathVariable Long subjectId);
}
