package dev.manuelsilva.recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCommand {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 255)
    private String description;
}
