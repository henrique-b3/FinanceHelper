package com.app.FinanceHelper.exceptions;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String field;
    String fieldName;
    UUID fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName){
        super(String.format("Nenhum(a) %s encontrado(a) com %s: '%s'", translateResourceName(resourceName), field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, UUID fieldId){
        super(String.format("Nenhum(a) %s encontrado(a) com %s: '%s'", translateResourceName(resourceName), field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    private static String translateResourceName(String resourceName) {
        if (resourceName == null) return "Recurso";
        if (resourceName.equalsIgnoreCase("UserProfile") || resourceName.equalsIgnoreCase("User")) return "Usuário";
        if (resourceName.equalsIgnoreCase("Category")) return "Categoria";
        if (resourceName.equalsIgnoreCase("Company")) return "Empresa";
        if (resourceName.equalsIgnoreCase("Transaction")) return "Transação";
        if (resourceName.equalsIgnoreCase("Goal")) return "Objetivo";
        return resourceName;
    }
}
