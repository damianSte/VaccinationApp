package com.example.vaccinationapp.phpAdmin

import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.SignUpDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.UserProfileDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import java.sql.Connection
import java.sql.SQLException

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

//    private fun mapResultSetToLogInDataClass(resultSet: ResultSet):
//            LogInDataClass? {
//        return LogInDataClass(
//            email = resultSet.getString("email"),
//            password = resultSet.getString("password")
//        )
//    }

    override fun insertVaccine(vaccine: AddVaccineDataClass): Boolean {

        val call = "{CALL insertVaccine(?,?,?,?,?)}"
        val statement = connection.prepareCall(call)

        statement.setString(1, vaccine.vaccineId)
        statement.setString(2, vaccine.recordId)
        statement.setString(3, vaccine.userId)
        statement.setDate(4, vaccine.dateOfVaccine)
        statement.setString(5, vaccine.timeOfVaccine)

        val result = !statement.execute()
        statement.close()
        return result
    }

    fun getUserId(email: String): String? {
        val call = "{CALL getUserId(?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, email)
        val resultSet = statement.executeQuery()

        var userId: String? = null
        if (resultSet.next()) {
            userId = resultSet.getString("user_id")
        }

        resultSet.close()
        statement.close()

        return userId
    }

    fun getVaccineId(vaccineName: String): String? {
        val call = "{CALL getVaccineId(?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, vaccineName)
        val resultSet = statement.executeQuery()

        var vaccineID: String? = null
        if (resultSet.next()) {
            vaccineID = resultSet.getString("vaccine_id")
        }

        resultSet.close()
        statement.close()

        return vaccineID
    }

    fun getVaccineHistory(userId: String): List<VaccineDataClass> {
        val vaccineList = mutableListOf<VaccineDataClass>()

        try {
            val call = "{CALL getVaccineHistory(?, ?)}"
            val statement = connection.prepareCall(call)
            statement.setString(1, userId)

            val currentDate = java.sql.Date(System.currentTimeMillis())
            statement.setDate(2, currentDate)

            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val name = resultSet.getString("vaccine_name")
                val manufacturer = resultSet.getString("manufacturer")
                val lastDose = resultSet.getDate("date_of_vaccine")

                val vaccine = VaccineDataClass(name, manufacturer, lastDose)
                vaccineList.add(vaccine)
            }
        } catch (e: SQLException) {
            // Handle SQL exceptions
            e.printStackTrace()
        }

        println(vaccineList)
        return vaccineList
    }


    fun getOneVaccineHistory(userId: String): List<VaccineDataClass> {

        val vaccineList = mutableListOf<VaccineDataClass>()

        try {
            val call = "{CALL getOneVaccineHistory(?, ?)}"
            val statement = connection.prepareCall(call)
            statement.setString(1, userId)

            val currentDate = java.sql.Date(System.currentTimeMillis())
            statement.setDate(2, currentDate)

            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val name = resultSet.getString("vaccine_name")
                val manufacturer = resultSet.getString("manufacturer")
                val lastDose = resultSet.getDate("date_of_vaccine")

                val vaccine = VaccineDataClass(name, manufacturer, lastDose)
                vaccineList.add(vaccine)
            }
        } catch (e: SQLException) {
            // Handle SQL exceptions
            e.printStackTrace()
        }

        println(vaccineList)
        return vaccineList

    }

    fun getVaccineFuture(userId: String, currentTime: String): List<VaccineDataClass> {
        val vaccineList = mutableListOf<VaccineDataClass>()

        try {
            val call = "{CALL getVaccineFuture(?, ?)}"
            val statement = connection.prepareCall(call)
            statement.setString(1, userId)
            statement.setString(2, currentTime)

            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val name = resultSet.getString("vaccine_name")
                val manufacturer = resultSet.getString("manufacturer")
                val lastDose = resultSet.getDate("date_of_vaccine")
                val vaccineHour = resultSet.getString("hour")

                val vaccine = VaccineDataClass(name, manufacturer, lastDose, vaccineHour)
                vaccineList.add(vaccine)
            }
        } catch (e: SQLException) {
            // Handle SQL exceptions
            e.printStackTrace()
        }

        println(vaccineList)
        return vaccineList
    }

    fun getAppointmentTime(userId: String): String {

        val call = "{CALL getAppointmentTime(?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, userId)

        val currentDate = java.sql.Date(System.currentTimeMillis())
        statement.setDate(2, currentDate)

        val resultSet = statement.executeQuery()

        return resultSet.getString("hour")
    }

    fun insertProfile(userProfile: UserProfileDataClass): Boolean{

        val call = "{CALL insertProfile(?,?,?,?,?)}"
        val statement = connection.prepareCall(call)

        statement.setString(1, userProfile.userId)
        statement.setString(2, userProfile.pesel)
        statement.setString(3, userProfile.phoneNumber)
        statement.setString(4, userProfile.dateOfBirth)
        statement.setString(5, userProfile.name)

        val result = !statement.execute()

        statement.close()

        return result
    }

}
