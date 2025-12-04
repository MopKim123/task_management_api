package com.example.task_management.model.dto

import java.time.LocalDateTime

data class CreateBoardRequest(
    val name: String
)

data class UpdateBoardRequest(
    val name: String
)

data class InviteMemberRequest(
    val userId: Long
)

data class BoardResponse(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val ownerId: Long,
    val ownerName: String

)