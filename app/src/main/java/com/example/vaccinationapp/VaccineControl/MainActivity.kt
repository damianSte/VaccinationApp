package com.example.vaccinationapp.VaccineControl

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.Functional.VaccineAdapter
import com.example.vaccinationapp.Functional.VaccineScheduledAdapter
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
/**
 * Main activity responsible for displaying vaccine history and scheduled vaccines.
 *
 * This activity displays the user's vaccine history and scheduled vaccines using RecyclerViews.
 */
class MainActivity : BarHandler() {

    // RecyclerView for displaying the most recent vaccine appointment
    private lateinit var recyclerView: RecyclerView
    // RecyclerView for displaying all scheduled vaccine appointments
    private lateinit var recyclerViewScheduled: RecyclerView
    // Adapter for one recent vaccine
    private lateinit var adapter: VaccineAdapter
    // Adapter for scheduled vaccine appointments
    private lateinit var adapterScheduled: VaccineScheduledAdapter

    /**
     * Initialize the activity, RecycleView and fetches data from database
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openActivity(R.id.bottom_home)

        recyclerView = findViewById(R.id.vaccinesRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerViewScheduled = findViewById(R.id.vaccineScheduledRecycleView)
        recyclerViewScheduled.layoutManager = LinearLayoutManager(this)

        // Initialize an empty list for vaccine history
        val vaccineList = mutableListOf<VaccineDataClass>()
        val vaccineScheduledList = mutableListOf<VaccineDataClass>()

        // Initialize the adapter with the empty list
        adapter = VaccineAdapter(vaccineList)
        recyclerView.adapter = adapter

        adapterScheduled = VaccineScheduledAdapter(vaccineScheduledList)
        recyclerViewScheduled.adapter = adapterScheduled


        // Functions call to fetch vaccines from database
        getVaccineHistory()
        getVaccineScheduled()

    }

    /**
     * Fetches the user's vaccine history from the database and updates Ui
     */

    private fun getVaccineHistory() {
        val userId = UserData.getUserId()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val userVaccineHistory = userId?.let { dbQueries.getOneVaccineHistory(it) }
                connection.close()

                // Update UI with the fetched vaccine history
                withContext(Dispatchers.Main) {
                    userVaccineHistory?.let {
                        // Clear previous data and update adapter with new data
                        adapter.setData(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Fetches the user's scheduled vaccine from the database and updates Ui
     */

    private fun getVaccineScheduled() {
        val userId = UserData.getUserId()


        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val userVaccineHistory = userId?.let { dbQueries.getVaccineFuture(it, currentTime) }
                connection.close()

                // Update UI with the fetched vaccine history
                withContext(Dispatchers.Main) {
                    userVaccineHistory?.let {
                        // Clear previous data and update adapter with new data
                        adapterScheduled.setData(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}