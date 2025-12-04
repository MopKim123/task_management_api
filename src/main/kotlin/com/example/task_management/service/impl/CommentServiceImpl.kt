package com.example.task_management.service.impl

import com.example.task_management.model.entity.Comment
import com.example.task_management.model.entity.Task
import com.example.task_management.model.entity.User
import com.example.task_management.repository.CommentRepository
import com.example.task_management.service.intr.CommentService
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl
    (
    private val commentRepository: CommentRepository,
): CommentService {

    override fun addComment(task: Task, author: User, message: String): Comment =
        Comment(task = task, author = author, message = message)
            .let { commentRepository.save(it) }

    override fun getComments(task: Task): List<Comment> =
        commentRepository.findByTask(task)
}
