package net.compose.leadandroiddevprep.data.local.keystore

interface MerchantKeyStore {
    fun saveToken(token: String)
    fun getToken(): String?
}