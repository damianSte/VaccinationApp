package com.example.vaccinationapp.phpAdmin

/**
 * Singleton class to save user Id during app exploitation
 */
object UserData {
    private var userId: String? = null

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }
}
