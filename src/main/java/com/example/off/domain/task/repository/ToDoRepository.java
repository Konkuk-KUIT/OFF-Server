package com.example.off.domain.task.repository;

import com.example.off.domain.task.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
