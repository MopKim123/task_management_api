package com.example.task_management.repository

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.Task
import com.example.task_management.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByBoard(board: Board): List<Task>
    fun findByAssignedTo(user: User): List<Task>
}