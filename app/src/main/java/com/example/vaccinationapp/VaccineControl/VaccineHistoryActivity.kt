package com.example.vaccinationapp.VaccineControl

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.Functional.VaccineAdapterHistory
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
    private lateinit var adapter: VaccineAdapterHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_history)
        openActivity(R.id.bottom_history)

        recyclerView = findViewById(R.id.vaccinesRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val vaccineList = mutableListOf<VaccineDataClass>()
        adapter = VaccineAdapterHistory(vaccineList) { recordId ->
            deleteVaccine(recordId)
        }
        recyclerView.adapter = adapter

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

                withContext(Dispatchers.Main) {
                    userVaccineHistory?.let {
                        adapter.setData(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteVaccine(recordId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)
                dbQueries.deleteVaccineRecord(recordId)
                connection.close()

                withContext(Dispatchers.Main) {
                    adapter.removeItem(recordId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

