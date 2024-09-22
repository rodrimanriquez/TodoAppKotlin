package com.rmprojects.todoapp.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmprojects.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class TasksViewModel @Inject constructor() : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false

    }

    fun onDialogOpen() {
        _showDialog.value = true
    }

    fun onTaskAdded(task: String) {
        _tasks.add(TaskModel(task = task))
        onDialogClose()
    }

    fun onCheckBoxSelected(taskItem: TaskModel) {
        Log.i("TasksViewModel", "onCheckBoxSelected: $taskItem")

        val index = _tasks.indexOf(taskItem)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }
}