package com.example.task_management.model

import com.example.task_management.model.dto.BoardResponse
import com.example.task_management.model.entity.Board

object boardMapper {

    fun Board.toResponse() =
        BoardResponse(
            id = this.id!!,
            name = this.name,
            createdAt =  this.createdAt,
            ownerId = this.owner?.id!!,
            ownerName = this.owner?.username!!
        )
}