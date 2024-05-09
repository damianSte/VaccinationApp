package com.example.vaccinationapp.phpAdmin

import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.SignUpDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.UserProfileDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import java.sql.Connection
import java.sql.SQLException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class for handling Database queries related to user data
 *
 * @param connection  database connection
 */

open class DBQueries(private val connection: Connection) : DbUser {

    /**
     * Checks if a user exists in the database. Overrides DBUser
     *
     * @param email user's email.
     * @param password user's password
     * @return True(1) if user exists, false otherwise
     */
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

    /**
     * Inserts a new user into Database
     *
     * @param user user data (Sign up data class)
     * @return True, if  user was successfully inserted false otherwise
     */
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

    /**
     * Inserts a new vaccine Record into the database
     *
     * @param vaccine vaccine data AddVaccineDataClass
     * @return True if vaccine record was successfully inserted false otherwise
     */
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
    /**
     * Retrieves the user ID associated with the given email
     *
     * @param email  user's email
     * @return user Id if found, null otherwise
     */
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

    /**
     * Retrieves the vaccine ID associated with given vaccine name
     *
     * @param vaccineName the name of the vaccine.
     * @return  vaccine ID if found, null otherwise
     */

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

    /**
     * Retrieves  vaccine history for a specific user
     *
     * @param userId  ID of the user
     * @return A list of VaccineDataClass objects representing  Vaccine history.
     */
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
                val dosage = resultSet.getInt("dosage")

                val vaccine = VaccineDataClass(name, manufacturer, lastDose,null, dosage)
                vaccineList.add(vaccine)

            }

        } catch (e: SQLException) {
            // Handle SQL exceptions
            e.printStackTrace()
        }

        println(vaccineList)
        return vaccineList
    }

    /**
     * Retrieves vaccine history for a specific user showing only the most recent dose of each vaccine
     *
     * @param userId the ID of the user
     * @return A list of VaccineDataClass objects representing vaccine history.
     */
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

    /**
     * Retrieves upcoming Vaccine Appointments for a specific user
     *
     * @param userId  ID of the user
     * @param currentTime the current time in HH:mm format
     * @return list of VaccineDataClass objects representing   upcoming appointments
     */
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


    fun getAppointmentTime(userId: String): Array<String>? {
        val call = "{CALL getAppointmentTime(?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, userId)

        val resultSet = statement.executeQuery()

        var appointmentDate: String? = null
        var appointmentTime: String? = null

        if (resultSet.next()) {
            appointmentDate = resultSet.getString("date_of_vaccine")
            appointmentTime = resultSet.getString("hour")
        }

        resultSet.close()
        statement.close()

        if (appointmentDate == null || appointmentTime == null) {
            return null
        }

        return arrayOf(appointmentDate, appointmentTime)
    }

    /**
     * Insert the user profile data into the Database.
     *
     * @param profile the user profile data
     * @return true if the profile data was successfully inserted false otherwise
     */
    override fun insertProfile(profile: UserProfileDataClass): Boolean{

        val call = "{CALL insertProfile(?,?,?,?,?)}"
        val statement = connection.prepareCall(call)

        statement.setString(1, profile.userId)
        statement.setString(2, profile.pesel)
        statement.setString(3, profile.phoneNumber)
        statement.setString(4, profile.dateOfBirth)
        statement.setString(5, profile.name)

        val result = !statement.execute()

        statement.close()

        return result
    }


}
