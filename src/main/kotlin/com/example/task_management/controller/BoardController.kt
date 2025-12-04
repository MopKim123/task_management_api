package com.example.task_management.controller

import com.example.task_management.model.dto.BoardResponse
import com.example.task_management.model.dto.CreateBoardRequest
import com.example.task_management.model.dto.InviteMemberRequest
import com.example.task_management.model.dto.LoginRequest
import com.example.task_management.model.dto.SimpleUserResponse
import com.example.task_management.model.dto.UpdateBoardRequest
import com.example.task_management.model.entity.Board
import com.example.task_management.service.intr.BoardMemberService
import com.example.task_management.service.intr.BoardService
import com.example.task_management.service.intr.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boards")
class BoardController(
    private val boardService: BoardService,
    private val userService: UserService,
    private val boardMemberService: BoardMemberService
) {

    @PostMapping
    fun createBoard(
        @RequestBody req: CreateBoardRequest,
        @RequestAttribute userId: Long
    ): ResponseEntity<Any> =
        userService.findById(userId).let { user ->
            boardService.create(req.name, user)
        }.let { ResponseEntity.ok(it) }

    @PatchMapping("/{boardId}")
    fun updateBoardName(
        @PathVariable boardId: Long,
        @RequestBody req: UpdateBoardRequest
    ): ResponseEntity<Any> =
        boardService.updateName(boardId, req.name).let { ResponseEntity.ok(it) }

    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable boardId: Long): ResponseEntity<Void> =
        boardService.delete(boardId).let { ResponseEntity.noContent().build() }

    @PostMapping("/{boardId}/invite")
    fun inviteMember(
        @PathVariable boardId: Long,
        @RequestBody req: InviteMemberRequest
    ): ResponseEntity<Any> =
        boardService.findById(boardId).let { board ->
            userService.findById(req.userId).let { user ->
                boardMemberService.addMember(board, user)
            }
        }.let { ResponseEntity.ok(it) }

    @GetMapping("/{boardId}/members")
    fun listBoardMembers(@PathVariable boardId: Long) =
        boardService.findById(boardId).let { board ->
            boardMemberService.getMembers(board)
        }.let { ResponseEntity.ok(it) }


    @GetMapping("/owner/{ownerId}")
    fun getBoardsByOwner(@PathVariable ownerId: Long): List<BoardResponse> =
        boardService.getAllBoardsByOwner(ownerId)

    @GetMapping("/user/{userId}")
    fun getBoardsByUser(@PathVariable userId: Long): List<BoardResponse> =
        boardService.getAllBoardsByUser(userId)
}
