package com.example.todoApp.controller.todo;

import com.example.todoApp.controller.todo.request.TodoRequest;
import com.example.todoApp.model.TodoModel;
import com.example.todoApp.model.ValidateResult;
import com.example.todoApp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoService service;

    @Autowired
    public TodoController(TodoService service) {
        this.service = service;
    }
    
    // for select all todo to show in main screen
    @GetMapping(value = "/{userId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoModel> getAll(@PathVariable int userId) {
        return service.selectAll(userId);
    }

    // for insert todo
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody TodoRequest request) {
        ValidateResult validate = request.validate();
        if (!validate.ok()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, validate.errorMessage());
        }
        service.insertTodo(request.toModel());
    }

    // for update todo
    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable int id, @RequestBody TodoRequest request) {
        System.out.println("Received TodoRequest: " + request);
        ValidateResult validate = request.validate();
        if (!validate.ok()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, validate.errorMessage());
        }

        try {
            service.updateTodo(request.toModel(id));
        } catch (Exception e) {
            // for when update todo failed.s
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating todo", e);
        }
    }

    //for delete todo
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.deleteTodo(id);
    }
}
