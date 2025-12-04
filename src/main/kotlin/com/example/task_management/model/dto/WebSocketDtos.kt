package com.example.task_management.model.dto

data class WsMessage(
    val type: String,
    val boardId: Long,
    val data: Any
)