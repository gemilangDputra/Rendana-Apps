package com.gdproduct.rendana.data.local.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Suppress("DEPRECATION")
class SecureStorage(context: Context) {
    private val appContext = context.applicationContext
	
    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
		
        EncryptedSharedPreferences.create(
            appContext,
            "secure_user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
	
    fun saveApiKey(apiKey: String) {
        sharedPreferences.edit()
            .putString("GEMINI_API_KEY", apiKey)
            .apply()
    }
	
    fun getApiKey(): String? {
        return sharedPreferences.getString("GEMINI_API_KEY", null)
    }
	
    fun clearApiKey() {
        sharedPreferences.edit()
            .remove("GEMINI_API_KEY")
            .apply()
    }
}