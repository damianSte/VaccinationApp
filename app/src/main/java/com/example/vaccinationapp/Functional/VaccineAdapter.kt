package com.example.vaccinationapp.Functional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import com.example.vaccinationapp.R

class VaccineAdapter(private val vaccineList: MutableList<VaccineDataClass>) :
    RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_tile_layout, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine = vaccineList[position]
        val sumsMap = calculateSumOfTakenVaccines(vaccineList)
        val sumForThisType = sumsMap[vaccine.name] ?: 0 // Get the sum for this vaccine type, default to 0 if not found
        holder.bind(vaccine, sumForThisType)
    }

    override fun getItemCount(): Int {
        return vaccineList.size
    }

    inner class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.name_text)
        private val manufacturerText: TextView = itemView.findViewById(R.id.manufacturer_text)
        private val lastDoseText: TextView = itemView.findViewById(R.id.last_dose_text)
        private val dosage: TextView = itemView.findViewById(R.id.dosage)
        private val sumOfTakenVaccinesText: TextView = itemView.findViewById(R.id.sum) // TextView to display the sum

        fun bind(vaccine: VaccineDataClass, sumOfTakenVaccines: Int) {
            nameText.text = vaccine.name
            manufacturerText.text = " • Manufacturer: ${vaccine.manufacturer}"
            lastDoseText.text = " • Date: ${vaccine.lastDose}"
            dosage.text = " • Recommended Dosage: ${vaccine.dosage}"
            sumOfTakenVaccinesText.text = " • Sum of Taken Vaccines: $sumOfTakenVaccines" // Display the sum
        }
    }

    fun setData(newData: List<VaccineDataClass>) {
        vaccineList.clear()
        vaccineList.addAll(newData)
        notifyDataSetChanged()
    }

    fun calculateSumOfTakenVaccines(vaccineList: List<VaccineDataClass>): Map<String, Int> {
        val vaccineCountMap = HashMap<String, Int>()
        for (vaccine in vaccineList) {
            val vaccineType = vaccine.name
            vaccineCountMap[vaccineType] = (vaccineCountMap[vaccineType] ?: 0) + 1
        }
        return vaccineCountMap
    }
}
