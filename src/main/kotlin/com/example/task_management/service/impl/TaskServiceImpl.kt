package com.example.task_management.service.impl

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.Task
import com.example.task_management.model.entity.User
import com.example.task_management.repository.TaskRepository
import com.example.task_management.service.intr.TaskService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
): TaskService {
    override fun create(
        board: Board,
        title: String,
        status: String,
        description: String?,
        assignedTo: User?
    ): Task =
        Task(
            board = board,
            title = title,
            status = status,
            description = description,
            assignedTo = assignedTo
        ).let { taskRepository.save(it) }

    override fun updateTask(
        taskId: Long,
        title: String,
        description: String?,
        status: String,
        assignedTo: User?
    ): Task =
        findById(taskId).apply {
            this.title = title
            this.description = description
            this.status = status
            this.assignedTo = assignedTo
            this.updatedAt = LocalDateTime.now()
        }.let { taskRepository.save(it) }

    override fun findByBoard(board: Board): List<Task> =
        taskRepository.findByBoard(board)

    override fun findById(id: Long): Task =
        taskRepository.findById(id)
            .orElseThrow { NoSuchElementException("Task not found") }

    override fun delete(id: Long) {
        findById(id).also { taskRepository.deleteById(id) }
    }
}
