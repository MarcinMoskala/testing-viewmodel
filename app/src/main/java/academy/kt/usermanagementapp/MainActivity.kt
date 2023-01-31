package academy.kt.usermanagementapp

import academy.kt.usermanagementapp.data.network.ApiException
import academy.kt.usermanagementapp.domain.UserListViewModel
import academy.kt.usermanagementapp.model.AddUser
import academy.kt.usermanagementapp.model.User
import academy.kt.usermanagementapp.ui.theme.UserManagementAppTheme
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userListViewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserManagementAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val usersList by userListViewModel.usersList.collectAsState()
                    val showLoading by userListViewModel.showLoading.collectAsState()
                    val error by userListViewModel.error.collectAsState()

                    ErrorDialog(error, userListViewModel::hideError)

                    UserList(
                        usersList,
                        removeUser = userListViewModel::removeUser,
                        refresh = userListViewModel::refresh,
                        addUser = userListViewModel::addUser
                    )

                    if (showLoading) {
                        ProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserList(users: List<User>, removeUser: (Int) -> Unit, refresh: () -> Unit, addUser: (AddUser) -> Unit) {
    val (activeRemoveUser, setActiveRemoveUser) = remember { mutableStateOf<User?>(null) }
    val (activeAddUser, setActiveAddUser) = remember { mutableStateOf(false) }

    RemoveUserDialog(activeRemoveUser, setActiveRemoveUser, removeUser)
    AddUserDialog(activeAddUser, setActiveAddUser, addUser)

    Scaffold(topBar = { },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setActiveAddUser(true) }
            ) {
                Icon(Icons.Rounded.Add, "Add user")
            }
        },
        content = { padding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(false),
                onRefresh = { refresh() },
            ) {
                Surface(modifier = Modifier.padding(padding)) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = users) { user ->
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = { setActiveRemoveUser(user) }
                                        )
                                    }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(user.name, style = MaterialTheme.typography.titleMedium)
                                    Text(user.email, style = MaterialTheme.typography.bodyMedium)
                                    Text("The status is ${user.status}", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun RemoveUserDialog(activeRemoveUser: User?, setRemoveUser: (User?) -> Unit, removeUser: (Int) -> Unit) {
    if (activeRemoveUser != null) {
        AlertDialog(
            onDismissRequest = { setRemoveUser(null) },
            title = {
                Text("Remove user")
            },
            text = {
                Text("Are you sure you want to remove this user?\n ${activeRemoveUser.name} (${activeRemoveUser.id})?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        removeUser(activeRemoveUser.id)
                        setRemoveUser(null)
                    },
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { setRemoveUser(null) },
                ) {
                    Text("No")
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserDialog(activeAddUser: Boolean, setActiveAddUser: (Boolean) -> Unit, addUser: (AddUser) -> Unit) {
    if (activeAddUser) {
        Dialog(onDismissRequest = { setActiveAddUser(false) }) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(8.dp),
            ) {
                Column(
                    Modifier
                        .background(White)
                ) {
                    var name by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }

                    Text(
                        text = "Fill user data",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text("User name") }
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text("User email") }
                    )

                    Row {
                        OutlinedButton(
                            onClick = { setActiveAddUser(false) },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                addUser(AddUser(name, email))
                                setActiveAddUser(false)
                            },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(text = "Add")
                        }
                    }


                }
            }
        }
    }
}


@Composable
private fun ErrorDialog(error: Throwable?, hideError: () -> Unit) {
    if (error != null) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Error")
            },
            text = {
                if (error is ApiException) {
                    Text("Server responded with code ${error.code}, message ${error.message}")
                } else {
                    Text("Unknown error $error")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        hideError()
                    },
                ) {
                    Text("Confirm")
                }
            },
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UsersListPreview() {
//    UserManagementAppTheme {
//        UsersList(UserListState.Loading, {}, {}, {})
//    }
//}