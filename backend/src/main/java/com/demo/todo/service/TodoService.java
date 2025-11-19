package com.demo.todo.service;

import com.demo.todo.model.Todo;
import com.demo.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Long id, Todo todoDetails) {
        Todo todo = findById(id);
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.getCompleted());
        return todoRepository.save(todo);
    }

    public void delete(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }

    public List<Todo> findByCompleted(Boolean completed) {
        return todoRepository.findByCompleted(completed);
    }
}
