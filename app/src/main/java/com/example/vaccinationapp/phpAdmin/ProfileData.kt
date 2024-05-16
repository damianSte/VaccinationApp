package com.example.vaccinationapp.phpAdmin

object ProfileData {

    private var userName: String? = null
    private var userNumber: String? = null
    private var userPesel: String? = null
    private var userDoB: String? = null

    // Getter and Setter for userName
    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    // Getter and Setter for userPesel
    fun getUserPesel(): String? {
        return userPesel
    }

    fun setUserPesel(userPesel: String) {
        this.userPesel = userPesel
    }

    // Getter and Setter for userDoB
    fun getUserDoB(): String? {
        return userDoB
    }

    fun setUserDoB(userDoB: String) {
        this.userDoB = userDoB
    }

    fun getUserNumber(): String? {
        return userNumber
    }

    fun setUserNumber(userNumber: String) {
        this.userNumber = userNumber
    }
}