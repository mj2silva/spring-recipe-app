package dev.manuelsilva.recipeapp.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {
    /*@ExceptionHandler(NotFoundException.class)
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
    }*/
}
