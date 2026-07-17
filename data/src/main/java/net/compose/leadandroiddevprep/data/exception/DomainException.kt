package net.compose.leadandroiddevprep.data.exception

import net.compose.leadandroiddevprep.domain.network.Resource
import retrofit2.HttpException
import java.io.IOException

sealed class DomainException(message: String? = null) : Exception(message) {
    object Network : DomainException("No internet connection")
    object Unauthorized : DomainException("Unauthorized")
    object NotFound : DomainException("Resource not found")
    object Server : DomainException("Server error")
    object Unknown : DomainException("Unknown error")
}

fun Throwable.toDomainException(): DomainException = when (this) {
    is IOException -> DomainException.Network
    is HttpException -> when (code()) {
        401 -> DomainException.Unauthorized
        404 -> DomainException.NotFound
        in 500..599 -> DomainException.Server
        else -> DomainException.Unknown
    }
    else -> DomainException.Unknown
}

suspend fun <T> safeApiCall(call: suspend () -> T): Resource<T> =
    try {
        Resource.Success(call())
    } catch (e: Throwable) {
        Resource.Error(e.toDomainException())
    }

