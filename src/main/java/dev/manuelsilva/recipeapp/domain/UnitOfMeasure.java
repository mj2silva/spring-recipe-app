package dev.manuelsilva.recipeapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document
public class UnitOfMeasure {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String uom;
}
