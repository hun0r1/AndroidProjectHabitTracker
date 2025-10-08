package com.progr3ss.habittracker.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.progr3ss.habittracker.domain.model.Schedule
import com.progr3ss.habittracker.domain.model.ScheduleStatus
import com.progr3ss.habittracker.ui.viewmodel.HomeViewModel
import com.progr3ss.habittracker.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreateSchedule: () -> Unit,
    onNavigateToScheduleDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAI: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val schedules by viewModel.schedules.collectAsState()
    val fetchState by viewModel.fetchState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Schedule") },
                actions = {
                    IconButton(onClick = onNavigateToAI) {
                        Icon(Icons.Default.SmartToy, contentDescription = "AI Assistant")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateSchedule) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (fetchState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = (fetchState as Resource.Error).message ?: "Error loading schedules",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    if (schedules.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No schedules for today",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(schedules) { schedule ->
                                ScheduleCard(
                                    schedule = schedule,
                                    onClick = { onNavigateToScheduleDetails(schedule.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(
    schedule: Schedule,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = schedule.habitName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${schedule.startTime} - ${schedule.endTime}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ",
                    style = MaterialTheme.typography.bodySmall
                )
                StatusChip(status = schedule.status)
            }
        }
    }
}

@Composable
fun StatusChip(status: ScheduleStatus) {
    val (text, color) = when (status) {
        ScheduleStatus.PENDING -> "Pending" to MaterialTheme.colorScheme.secondary
        ScheduleStatus.IN_PROGRESS -> "In Progress" to MaterialTheme.colorScheme.primary
        ScheduleStatus.COMPLETED -> "Completed" to MaterialTheme.colorScheme.tertiary
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
