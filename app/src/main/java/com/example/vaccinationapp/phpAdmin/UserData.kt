package com.example.vaccinationapp.phpAdmin

/**
 * Singleton class to save user Id during app exploitation
 */
object UserData {
    private var userId: String? = null


    // Getter and Setter for userId
    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }


}
