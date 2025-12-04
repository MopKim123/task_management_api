package com.example.task_management.controller.websockeet

interface BoardEventPublisher {
    fun sendToBoard(boardId: Long, payload: String)
}