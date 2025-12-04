package com.example.task_management.repository

import com.example.task_management.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByUsernameContainingIgnoreCase(username: String): List<User>
}