package com.example.task_management.model.dto

data class SimpleUserResponse(
    val id: Long,
    val username: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)