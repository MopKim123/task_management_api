package com.example.task_management.websocket

import com.example.task_management.controller.websockeet.BoardEventPublisher
import com.example.task_management.controller.websockeet.TaskWebSocketHandler
import com.example.task_management.model.`object`.WsEventTypes
import com.example.task_management.service.intr.*
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WsEventProcessor(
    private val boardService: BoardService,
    private val boardMemberService: BoardMemberService,
    private val taskService: TaskService,
    private val commentService: CommentService,
    private val userService: UserService
) {
    val logger = LoggerFactory.getLogger(WsEventProcessor::class.java)

    fun handleEvent(type: String, boardId: Long, data: JsonNode): String {
        logger?.info("Type: $type", )
        logger?.info("data: $data", )
        return when (type) {

            WsEventTypes.BOARD_CREATED -> {
                val name = data["name"].asText()
                val ownerId = data["ownerId"].asLong()
                val owner = userService.findById(ownerId)
                val board = boardService.create(name, owner)
                boardMemberService.addMember(board,owner)
                """
                {
                    "type":"${WsEventTypes.BOARD_CREATED}",
                    "boardId":${board.id},
                    "data":{
                        "id":${board.id},
                        "name":"${board.name}",
                        "createdAt":"${board.createdAt}",
                        "ownerId":"${owner.id}",
                        "ownerName":"${owner.username}"
                    }
                }
                """.trimIndent()
            }
            WsEventTypes.BOARD_UPDATED -> {
                val boardIdToUpdate = data["id"].asLong()
                val name = data["name"].asText()
                val board = boardService.updateName(boardIdToUpdate, name)
                """{"type":"${WsEventTypes.BOARD_UPDATED}","boardId":${board.id},"data":{"id":${board.id},"name":"${board.name}"}}"""
            }

            WsEventTypes.BOARD_DELETED -> {
                val boardIdToDelete = data["id"].asLong()
                boardService.delete(boardIdToDelete)
                """{"type":"${WsEventTypes.BOARD_DELETED}","boardId":$boardIdToDelete}"""
            }

            WsEventTypes.MEMBER_ADDED -> {
                val userId = data["userId"].asLong()
                val user = userService.findById(userId)
                val board = boardService.findById(boardId)
                val member = boardMemberService.addMember(board, user)
                """
                    {
                        "type": "${WsEventTypes.MEMBER_ADDED}",
                        "boardId": ${board.id},
                        "data": {
                            "id": ${member.id},
                            "board": {
                                "id": ${board.id},
                                "name": "${board.name}",
                                "createdAt": "${board.createdAt}",
                                "ownerId": ${board.owner?.id}
                            },
                            "user": {
                                "id": ${user.id},
                                "username": "${user.username}"
                            }
                        }
                    }
                """
            }

            WsEventTypes.TASK_CREATED -> {
                val title = data["title"].asText()
                val description = data["description"]?.asText()
                val status = data["status"].asText()
                val assignedToId = data["assignedTo"]?.asLong()
                val assignedTo = assignedToId?.let { userService.findById(it) }
                val board = boardService.findById(boardId)
                val task = taskService.create(board, title, status, description, assignedTo)
                """{
                    "type": "${WsEventTypes.TASK_CREATED}",
                    "boardId": ${board.id},
                    "data": {
                        "id": ${task.id},
                        "title": "${task.title}",
                        "status": "${task.status}",
                        "description": "${task.description ?: ""}",
                        "assignedTo": ${task.assignedTo?.let { """{"id":${it.id},"username":"${it.username}"}""" } ?: "null"}
                    }
                }"""

            }


            WsEventTypes.TASK_UPDATED -> {
                val taskId = data["id"].asLong()
                val title = data["title"].asText()
                val description = data["description"]?.asText()
                val status = data["status"].asText()
                val board = boardService.findById(boardId)
                val assignedToId = data["assignedTo"]?.asLong()
                val assignedTo = assignedToId?.let { userService.findById(it) }
                val task = taskService.updateTask(taskId, title, description, status, assignedTo)
                """{
                    "type": "${WsEventTypes.TASK_UPDATED}",
                    "boardId": ${board.id},
                    "data": {
                        "id": ${task.id},
                        "title": "${task.title}",
                        "status": "${task.status}",
                        "description": "${task.description ?: ""}",
                        "assignedTo": ${task.assignedTo?.let { """{"id":${it.id},"username":"${it.username}"}""" } ?: "null"}
                    }
                }"""
            }

            WsEventTypes.TASK_DELETED -> {
                val taskId = data["id"].asLong()
                val task = taskService.findById(taskId)
                taskService.delete(taskId)
                """{"type":"${WsEventTypes.TASK_DELETED}","boardId":${task.board!!.id},"data":{"id":${task.id}}}"""
            }

            WsEventTypes.COMMENT_ADDED -> {
                val taskId = data["taskId"].asLong()
                val messageText = data["message"].asText()
                val authorId = data["authorId"].asLong()
                val task = taskService.findById(taskId)
                val author = userService.findById(authorId)
                val comment = commentService.addComment(task, author, messageText)
                """
                {
                    "type":"${WsEventTypes.COMMENT_ADDED}",
                    "boardId":${task.board!!.id},
                    "data":{
                        "id":${comment.id},
                        "message":"${comment.message}",
                        "createdAt":"${comment.createdAt}",
                        "task":{"id":${task.id}},
                        "author":{
                            "id":${author.id},
                            "username":"${author.username}"
                        }
                    }
                }
                """
            }

            else -> ""  // ignore unknown types
        }
    }
}