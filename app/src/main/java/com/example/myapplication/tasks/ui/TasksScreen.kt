package com.example.myapplication.tasks.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.tasks.domain.Task

@Composable
fun TasksScreen(viewModel: TasksViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    Box {
        TasksList(
            tasks = tasks,
            onTaskCompletionChanged = { taskId, isCompleted ->
                viewModel.completeTask(taskId, isCompleted)
            },
            onTaskDeleted = { taskId -> viewModel.deleteTask(taskId) }
        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(50.dp),
            onClick = {
                TODO("show add task UI")
            },
        ) {
            Image (
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksList(
    tasks: List<Task>,
    onTaskCompletionChanged: (taskId: Int, isCompleted: Boolean) -> Unit = { _, _ -> },
    onTaskDeleted: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(6.dp)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            TaskItem(
                modifier = Modifier.animateItem(),
                task = task,
                onTaskCompletionChanged = { isCompleted -> onTaskCompletionChanged(task.id, isCompleted) },
                onTaskDeleted = onTaskDeleted
            )
        }
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskCompletionChanged: (isCompleted: Boolean) -> Unit = {},
    onTaskDeleted: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = task.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge
        )

        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onTaskCompletionChanged(it) }
        )

        IconButton(
            onClick = { onTaskDeleted(task.id) },
        ) {
            Image(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(task = Task(2, "Task 2", false))
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TasksListPreview() {
    TasksList(
        listOf(
            Task(1, "Task 111111111111111111111111111", true),
            Task(2, "Task 2", false),
            Task(3, "Task 3", true),
        )
    )
}