package com.app.FinanceHelper.payload.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {

    @NotBlank(message = "{category.name.notblank}")
    private String name;

    private String description;

    @Size(min = 3, max = 50, message = "{category.image.size}")
    private String image;

    private String color;
}
