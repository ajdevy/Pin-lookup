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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.tasks.R
import com.example.myapplication.tasks.domain.Task
import com.example.myapplication.tasks.domain.TaskDraft
import java.util.UUID

@Composable
fun TasksScreen(viewModel: TasksViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val newTaskDraft by viewModel.newTaskDraft.collectAsState()

    Box {
        TasksList(
            tasks = tasks,
            newTaskDraft = newTaskDraft,
            onTaskCompletionChanged = { taskId, isCompleted ->
                viewModel.completeTask(taskId, isCompleted)
            },
            onTaskDeleted = { taskId -> viewModel.deleteTask(taskId) },
            onDraftDeleted = { viewModel.deleteDraft() },
            onDraftChanged = { draft -> viewModel.modifyTaskDraft(draft) },
            onDraftCreated = { draft -> viewModel.addTask(draft) }
        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(50.dp),
            onClick = { viewModel.addTaskDraft() }
        ) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksList(
    modifier: Modifier = Modifier,
    newTaskDraft: TaskDraft?,
    tasks: List<Task>,
    onTaskCompletionChanged: (taskId: String, isCompleted: Boolean) -> Unit = { _, _ -> },
    onTaskDeleted: (String) -> Unit = {},
    onDraftDeleted: () -> Unit = {},
    onDraftChanged: (TaskDraft) -> Unit = { _ -> },
    onDraftCreated: (TaskDraft) -> Unit = { _ -> },
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(newTaskDraft) {
        if (newTaskDraft != null) {
            lazyListState.animateScrollToItem(0)
        }
    }
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(16.dp)
    ) {
        if (newTaskDraft != null) {
            item(newTaskDraft.id) {
                TaskDraftItem(
                    modifier = modifier,
                    taskDraft = newTaskDraft,
                    onDraftDeleted = onDraftDeleted,
                    onDraftChanged = onDraftChanged,
                    onDraftCreated = onDraftCreated,
                )
            }
        }
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
fun TaskDraftItem(
    modifier: Modifier = Modifier,
    taskDraft: TaskDraft,
    onDraftDeleted: () -> Unit = {},
    onDraftChanged: (TaskDraft) -> Unit = { _ -> },
    onDraftCreated: (TaskDraft) -> Unit = { _ -> },
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = taskDraft.text,
            onValueChange = { draftText -> onDraftChanged(taskDraft.copy(text = draftText)) },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDraftCreated(taskDraft)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        IconButton(
            onClick = { onDraftDeleted() },
        ) {
            Image(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskCompletionChanged: (isCompleted: Boolean) -> Unit = {},
    onTaskDeleted: (String) -> Unit = {},
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
fun TaskDraftItemPreview() {
    TaskDraftItem(
        taskDraft = TaskDraft(
            id = UUID.randomUUID().toString(),
            text = "Draft Task"
        )
    )
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        task = Task(
            id = UUID.randomUUID().toString(),
            text = "Task 2",
            isCompleted = false
        )
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TasksListPreview() {
    TasksList(
        newTaskDraft = TaskDraft(
            id = UUID.randomUUID().toString(),
            text = "draft"
        ),
        tasks = listOf(
            Task(
                id = UUID.randomUUID().toString(),
                text = "Task 111111111111111111111111111",
                isCompleted = true
            ),
            Task(
                id = UUID.randomUUID().toString(),
                text = "Task 2",
                isCompleted = false
            ),
            Task(
                id = UUID.randomUUID().toString(),
                text = "Task 3",
                isCompleted = true
            ),
        )
    )
}
