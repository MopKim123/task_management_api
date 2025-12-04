package com.example.task_management.service.impl

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.BoardMember
import com.example.task_management.model.entity.User
import com.example.task_management.repository.BoardMemberRepository
import com.example.task_management.service.intr.BoardMemberService
import org.springframework.stereotype.Service

@Service
class BoardMemberServiceImpl(
    private val boardMemberRepository: BoardMemberRepository,
): BoardMemberService {
    override fun addMember(board: Board, user: User): BoardMember {
        boardMemberRepository.findByBoardAndUser(board, user)?.let { throw IllegalArgumentException("Already a member") }

        return BoardMember(board = board, user = user)
            .let { boardMemberRepository.save(it) }
    }

    override fun getMembers(board: Board): List<BoardMember> =
        boardMemberRepository.findByBoard(board)

    override fun getBoardsForUser(user: User): List<BoardMember> =
        boardMemberRepository.findByUser(user)
}
