package id.ac.unhas.foodcashier.loginfragment

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences (context: Context) {

    private val PREFS_PASS = "pass"
    private val PREFS_USERNAME = "username"
    private val PREFS_NAME = "name"

    private var sharedPrefPass: SharedPreferences = context.getSharedPreferences(PREFS_PASS, Context.MODE_PRIVATE)
    val editorPass: SharedPreferences.Editor = sharedPrefPass.edit()

    private var sharedPrefUsername: SharedPreferences = context.getSharedPreferences(PREFS_USERNAME, Context.MODE_PRIVATE)
    val editorUsername: SharedPreferences.Editor = sharedPrefUsername.edit()

    private var sharedPrefName: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editorName: SharedPreferences.Editor = sharedPrefName.edit()

    fun putPass(key: String, value: String) {
        editorPass.putString(key, value)
            .apply()
    }

    fun putUsername(key: String, value: String){
        editorUsername.putString(key, value)
            .apply()
    }

    fun putName(key: String, value: String){
        editorName.putString(key, value)
            .apply()
    }

    fun getStringPass(key: String): String? {
        return sharedPrefPass.getString(key, null)
    }

    fun getStringUserame(key: String): String? {
        return sharedPrefUsername.getString(key, null)
    }

    fun getStringName(key: String): String? {
        return sharedPrefName.getString(key, null)
    }

    fun clear() {
        editorPass.clear()
            .apply()
        editorUsername.clear()
            .apply()
        editorName.clear()
            .apply()
    }

}