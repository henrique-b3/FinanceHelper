package com.app.FinanceHelper.exceptions;

public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException(String resourceName, String resourceType) {
        super(buildMessage(resourceName, resourceType));
    }

    private static String buildMessage(String resourceName, String resourceType) {
        if (resourceType != null) {
            if (resourceType.equalsIgnoreCase("Company")) {
                return "Não é possível deletar a empresa " + resourceName + ", pois ela possui transações ativas associadas.";
            } else if (resourceType.equalsIgnoreCase("Category")) {
                return "Não é possível deletar a categoria " + resourceName + ", pois ela pode estar associada a Transações, Metas ou Empresas.";
            }
        }

        return "Violação de integridade de dados no recurso: " + resourceName + " (" + resourceType + ").";
    }
}