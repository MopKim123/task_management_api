package com.example.task_management.controller

import com.example.task_management.model.dto.LoginRequest
import com.example.task_management.model.dto.RegisterRequest
import com.example.task_management.model.dto.SimpleUserResponse
import com.example.task_management.model.entity.User
import com.example.task_management.service.intr.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<SimpleUserResponse> =
        userService.register(req.username, req.password)
            .let { user -> SimpleUserResponse(user.id!!, user.username) }
            .let { ResponseEntity.ok(it) }


    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<SimpleUserResponse> =
        userService.findByUsername(req.username)
            ?.takeIf { it.password == req.password }
            ?.let { user -> SimpleUserResponse(user.id!!, user.username) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.badRequest().build()

    @GetMapping("/search")
    fun search(@RequestParam username: String): ResponseEntity<List<SimpleUserResponse>> =
        userService.findByUsernameContaining(username)
            .map { user -> SimpleUserResponse(user.id!!, user.username) }
            .let { ResponseEntity.ok(it) }
}