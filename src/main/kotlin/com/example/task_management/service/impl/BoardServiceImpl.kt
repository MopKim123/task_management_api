package com.example.task_management.service.impl

import com.example.task_management.model.boardMapper.toResponse
import com.example.task_management.model.dto.BoardResponse
import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.User
import com.example.task_management.repository.BoardRepository
import com.example.task_management.service.intr.BoardService
import org.springframework.stereotype.Service

@Service
class BoardServiceImpl(
    private val boardRepository: BoardRepository,
): BoardService {
    override fun create(name: String, owner: User): Board =
        Board(name = name, owner = owner)
            .let { boardRepository.save(it) }

    override fun updateName(boardId: Long, newName: String): Board =
        findById(boardId).apply { name = newName }
            .let { boardRepository.save(it) }

    override fun delete(boardId: Long) {
        findById(boardId)
            .also { boardRepository.deleteById(boardId) }
    }

    override fun findById(id: Long): Board =
        boardRepository.findById(id).orElseThrow { NoSuchElementException("Board not found") }

    override fun findOwnedBy(owner: User): List<Board> =
        boardRepository.findByOwner(owner)

    override fun getAllBoardsByOwner(ownerId: Long): List<BoardResponse> =
        boardRepository.findAllByOwnerId(ownerId)
            .map { it.toResponse() }

    override fun getAllBoardsByUser(userId: Long): List<BoardResponse> =
        boardRepository.findAllByUserId(userId)
            .map { it.toResponse() }
}
