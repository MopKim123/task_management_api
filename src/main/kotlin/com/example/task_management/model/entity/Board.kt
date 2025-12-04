package com.example.task_management.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "boards")
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    @Column(name = "created_at")
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    var owner: User? = null,


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "board_members",
        joinColumns = [JoinColumn(name = "board_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var members: MutableList<User> = mutableListOf()
)