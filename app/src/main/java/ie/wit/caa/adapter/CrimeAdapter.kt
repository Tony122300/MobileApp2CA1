package ie.wit.caa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.caa.R
import ie.wit.caa.models.CaaModel
import ie.wit.caa.databinding.ActivityCrimeBinding


interface ReportClickListener {
    fun onReportClick(caa: CaaModel)
}
class CrimeAdapter constructor(private var caas: List<CaaModel>,private val listener: ReportClickListener)
        : RecyclerView.Adapter<CrimeAdapter.MainHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val binding = ActivityCrimeBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return MainHolder(binding)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            val caa = caas[holder.adapterPosition]
            holder.bind(caa,listener)
        }

        override fun getItemCount(): Int = caas.size

        inner class MainHolder(val binding : ActivityCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(caa: CaaModel, listener: ReportClickListener) {
               // binding.FullName.text = caa.name
               // binding.name.setText(caa.name)
                binding.caa = caa
                binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
                binding.root.setOnClickListener { listener.onReportClick(caa) }
                binding.executePendingBindings()
            }
        }
    }