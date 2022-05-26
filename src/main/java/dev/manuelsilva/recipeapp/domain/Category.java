package dev.manuelsilva.recipeapp.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document
@EqualsAndHashCode(exclude = {"recipes"})
public class Category {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String description;
    @DBRef
    private List<Recipe> recipes;
}
