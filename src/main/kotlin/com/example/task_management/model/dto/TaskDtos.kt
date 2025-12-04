package com.example.task_management.model.dto

data class CreateTaskRequest(
    val title: String,
    val status: String,
    val description: String?,
    val assignedTo: Long?
)

data class UpdateTaskRequest(
    val title: String,
    val description: String?,
    val status: String,
    val assignedTo: Long?
)