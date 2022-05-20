package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping({"", "/"})
    public String getIndex() {
        return "index";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("errors/404");
        modelAndView.addObject("error", exception);
        return modelAndView;
    }
}
