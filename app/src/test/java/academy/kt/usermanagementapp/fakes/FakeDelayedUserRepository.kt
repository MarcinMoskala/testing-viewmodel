package academy.kt.usermanagementapp.fakes

import academy.kt.usermanagementapp.data.network.UserRepository
import academy.kt.usermanagementapp.model.AddUser
import academy.kt.usermanagementapp.model.User
import kotlinx.coroutines.delay

class FakeDelayedUserRepository : UserRepository {
    var users = listOf<User>()
    private var nextId = 0
    var fetchUsersFailure: Throwable? = null
    var removeUserFailure: Throwable? = null
    var addUserFailure: Throwable? = null

    override suspend fun fetchUsers(): Result<List<User>> {
        delay(FETCH_USERS_DELAY)
        fetchUsersFailure?.let { return Result.failure(it) }
        return Result.success(users)
    }

    override suspend fun removeUser(id: Int): Result<Unit> {
        delay(REMOVE_USER_DELAY)
        removeUserFailure?.let { return Result.failure(it) }
        users = users.filter { it.id != id }
        return Result.success(Unit)
    }

    override suspend fun addUser(addUser: AddUser): Result<Unit> {
        delay(ADD_USER_DELAY)
        addUserFailure?.let { return Result.failure(it) }
        users = users + User(nextId++, addUser.name, addUser.email, DEFAULT_STATUS)
        return Result.success(Unit)
    }

    fun hasUsers(users: List<User>) {
        this.users = users
    }

    companion object {
        val FETCH_USERS_DELAY = 123L
        val REMOVE_USER_DELAY = 234L
        val ADD_USER_DELAY = 345L
        private val DEFAULT_STATUS = "active"
    }
}