package com.rmprojects.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rmprojects.todoapp.addtasks.ui.model.TaskModel

@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {

    val showDialog: Boolean by tasksViewModel.showDialog.observeAsState(false)

    Scaffold(floatingActionButton = {
        FabAddTasks { tasksViewModel.onDialogOpen() }
    }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            DialogAddTask(show = showDialog,
                onDismiss = { tasksViewModel.onDialogClose() },
                onTaskAdded = { tasksViewModel.onTaskAdded(it) })
            TaskListView(tasksViewModel)
        }
    }
}

@Composable
fun TaskListView(tasksViewModel: TasksViewModel) {
    val myTasks: List<TaskModel> = tasksViewModel.tasks
    if (myTasks.isEmpty()) {
        EmptyTasks()

    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {
            if (myTasks.isNotEmpty()) {
                items(myTasks, key = { it.id }) { task ->
                    TaskItem(taskItem = task, tasksViewModel)
                }
            } else {
                item {
                    EmptyTasks()
                }
            }

        }
    }

}

@Composable
fun EmptyTasks() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text("No tasks", fontSize = 32.sp)
    }
}

@Composable
fun TaskItem(taskItem: TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = taskItem.task,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                fontSize = 24.sp
            )
            Checkbox(checked = taskItem.selected,
                onCheckedChange = { tasksViewModel.onCheckBoxSelected(taskItem) })
        }
    }

}

@Composable
fun FabAddTasks(openTasksDialog: () -> Unit) {
    FloatingActionButton(onClick = {
        openTasksDialog()
    }) {
        Icon(Icons.Rounded.Add, contentDescription = "Add")
    }
}

@Composable
fun DialogAddTask(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {

    var myTask by rememberSaveable { mutableStateOf("") }

    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(text = "Add Task", fontSize = 24.sp)
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = { myTask = it })
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    enabled = myTask.isNotEmpty(),
                    onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Add Task")
                }
            }
        }

    }
}