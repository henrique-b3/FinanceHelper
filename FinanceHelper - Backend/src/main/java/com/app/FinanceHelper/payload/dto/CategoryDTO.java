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

    @NotBlank(message = "Category Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String name;

    String description;

    @Size(min = 3, max = 50, message = "The image path must has more than {min} and less than {max} characters")
    String image;

    String color;
}
