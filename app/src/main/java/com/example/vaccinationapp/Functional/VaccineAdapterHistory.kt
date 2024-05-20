package com.example.vaccinationapp.Functional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinationapp.phpAdmin.DataClasses.VaccineDataClass
import com.example.vaccinationapp.R

/**
 * Adapter for displaying vaccine data in a RecyclerView
 *
 * @property vaccineList list of vaccine data.
 */
class VaccineAdapterHistory(private val vaccineList: MutableList<VaccineDataClass>, private val onDelete: (String) -> Unit)
    : RecyclerView.Adapter<VaccineAdapterHistory.VaccineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_tile_layout_history, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine = vaccineList[position]
        val sumsMap = calculateSumOfTakenVaccines(vaccineList)
        val sumForThisType = sumsMap[vaccine.name] ?: 0
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
        private val sumOfTakenVaccinesText: TextView = itemView.findViewById(R.id.sum)
        private val deleteButton: Button = itemView.findViewById(R.id.delete)
        private lateinit var recordId: String

        fun bind(vaccine: VaccineDataClass, sumOfTakenVaccines: Int) {
            nameText.text = vaccine.name
            manufacturerText.text = " • Manufacturer: ${vaccine.manufacturer}"
            lastDoseText.text = " • Date: ${vaccine.lastDose}"
            dosage.text = " • Recommended Dosage: ${vaccine.dosage}"
            sumOfTakenVaccinesText.text = " • Sum of Taken Vaccines: $sumOfTakenVaccines"
            recordId = vaccine.recordId ?: ""

            deleteButton.setOnClickListener {
                if (recordId.isNotEmpty()) {
                    onDelete(recordId)
                }
            }
        }
    }

    fun setData(newData: List<VaccineDataClass>) {
        vaccineList.clear()
        vaccineList.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(recordId: String) {
        val position = vaccineList.indexOfFirst { it.recordId == recordId }
        if (position != -1) {
            vaccineList.removeAt(position)
            notifyItemRemoved(position)
        }
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


