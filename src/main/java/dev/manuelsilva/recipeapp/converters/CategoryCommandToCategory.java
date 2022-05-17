package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.CategoryCommand;
import dev.manuelsilva.recipeapp.domain.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {
    @Override
    @Nullable
    @Synchronized
    public Category convert(@Nullable CategoryCommand source) {
        if (source == null) return null;
        final Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
