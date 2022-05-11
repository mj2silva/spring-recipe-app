package dev.manuelsilva.recipeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


class Odi {
    private final Manuel manuel;
    public Odi() {
        this.manuel = new Manuel();
    }
    public void pensarEnManuel() {
        System.out.println("Odi está pensando en Manuel");
        manuel.pensarEnOdi();
    }
}
class Manuel {
    private final Odi odi;
    public Manuel() {
        this.odi = new Odi();
    }
    public void pensarEnOdi() {
        System.out.println("Manuel está pensando en Odi");
        odi.pensarEnManuel();
    }
}
public class RecipeAppApplication {
    public static void main(String[] args) {
        Odi odi = new Odi();
        odi.pensarEnManuel();
    }
        // SpringApplication.run(RecipeAppApplication.class, args);

}
