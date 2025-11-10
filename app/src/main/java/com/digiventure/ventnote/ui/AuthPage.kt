package com.digiventure.ventnote.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun AuthPage(onSuccess: () -> Unit, serverBase: String = "http://10.0.2.2:8080") {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val client = remember { OkHttpClient() }
    val tag = "AuthPage"

    fun call(endpoint: String) {
        if (email.isBlank() || password.length < 4) {
            message = "Enter email and password (>=4 chars)";
            return
        }
        scope.launch {
            loading = true
            val result = authRequest(client, "$serverBase/$endpoint", email, password)
            loading = false
            message = result.second
            Log.d(tag, "Endpoint=$endpoint result=${result.second}")
            if (result.first) {
                onSuccess()
            }
        }
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Login / Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = { call("register") }, enabled = !loading) { Text("Register") }
            Spacer(Modifier.width(12.dp))
            Button(onClick = { call("login") }, enabled = !loading) { Text("Login") }
        }
        if (loading) {
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        message?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = if (it.contains("fail", true) || it.contains("Invalid", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }
    }
}

private suspend fun authRequest(client: OkHttpClient, url: String, email: String, password: String): Pair<Boolean, String> {
    return withContext(Dispatchers.IO) {
        try {
            val json = """{"email":"${email}","password":"${password}"}"""
            val media = "application/json; charset=utf-8".toMediaType()
            val body = json.toRequestBody(media)
            val req = Request.Builder().url(url).post(body).build()
            client.newCall(req).execute().use { resp ->
                val code = resp.code
                val bodyStr = resp.body?.string() ?: ""
                if (code >= 500) return@withContext false to "Server error ($code)"
                val ok = bodyStr.contains("\"ok\":true")
                val message = when {
                    ok && url.endsWith("register") -> "Registered successfully"
                    ok && url.endsWith("login") -> "Logged in successfully"
                    bodyStr.contains("User exists") -> "User already exists"
                    bodyStr.contains("Invalid credentials") -> "Invalid credentials"
                    else -> bodyStr.ifBlank { "Request failed (code=$code)" }
                }
                ok to message
            }
        } catch (e: Exception) {
            false to ("Error: ${e.message}")
        }
    }
}
