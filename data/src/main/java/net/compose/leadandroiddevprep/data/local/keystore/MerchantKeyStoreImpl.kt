package net.compose.leadandroiddevprep.data.local.keystore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject
import androidx.core.content.edit

class MerchantKeyStoreImpl @Inject constructor(private val context: Context) : MerchantKeyStore {

    private val AUTH_TOKEN = "secure_merchant_prefs"

    val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    val sharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "secure_merchant_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit { putString(AUTH_TOKEN, token) }
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }
}