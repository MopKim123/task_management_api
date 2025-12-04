package com.example.task_management.controller

import com.example.task_management.model.dto.CreateCommentRequest
import com.example.task_management.service.intr.CommentService
import com.example.task_management.service.intr.TaskService
import com.example.task_management.service.intr.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks/{taskId}/comments")
class CommentController(
    private val commentService: CommentService,
    private val taskService: TaskService,
    private val userService: UserService
) {
    @PostMapping
    fun addComment(
        @PathVariable taskId: Long,
        @RequestBody req: CreateCommentRequest,
        @RequestAttribute userId: Long
    ): ResponseEntity<Any> =
        userService.findById(userId).let { user ->
            taskService.findById(taskId).let { task ->
                commentService.addComment(task, user, req.message)
            }
        }.let { ResponseEntity.ok(it) }

    @GetMapping
    fun getComments(@PathVariable taskId: Long): ResponseEntity<Any> =
        taskService.findById(taskId).let { task ->
            commentService.getComments(task)
        }.let { ResponseEntity.ok(it) }
}
