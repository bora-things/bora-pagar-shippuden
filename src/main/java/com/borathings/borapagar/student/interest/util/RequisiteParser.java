package com.borathings.borapagar.student.interest.util;

import com.borathings.borapagar.student.interest.exception.PreRequisitesNotCompletedException;
import java.util.*;

/**
 * Analisa uma expressão de pré-requisitos e pode validar se eles foram cumpridos, lançando uma exceção em caso de
 * falha.
 */
public class RequisiteParser {

    private final Set<String> completed;

    public RequisiteParser(Set<String> completed) {
        this.completed = Objects.requireNonNull(completed, "O conjunto de matérias concluídas não pode ser nulo.");
    }

    /**
     * Verifica se os pré-requisitos da expressão foram atendidos. Não faz nada se os requisitos forem cumpridos. Lança
     * PreRequisitesNotCompletedException se houver pendências.
     *
     * @param expression A string da expressão a ser avaliada.
     * @throws PreRequisitesNotCompletedException se a validação falhar.
     */
    public void assertRequisitesAreMet(String expression) throws PreRequisitesNotCompletedException {
        // Se a expressão for nula ou vazia, não há requisitos a cumprir.
        if (expression == null || expression.trim().isEmpty()) {
            return; // Sucesso, nenhum requisito a ser verificado.
        }

        List<String> tokens = tokenize(expression);
        EvaluationResult finalResult = evaluateRecursive(tokens);

        // Se o resultado da avaliação não for válido, lance a exceção.
        if (!finalResult.isValid()) {
            String requirementMessage =
                    "É necessário pagar: " + finalResult.requirementString().replace("OU", "ou");
            throw new PreRequisitesNotCompletedException(requirementMessage);
        }
        // Se for válido, o método simplesmente termina a execução.
    }

    // -----------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES PRIVADOS (Lógica interna para avaliação da expressão)
    // -----------------------------------------------------------------------------------

    private List<String> tokenize(String expr) {
        return new ArrayList<>(Arrays.asList(
                expr.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+")));
    }

    private EvaluationResult evaluateRecursive(List<String> tokens) {
        Deque<EvaluationResult> values = new ArrayDeque<>();
        Deque<String> ops = new ArrayDeque<>();

        while (!tokens.isEmpty()) {
            String token = tokens.remove(0);
            if (token.isEmpty()) continue;

            switch (token) {
                case "(":
                    values.push(evaluateRecursive(tokens));
                    break;
                case ")":
                    return resolveStack(values, ops);
                case "E", "OU":
                    ops.push(token);
                    break;
                default:
                    boolean isCompleted = completed.contains(token);
                    String requirement = isCompleted ? "" : token;
                    values.push(new EvaluationResult(isCompleted, requirement));
                    break;
            }
        }
        return resolveStack(values, ops);
    }

    private EvaluationResult resolveStack(Deque<EvaluationResult> values, Deque<String> ops) {
        while (!ops.isEmpty()) {
            String op = ops.removeLast();
            if (values.size() < 2) throw new IllegalArgumentException("Expressão de pré-requisitos mal formada.");

            EvaluationResult b = values.pop();
            EvaluationResult a = values.pop();

            values.push(applyOperator(op, a, b));
        }
        return values.isEmpty() ? new EvaluationResult(true, "") : values.pop();
    }

    private EvaluationResult applyOperator(String op, EvaluationResult a, EvaluationResult b) {
        boolean isValid;
        String requirementString = "";

        if ("E".equals(op)) {
            isValid = a.isValid() && b.isValid();
            if (!isValid) {
                if (!a.isValid() && !b.isValid()) {
                    requirementString =
                            formatRequirement(a.requirementString()) + " E " + formatRequirement(b.requirementString());
                } else if (!a.isValid()) {
                    requirementString = a.requirementString();
                } else {
                    requirementString = b.requirementString();
                }
            }
        } else if ("OU".equals(op)) {
            isValid = a.isValid() || b.isValid();
            if (!isValid) {
                requirementString =
                        formatRequirement(a.requirementString()) + " OU " + formatRequirement(b.requirementString());
            }
        } else {
            throw new IllegalArgumentException("Operador de pré-requisito inválido: " + op);
        }

        return new EvaluationResult(isValid, requirementString);
    }

    private String formatRequirement(String req) {
        if (req.contains(" E ") || req.contains(" OU ")) {
            return "(" + req + ")";
        }
        return req;
    }

    private record EvaluationResult(boolean isValid, String requirementString) {}
}
