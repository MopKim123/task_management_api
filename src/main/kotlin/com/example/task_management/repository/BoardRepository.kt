package com.example.task_management.repository

import com.example.task_management.model.entity.Board
import com.example.task_management.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BoardRepository : JpaRepository<Board, Long> {
    fun findByOwner(owner: User): List<Board>
    fun findAllByOwnerId(ownerId: Long): List<Board>
    @Query("""
        SELECT b FROM Board b 
        LEFT JOIN b.members m 
        WHERE b.owner.id = :userId OR m.id = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: Long): List<Board>
}