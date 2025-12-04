package com.example.task_management.repository

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.BoardMember
import com.example.task_management.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface BoardMemberRepository : JpaRepository<BoardMember, Long> {
    fun findByBoard(board: Board): List<BoardMember>
    fun findByUser(user: User): List<BoardMember>
    fun findByBoardAndUser(board: Board, user: User): BoardMember?
}