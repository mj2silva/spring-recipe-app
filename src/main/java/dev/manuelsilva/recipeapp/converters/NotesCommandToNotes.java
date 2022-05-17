package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.NotesCommand;
import dev.manuelsilva.recipeapp.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {
    @Nullable
    @Override
    public Notes convert(@Nullable NotesCommand source) {
        if (source == null) return null;
        final Notes notes = new Notes();
        notes.setRecipeNotes(source.getRecipeNotes());
        notes.setId(source.getId());
        return notes;
    }
}
