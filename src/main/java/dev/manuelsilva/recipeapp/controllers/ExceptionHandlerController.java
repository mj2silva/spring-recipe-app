package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFoundExceptionHandler(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("errors/404");
        modelAndView.addObject("error", exception);
        return modelAndView;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView wrongIdFormatException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("errors/400WrongId");
        modelAndView.addObject("error", exception);
        return modelAndView;
    }
}
