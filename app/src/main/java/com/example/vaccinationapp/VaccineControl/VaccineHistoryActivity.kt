package com.example.vaccinationapp.VaccineControl

import com.example.vaccinationapp.Functional.VaccineAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VaccineHistoryActivity : BarHandler() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VaccineAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_history)
        openActivity(R.id.bottom_history)

        recyclerView = findViewById(R.id.vaccinesRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize an empty list for vaccine history
        val vaccineList = mutableListOf<VaccineDataClass>()

        // Initialize the adapter with the empty list
        adapter = VaccineAdapter(vaccineList)
        recyclerView.adapter = adapter

        // Call the function to fetch vaccine history
        getVaccineHistory()
    }

    private fun getVaccineHistory() {
        val userId = UserData.getUserId()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val userVaccineHistory = userId?.let { dbQueries.getVaccineHistory(it) }
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
}

