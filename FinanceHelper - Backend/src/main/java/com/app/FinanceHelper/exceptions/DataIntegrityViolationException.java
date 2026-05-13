package com.app.FinanceHelper.exceptions;

public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException(String resourceName, String resourceType) {
        super(buildMessage(resourceName, resourceType));
    }

    private static String buildMessage(String resourceName, String resourceType) {
        // Agora retorna apenas uma chave para i18n em conjunto com arg1 (resourceName)
        // Isso depende de o handler formatar com essa string caso retorne, se não retorna uma genérica.
        // A lógica ideal seria enviar "error.db.foreignkey" caso "Company" etc, mas o
        // handleSpringDataIntegrityViolationException cuidará de interceptar os genéricos bem.
        // Vamos retornar apenas a chave com os argumentos se for custom para front end.

        if (resourceType != null) {
            if (resourceType.equalsIgnoreCase("Company")) {
                return "Não é possível deletar a empresa " + resourceName + ", pois ela possui transações ativas associadas.";
            } else if (resourceType.equalsIgnoreCase("Category")) {
                return "Não é possível deletar a categoria " + resourceName + ", pois ela pode estar associada a Transações, Metas ou Empresas.";
            }
        }

        return "Violação de integridade de dados no recurso: " + resourceName + ".";
    }
}