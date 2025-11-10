package com.digiventure.authserver

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class AuthRequest(val email: String, val password: String)

@Serializable
data class AuthResponse(val ok: Boolean, val message: String? = null)

fun main() {
    // naive in-memory storage for demo
    val users = mutableMapOf<String, String>() // email -> hashed password

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        routing {
            get("/") {
                call.respond(mapOf("service" to "AuthServer", "status" to "ok"))
            }
            post("/register") {
                val req = call.receive<AuthRequest>()
                if (req.email.isBlank() || req.password.length < 4) {
                    call.respond(AuthResponse(false, "Invalid email or password too short"))
                    return@post
                }
                if (users.containsKey(req.email)) {
                    call.respond(AuthResponse(false, "User exists"))
                    return@post
                }
                val hash = BCrypt.hashpw(req.password, BCrypt.gensalt())
                users[req.email] = hash
                call.respond(AuthResponse(true))
            }
            post("/login") {
                val req = call.receive<AuthRequest>()
                val hash = users[req.email]
                if (hash != null && BCrypt.checkpw(req.password, hash)) {
                    call.respond(AuthResponse(true))
                } else {
                    call.respond(AuthResponse(false, "Invalid credentials"))
                }
            }
        }
    }.start(wait = true)
}

