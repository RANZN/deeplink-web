package com.ranjan.deeplink_web

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent

@Composable
fun App() {
    var path by remember { mutableStateOf(window.location.pathname) }

    // Listen to history navigation (back/forward or pushState)
    LaunchedEffect(Unit) {
        window.addEventListener("popstate", {
            path = window.location.pathname
        })
    }

    MaterialTheme {
        when {
            path.startsWith("/interview/") -> {
                val interviewId = path.removePrefix("/interview/")
                InterviewPage(interviewId)
            }
            else -> HomePage(
                onNavigate = { interviewId ->
                    // Push new URL without reload
                    window.history.pushState(null, "", "/interview/$interviewId")
                    // Trigger composition
                    window.dispatchEvent(PopStateEvent("popstate"))
                }
            )
        }
    }
}

@Composable
fun InterviewPage(interviewId: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Interview Page")
        Text("ID: $interviewId")
        Text("If your Android app is installed, it would auto-open via App Link.")
    }
}

@Composable
fun HomePage(onNavigate: (String) -> Unit) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Page")
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Interview ID") }
        )
        Button(
            onClick = {
                if (input.isNotBlank()) {
                    onNavigate(input)
                }
            }
        ) {
            Text("Go to Interview")
        }
    }
}