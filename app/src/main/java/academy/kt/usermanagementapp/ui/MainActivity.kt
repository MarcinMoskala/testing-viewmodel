package academy.kt.usermanagementapp.ui

import academy.kt.usermanagementapp.presentation.UserListViewModel
import academy.kt.usermanagementapp.ui.theme.UserManagementAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userListViewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserManagementAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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