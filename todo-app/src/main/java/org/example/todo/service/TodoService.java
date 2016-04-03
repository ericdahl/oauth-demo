package org.example.todo.service;

import org.example.todo.model.Todo;
import org.example.todo.model.TodoStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class TodoService {
    private Map<String, List<Todo>> userTodos = new HashMap<>();


    public TodoService() {
        userTodos.put("myusername", Collections.singletonList(new Todo("first todo")));
    }

    public List<Todo> getTodos(String username) {
        return userTodos.get(username);
    }

    public TodoStats getStats() {
        final int total = userTodos.values().stream()
                .map(list -> list.size())
                .mapToInt(i -> i)
                .sum();
        return new TodoStats(total);
    }
}
