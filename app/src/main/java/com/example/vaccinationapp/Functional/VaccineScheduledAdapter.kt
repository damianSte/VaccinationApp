package com.example.vaccinationapp.Functional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import com.example.vaccinationapp.R


class VaccineScheduledAdapter(private val vaccineList: MutableList<VaccineDataClass>) :
    RecyclerView.Adapter<VaccineScheduledAdapter.VaccineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_tile_layout_scheduled, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine = vaccineList[position]
        holder.bind(vaccine)
    }

    override fun getItemCount(): Int {
        return vaccineList.size
    }

    inner class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.name_text)
        private val manufacturerText: TextView = itemView.findViewById(R.id.manufacturer_text)
        private val lastDoseText: TextView = itemView.findViewById(R.id.last_dose_text)
        private val timeText: TextView = itemView.findViewById(R.id.appointment_time_text)

        fun bind(vaccine: VaccineDataClass) {
            nameText.text = vaccine.name
            manufacturerText.text = " • Manufacturer: ${vaccine.manufacturer}"
            lastDoseText.text = " • Date: ${vaccine.lastDose}"
            timeText.text = " • Appointment time: ${vaccine.time}"
        }
    }

    fun setData(newData: List<VaccineDataClass>) {
        // Clear previous data
        vaccineList.clear()
        // Add new data
        vaccineList.addAll(newData)
        // Notify adapter about data change
        notifyDataSetChanged()
    }
}

