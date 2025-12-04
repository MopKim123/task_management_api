package com.example.task_management.service.intr

import com.example.task_management.model.dto.BoardResponse
import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.User


interface BoardService {
    fun create(name: String, owner: User): Board
    fun updateName(boardId: Long, newName: String): Board
    fun delete(boardId: Long)
    fun findById(id: Long): Board
    fun findOwnedBy(owner: User): List<Board>
    fun getAllBoardsByOwner(ownerId: Long): List<BoardResponse>
    fun getAllBoardsByUser(userId: Long): List<BoardResponse>
}