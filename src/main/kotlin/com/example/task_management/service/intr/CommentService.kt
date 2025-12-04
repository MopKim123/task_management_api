package com.example.task_management.service.intr

import com.example.task_management.model.entity.Comment
import com.example.task_management.model.entity.Task
import com.example.task_management.model.entity.User

interface CommentService {
    fun addComment(task: Task, author: User, message: String): Comment
    fun getComments(task: Task): List<Comment>
}