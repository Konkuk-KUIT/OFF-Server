package com.example.off.domain.task.repository;

import com.example.off.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProject_IdOrderByCreatedAtAsc(Long projectId);

    @Query("select t.project.id from Task t where t.id = :taskId")
    Long findProjectIdById(@Param("taskId") Long taskId);
}
