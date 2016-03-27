package org.example.todo.service;

import org.example.todo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoService {
    private Map<String, List<Todo>> userTodos = new HashMap<String, List<Todo>>();


    public TodoService() {
        userTodos.put("myuser", Collections.singletonList(new Todo("first todo")));
    }

    public List<Todo> getTodos(String username) {
        return userTodos.get(username);
    }
}
