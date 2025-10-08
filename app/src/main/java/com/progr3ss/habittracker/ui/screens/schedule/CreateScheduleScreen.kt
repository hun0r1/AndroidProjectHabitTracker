package com.progr3ss.habittracker.ui.screens.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.progr3ss.habittracker.ui.viewmodel.ScheduleViewModel
import com.progr3ss.habittracker.util.DateUtils
import com.progr3ss.habittracker.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddHabit: () -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    var habitId by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:00") }
    var repeatPattern by remember { mutableStateOf("DAILY") }

    val createState by viewModel.createScheduleState.collectAsState()

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            onNavigateBack()
            viewModel.resetCreateScheduleState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Schedule") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = habitId,
                onValueChange = { habitId = it },
                label = { Text("Habit ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = startTime,
                onValueChange = { startTime = it },
                label = { Text("Start Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endTime,
                onValueChange = { endTime = it },
                label = { Text("End Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Repeat Pattern",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("DAILY", "WEEKLY", "MONTHLY").forEach { pattern ->
                    FilterChip(
                        selected = repeatPattern == pattern,
                        onClick = { repeatPattern = pattern },
                        label = { Text(pattern) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.createSchedule(
                        habitId = habitId,
                        startTime = startTime,
                        endTime = endTime,
                        repeatPattern = repeatPattern,
                        date = DateUtils.getCurrentDate()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = habitId.isNotEmpty() && createState !is Resource.Loading
            ) {
                if (createState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Schedule")
                }
            }

            if (createState is Resource.Error) {
                Text(
                    text = (createState as Resource.Error).message ?: "Failed to create schedule",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
