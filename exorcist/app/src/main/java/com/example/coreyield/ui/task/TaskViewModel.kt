package com.example.coreyield.ui.tasks

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreyield.data.model.Task
import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.repository.Repository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadTasks()
    }

    fun loadTasks() {
        val uid = Repository.currentUserId ?: return
        _isLoading.value = true

        FirebaseManager.db.collection("users").document(uid)
            .collection("tasks")
            .orderBy("deadlineTimestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    Log.e("TaskViewModel", "Error loading tasks", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.toObjects(Task::class.java)
                    val listWithIds = list.mapIndexed { index, task ->
                        task.id = snapshot.documents[index].id

                        // --- AUTO-MIGRASI DI UI/VIEWMODEL ---
                        if (task.title.isBlank() && task.description.isNotBlank()) {
                            task.title = task.description.trim()
                        }
                        task
                    }
                    _tasks.value = listWithIds
                }
            }
    }

    fun addTask(title: String, category: String, difficulty: Int, deadline: Long?, inputDate: Long) {
        val uid = Repository.currentUserId ?: run {
            Log.e("TaskViewModel", "addTask failed: user not logged in")
            return
        }

        val newTask = Task(
            id = "",
            title = title,
            category = category,
            difficultyScore = difficulty,
            deadlineTimestamp = deadline, // bisa null
            isCompleted = false,
            timestamp = System.currentTimeMillis(), // waktu sistem
            inputTimestamp = inputDate // waktu input menurut user
        )

        FirebaseManager.db.collection("users").document(uid)
            .collection("tasks")
            .add(newTask)
            .addOnSuccessListener { documentReference ->
                Log.d("TaskViewModel", "Task added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("TaskViewModel", "Failed to add task", exception)
            }
    }

    fun updateTask(task: Task, newTitle: String, cat: String, diff: Int, deadline: Long?) {
        val uid = Repository.currentUserId ?: return

        val updates = mapOf(
            "title" to newTitle,
            "category" to cat,
            "difficultyScore" to diff,
            "deadlineTimestamp" to deadline
        )

        FirebaseManager.db.collection("users").document(uid)
            .collection("tasks")
            .document(task.id)
            .update(updates)
            .addOnFailureListener { e ->
                Log.e("TaskViewModel", "Failed to update task ${task.id}", e)
            }
    }

    fun toggleTaskCompletion(task: Task) {
        val uid = Repository.currentUserId ?: return
        val newStatus = !task.isCompleted

        FirebaseManager.db.collection("users").document(uid)
            .collection("tasks")
            .document(task.id)
            .update("isCompleted", newStatus)
            .addOnFailureListener { e ->
                Log.e("TaskViewModel", "Failed to toggle task ${task.id}", e)
            }
    }

    fun deleteTask(taskId: String) {
        val uid = Repository.currentUserId ?: return

        FirebaseManager.db.collection("users").document(uid)
            .collection("tasks")
            .document(taskId)
            .delete()
            .addOnFailureListener { e ->
                Log.e("TaskViewModel", "Failed to delete task $taskId", e)
            }
    }
}