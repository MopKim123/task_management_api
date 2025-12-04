package com.example.task_management.controller.websockeet

import com.example.task_management.model.`object`.WsEventTypes
import com.example.task_management.websocket.WsEventProcessor
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class TaskWebSocketHandler(
    private val wsEventProcessor: WsEventProcessor
) : TextWebSocketHandler(), BoardEventPublisher {

    private val userSessions = ConcurrentHashMap<Long, MutableList<WebSocketSession>>()
    private val sessions = ConcurrentHashMap<Long, MutableList<WebSocketSession>>()
    private val mapper = jacksonObjectMapper()
    val logger = LoggerFactory.getLogger(TaskWebSocketHandler::class.java)

    override fun afterConnectionEstablished(session: WebSocketSession) {
//        val boardId = session.uri?.query?.split("=")?.getOrNull(1)?.toLongOrNull() ?: return
//        sessions.computeIfAbsent(boardId) { mutableListOf() }.also { it.add(session) }

        val queryParams = session.uri?.query?.split("&")?.associate {
            val (key, value) = it.split("=")
            key to value
        } ?: emptyMap()

        val userId = queryParams["userId"]?.toLongOrNull() ?: return
        userSessions.computeIfAbsent(userId) { mutableListOf() }.also { it.add(session) }
            .also { logger?.warn("user connected") }

        val boardId = queryParams["boardId"]?.toLongOrNull()
        if (boardId != null) {
            sessions.computeIfAbsent(boardId) { mutableListOf() }.also { it.add(session) }
                .also { logger?.warn("board connected") }
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val json = mapper.readTree(message.payload)
            val type = json["type"]?.asText() ?: run {
                session.close(CloseStatus.BAD_DATA)
                return
            }

            val userId = json["userId"]?.asLong()
            val boardId = json["boardId"]?.asLong() ?: 0

            val data = json["data"] ?: run {
                session.close(CloseStatus.BAD_DATA)
                return
            }
            val payload = wsEventProcessor.handleEvent(type, boardId, data)
            if (payload.isNotEmpty()) {
                val parsedPayload = mapper.readTree(payload)
                val broadcastBoardId = jacksonObjectMapper().readTree(payload)["boardId"]?.asLong()
                val broadcastUserId = parsedPayload["data"].get("ownerId")?.asLong() ?: userId
                val newMemberId = parsedPayload["data"]?.get("user")?.get("id")?.asLong()

                if (broadcastBoardId != null) {
                    sendToBoard(broadcastBoardId, payload)
                }

                if ((type == WsEventTypes.BOARD_CREATED || type == WsEventTypes.BOARD_UPDATED || type == WsEventTypes.BOARD_DELETED)
                    && broadcastUserId != null) {
                    sendToUser(broadcastUserId, payload)
                }
                if (type == WsEventTypes.MEMBER_ADDED && newMemberId != null) {
                    sendToUser(newMemberId, payload)
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            session.close(CloseStatus.SERVER_ERROR)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
        sessions.values.forEach { it.remove(session) }
    }

    override fun sendToBoard(boardId: Long, payload: String) {
        sessions[boardId]?.forEach { sess -> if (sess.isOpen) sess.sendMessage(TextMessage(payload)) }
    }
    fun sendToUser(userId: Long, payload: String) {
        userSessions[userId]?.forEach { sess ->
            if (sess.isOpen) sess.sendMessage(TextMessage(payload))
        }
    }
}
