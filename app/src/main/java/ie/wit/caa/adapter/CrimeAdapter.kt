package ie.wit.caa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.caa.R
import ie.wit.caa.models.CaaModel
import ie.wit.caa.databinding.ActivityCrimeBinding

class CrimeAdapter constructor(private var caas: List<CaaModel>)
        : RecyclerView.Adapter<CrimeAdapter.MainHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val binding = ActivityCrimeBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return MainHolder(binding)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            val caa = caas[holder.adapterPosition]
            holder.bind(caa)
        }

        override fun getItemCount(): Int = caas.size

        inner class MainHolder(val binding : ActivityCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(caa: CaaModel) {
                binding.FullName.text = caa.name
               // binding.name.setText(caa.name)
                binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            }
        }
    }