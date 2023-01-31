package academy.kt.usermanagementapp.data.network

import academy.kt.usermanagementapp.model.AddUser
import academy.kt.usermanagementapp.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import javax.inject.Inject

interface UserRepository {
    suspend fun fetchUsers(): Result<List<User>>
    suspend fun removeUser(id: Int): Result<Unit>
    suspend fun addUser(addUser: AddUser): Result<Unit>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkUserRepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        networkUserRepository: NetworkUserRepository
    ): UserRepository
}

class NetworkUserRepository @Inject constructor(
    retrofit: Retrofit
) : UserRepository {
    private val api = retrofit.create(UserApi::class.java)

    override suspend fun fetchUsers(): Result<List<User>> =
        api.fetchUsers()
            .toResult()
            .map { it.map { it.toUser() } }

    override suspend fun removeUser(id: Int): Result<Unit> =
        api.deleteUser(id)
            .toResult()

    override suspend fun addUser(addUser: AddUser): Result<Unit> =
        api.addUser(addUser.toAddUserJson())
            .toResult()

    interface UserApi {
        @GET("users")
        suspend fun fetchUsers(): Response<List<UserJson>>

        @DELETE("users/{userId}")
        suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>

        @POST("users")
        suspend fun addUser(@Body addUserJson: AddUserJson): Response<Unit>
    }

    class UserJson(
        val id: Int,
        val name: String,
        val email: String,
        val gender: String,
        val status: String
    )

    class AddUserJson(
        val name: String,
        val email: String,
        val gender: String,
        val status: String,
    )
}

fun NetworkUserRepository.UserJson.toUser() = User(
    id = this.id,
    name = this.name,
    email = this.email,
    status = this.status,
)

fun AddUser.toAddUserJson() = NetworkUserRepository.AddUserJson(
    name = this.name,
    email = this.email,
    gender = "female", // TODO
    status = "active",
)