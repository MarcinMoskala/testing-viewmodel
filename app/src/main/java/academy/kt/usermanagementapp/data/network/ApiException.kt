package academy.kt.usermanagementapp.data.network

import retrofit2.Response

class ApiException(val code: Int, override val message: String): Exception(message)

fun <T> Response<T>.toResult(): Result<T> {
    if (this.isSuccessful) {
        return Result.success(this.body() as T)
    } else {
        return Result.failure(ApiException(this.code(), this.message()))
    }
}