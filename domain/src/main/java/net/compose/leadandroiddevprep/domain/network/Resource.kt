package net.compose.leadandroiddevprep.domain.network

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable) : Resource<Nothing>
    data class Empty(val showEmpty: Boolean = false) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}