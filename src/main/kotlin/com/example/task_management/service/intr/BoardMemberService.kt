package com.example.task_management.service.intr

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.BoardMember
import com.example.task_management.model.entity.User


interface BoardMemberService {
    fun addMember(board: Board, user: User): BoardMember
    fun getMembers(board: Board): List<BoardMember>
    fun getBoardsForUser(user: User): List<BoardMember>
}