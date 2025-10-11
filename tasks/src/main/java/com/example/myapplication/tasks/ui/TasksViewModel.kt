package com.example.myapplication.tasks.ui

import androidx.lifecycle.ViewModel
import com.example.myapplication.tasks.domain.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Random

class TasksViewModel : ViewModel() {

    private val _tasks: MutableStateFlow<List<Task>> = MutableStateFlow(
        List(100) { i -> Task(i, "Task ${i + 1}", Random().nextBoolean()) }
    )
    val tasks: StateFlow<List<Task>> = _tasks

    fun completeTask(taskId: Int, isCompleted: Boolean) {
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

    fun deleteTask(taskId: Int) {
        _tasks.update { tasks ->
            tasks.filter { task ->
                task.id != taskId
            }
        }
    }
}
