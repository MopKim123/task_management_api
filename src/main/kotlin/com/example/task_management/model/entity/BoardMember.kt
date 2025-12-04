package com.example.task_management.model.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "board_members",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["board_id", "user_id"])
    ]
)
class BoardMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    var board: Board? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
)
