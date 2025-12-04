package com.example.task_management.repository

import com.example.task_management.model.entity.Comment
import com.example.task_management.model.entity.Task
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByTask(task: Task): List<Comment>
}