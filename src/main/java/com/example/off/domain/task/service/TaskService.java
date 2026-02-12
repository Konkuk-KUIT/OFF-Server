package com.example.off.domain.task.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.project.Project;
import com.example.off.domain.project.repository.ProjectRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import com.example.off.domain.task.Task;
import com.example.off.domain.task.ToDo;
import com.example.off.domain.task.dto.*;
import com.example.off.domain.task.repository.TaskRepository;
import com.example.off.domain.task.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ToDoRepository toDoRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public CreateTaskResponse createTask(Long memberId, Long projectId, CreateTaskRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        validateProjectAccess(memberId, project);

        ProjectMember assignee = projectMemberRepository.findById(request.getProjectMemberId())
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // 담당자가 해당 프로젝트에 속하는지 검증
        if (!assignee.getProject().getId().equals(projectId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        Task task = Task.of(request.getName(), request.getDescription(), project, assignee);
        taskRepository.save(task);

        if (request.getToDoList() != null) {
            for (String content : request.getToDoList()) {
                ToDo toDo = ToDo.of(content, task);
                toDoRepository.save(toDo);
                task.getToDoList().add(toDo);
            }
        }

        return CreateTaskResponse.of(task.getId());
    }

    @Transactional
    public UpdateTaskResponse updateTask(Long memberId, Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new OffException(ResponseCode.TASK_NOT_FOUND));

        validateProjectAccess(memberId, task.getProject());

        ProjectMember assignee = projectMemberRepository.findById(request.getProjectMemberId())
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // 담당자가 해당 프로젝트에 속하는지 검증
        if (!assignee.getProject().getId().equals(task.getProject().getId())) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        task.update(request.getName(), request.getDescription(), assignee);

        // ToDo 리스트 업데이트 (제공된 경우)
        if (request.getToDoList() != null) {
            // 요청에 포함된 기존 ToDo ID 수집
            java.util.Set<Long> requestedIds = request.getToDoList().stream()
                    .map(UpdateTaskRequest.ToDoItem::getId)
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());

            // 요청에 없는 기존 ToDo 삭제
            java.util.List<ToDo> toDelete = task.getToDoList().stream()
                    .filter(todo -> !requestedIds.contains(todo.getId()))
                    .toList();
            toDoRepository.deleteAll(toDelete);
            task.getToDoList().removeAll(toDelete);

            // 업데이트 또는 신규 생성
            for (UpdateTaskRequest.ToDoItem item : request.getToDoList()) {
                if (item.getId() != null) {
                    // 기존 ToDo 업데이트
                    ToDo existingToDo = task.getToDoList().stream()
                            .filter(todo -> todo.getId().equals(item.getId()))
                            .findFirst()
                            .orElseThrow(() -> new OffException(ResponseCode.TODO_NOT_FOUND));
                    existingToDo.updateContent(item.getContent());
                } else {
                    // 신규 ToDo 생성
                    ToDo newToDo = ToDo.of(item.getContent(), task);
                    toDoRepository.save(newToDo);
                    task.getToDoList().add(newToDo);
                }
            }
        }

        return UpdateTaskResponse.of(task.getId());
    }

    @Transactional
    public void deleteTask(Long memberId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new OffException(ResponseCode.TASK_NOT_FOUND));

        validateProjectAccess(memberId, task.getProject());

        taskRepository.delete(task);
    }

    @Transactional
    public ToggleToDoResponse toggleToDo(Long memberId, Long taskId, Long toDoId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new OffException(ResponseCode.TASK_NOT_FOUND));

        validateProjectAccess(memberId, task.getProject());

        ToDo toDo = toDoRepository.findById(toDoId)
                .orElseThrow(() -> new OffException(ResponseCode.TODO_NOT_FOUND));

        toDo.toggleDone();

        int progressPercent = calculateTaskProgress(task);

        return ToggleToDoResponse.of(toDo.getId(), toDo.getIsDone(), progressPercent);
    }

    private int calculateTaskProgress(Task task) {
        if (task.getToDoList().isEmpty()) return 0;
        long done = task.getToDoList().stream().filter(ToDo::getIsDone).count();
        return (int) (done * 100 / task.getToDoList().size());
    }

    private void validateProjectAccess(Long memberId, Project project) {
        if (project.getCreator().getId().equals(memberId)) return;
        boolean isMember = project.getProjectMembers().stream()
                .anyMatch(pm -> pm.getMember().getId().equals(memberId));
        if (!isMember) throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
    }
}
