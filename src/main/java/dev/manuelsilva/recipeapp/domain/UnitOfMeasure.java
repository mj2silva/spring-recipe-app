package dev.manuelsilva.recipeapp.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
public class UnitOfMeasure {
    private String id;
    private String uom;
}
