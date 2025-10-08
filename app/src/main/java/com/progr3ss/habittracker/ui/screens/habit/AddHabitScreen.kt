package com.progr3ss.habittracker.ui.screens.habit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.progr3ss.habittracker.ui.viewmodel.HabitViewModel
import com.progr3ss.habittracker.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    var habitName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    val createState by viewModel.createHabitState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            onNavigateBack()
            viewModel.resetCreateHabitState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Habit") },
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
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Habit Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = goal,
                onValueChange = { goal = it },
                label = { Text("Goal") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.createHabit(habitName, category, goal)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = habitName.isNotEmpty() && category.isNotEmpty() && 
                         goal.isNotEmpty() && createState !is Resource.Loading
            ) {
                if (createState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Habit")
                }
            }

            if (createState is Resource.Error) {
                Text(
                    text = (createState as Resource.Error).message ?: "Failed to create habit",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
