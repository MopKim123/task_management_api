package com.example.task_management.service.intr

import com.example.task_management.model.entity.User


interface UserService {
    fun register(username: String, password: String): User
    fun findById(id: Long): User
    fun findByUsername(username: String): User?
    fun findByUsernameContaining(username: String): List<User>
}