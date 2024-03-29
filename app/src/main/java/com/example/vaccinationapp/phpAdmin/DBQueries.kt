package com.example.vaccinationapp.phpAdmin

import java.sql.Connection

class DBQueries (private val connection: Connection): DbUser {
    override fun getPassword(password: String): LogInDataClass? {
        TODO("Not yet implemented")
    }

    override fun getEmail(email: String): LogInDataClass? {
        TODO("Not yet implemented")
    }

    override fun getUserId(id: String): LogInDataClass? {
        TODO("Not yet implemented")
    }

    override fun insertUser(user: LogInDataClass): Boolean {

        val call = "{CALL insertUser(?,?,?)}"
        val statement = connection.prepareCall(call)

        statement.setString(1, user.user_id)
        statement.setString(2, user.email)
        statement.setString(3, user.password)

        val result = !statement.execute()
        statement.close()
        return result
    }


}