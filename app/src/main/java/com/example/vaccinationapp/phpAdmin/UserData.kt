package com.example.vaccinationapp.phpAdmin


object UserData {
    private var userId: String? = null

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }
}
