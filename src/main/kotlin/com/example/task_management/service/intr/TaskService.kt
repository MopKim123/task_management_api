package com.example.task_management.service.intr

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.Task
import com.example.task_management.model.entity.User

interface TaskService {
    fun create(board: Board, title: String, status: String, description: String?, assignedTo: User?): Task
    fun updateTask(taskId: Long, title: String, description: String?, status: String, assignedTo: User?): Task
    fun findByBoard(board: Board): List<Task>
    fun findById(id: Long): Task
    fun delete(id: Long)
}