package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.NotesCommand;
import dev.manuelsilva.recipeapp.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {
    @Override
    @Nullable
    @Synchronized
    public NotesCommand convert(Notes source) {
        final NotesCommand notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes(source.getRecipeNotes());
        notesCommand.setId(source.getId());
        return notesCommand;
    }
}
