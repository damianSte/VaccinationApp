package com.example.vaccinationapp.phpAdmin

import java.sql.Connection
import java.sql.ResultSet

class DBQueries(private val connection: Connection) : DbUser {

    override fun userExists(email: String, password: String): Boolean {
        val query = "CALL getUser(?, ?)"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)
        callableStatement.setString(2, password)
        val resultSet = callableStatement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt(1)
        callableStatement.close()
        return count > 0
    }


    override fun insertUser(user: SignUpDataClass): Boolean {

        val call = "{CALL insertUser(?,?,?)}"
        val statement = connection.prepareCall(call)

        statement.setString(1, user.user_id)
        statement.setString(2, user.email)
        statement.setString(3, user.password)

        val result = !statement.execute()
        statement.close()
        return result
    }

    private fun mapResultSetToLogInDataClass(resultSet: ResultSet):
            LogInDataClass? {
        return LogInDataClass(
            email = resultSet.getString("email"),
            password = resultSet.getString("password")
        )
    }
}
