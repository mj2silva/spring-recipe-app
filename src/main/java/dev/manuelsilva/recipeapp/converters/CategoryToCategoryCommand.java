package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.CategoryCommand;
import dev.manuelsilva.recipeapp.domain.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {
    @Override
    @Nullable
    @Synchronized
    public CategoryCommand convert(@Nullable Category source) {
        if (source == null) return null;
        final CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());
        return categoryCommand;
    }
}
