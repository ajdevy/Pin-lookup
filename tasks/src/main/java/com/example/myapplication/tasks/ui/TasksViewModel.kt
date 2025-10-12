package com.example.myapplication.tasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.tasks.domain.Task
import com.example.myapplication.tasks.domain.TaskDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Random
import java.util.UUID

class TasksViewModel : ViewModel() {

    private val taskDraft: MutableStateFlow<TaskDraft?> = MutableStateFlow(null)

    private val _tasks: MutableStateFlow<List<Task>> = MutableStateFlow(
        List(100) { i -> Task(UUID.randomUUID().toString(), "Task ${i + 1}", Random().nextBoolean()) })
    val tasks: StateFlow<List<Task>> = _tasks
    val newTaskDraft: StateFlow<TaskDraft?> = taskDraft

    fun completeTask(taskId: String, isCompleted: Boolean) {
        _tasks.update { tasks ->
            tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(isCompleted = isCompleted)
                } else {
                    task
                }
            }
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.update { tasks ->
            tasks.filter { task ->
                task.id != taskId
            }
        }
    }

    fun deleteDraft() {
        viewModelScope.launch {
            taskDraft.emit(null)
        }
    }

    fun addTaskDraft() {
        viewModelScope.launch {
            taskDraft.emit(
                TaskDraft(
                    id = UUID.randomUUID().toString(),
                    text = ""
                )
            )
        }
    }

    fun modifyTaskDraft(draft: TaskDraft) {
        viewModelScope.launch {
            taskDraft.emit(draft)
        }
    }

    fun addTask(newTaskDraft: TaskDraft) {
        val newTask = Task(
            id = newTaskDraft.id,
            text = newTaskDraft.text,
            isCompleted = false
        )
        _tasks.update { tasks ->
            listOf(newTask) + tasks
        }
        deleteDraft()
    }
}
