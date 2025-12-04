package com.example.task_management.service.impl

import com.example.task_management.model.entity.User
import com.example.task_management.repository.UserRepository
import com.example.task_management.service.intr.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    override fun register(username: String, password: String): User {
        userRepository.findByUsername(username)
            ?.let { throw IllegalArgumentException("Username already exists") }

        return User(
            username = username,
            password = password
        ).let { userRepository.save(it) }
    }

    override fun findById(id: Long): User =
        userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found") }

    override fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    override fun findByUsernameContaining(username: String): List<User> =
        userRepository.findByUsernameContainingIgnoreCase(username)
}
