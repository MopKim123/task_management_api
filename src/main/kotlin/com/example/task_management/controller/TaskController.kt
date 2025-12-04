package com.example.task_management.controller

import com.example.task_management.model.dto.CreateTaskRequest
import com.example.task_management.model.dto.UpdateTaskRequest
import com.example.task_management.service.intr.BoardService
import com.example.task_management.service.intr.TaskService
import com.example.task_management.service.intr.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boards/{boardId}/tasks")
class TaskController(
    private val taskService: TaskService,
    private val boardService: BoardService,
    private val userService: UserService
) {

    @PostMapping
    fun createTask(
        @PathVariable boardId: Long,
        @RequestBody req: CreateTaskRequest
    ) =
        boardService.findById(boardId).let { board ->
            val assigned = req.assignedTo?.let { userService.findById(it) }
            taskService.create(board, req.title, req.status, req.description, assigned)
        }.let { ResponseEntity.ok(it) }

    @PatchMapping("/{taskId}")
    fun updateTask(
        @PathVariable taskId: Long,
        @RequestBody req: UpdateTaskRequest
    ) =
        req.run {
            val assigned = assignedTo?.let { userService.findById(it) }
            taskService.updateTask(taskId, title, description, status, assigned)
        }.let { ResponseEntity.ok(it) }

    @GetMapping
    fun getTasks(@PathVariable boardId: Long) =
        boardService.findById(boardId).let { board ->
            taskService.findByBoard(board)
        }.let { ResponseEntity.ok(it) }

    @DeleteMapping("/{taskId}")
    fun deleteTask(@PathVariable taskId: Long): ResponseEntity<Void> =
        taskService.delete(taskId).let { ResponseEntity.noContent().build() }
}
