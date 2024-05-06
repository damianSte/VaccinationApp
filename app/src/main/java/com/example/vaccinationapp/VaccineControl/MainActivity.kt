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
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : BarHandler() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewScheduled: RecyclerView
    private lateinit var adapter: VaccineAdapter
    private lateinit var adapterScheduled: VaccineScheduledAdapter

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



        // Call the function to fetch vaccine history
        getVaccineHistory()
        getVaccineScheduled()
    }

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
                e.printStackTrace() // Consider handling this exception more gracefully
            }
        }
    }


    private fun getVaccineScheduled() {
        val userId = UserData.getUserId()

        // Get current time in HH:mm format
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
                e.printStackTrace() // Consider handling this exception more gracefully
            }
        }
    }


}