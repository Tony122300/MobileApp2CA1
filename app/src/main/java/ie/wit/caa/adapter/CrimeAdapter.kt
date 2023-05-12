package ie.wit.caa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.caa.R
import ie.wit.caa.models.CaaModel
import ie.wit.caa.databinding.ActivityCrimeBinding
import ie.wit.caa.utils.customTransformation


interface ReportClickListener {
    fun onReportClick(caa: CaaModel)
}
class CrimeAdapter constructor(private var caas: ArrayList<CaaModel>,private val listener: ReportClickListener,private val readOnly: Boolean)
        : RecyclerView.Adapter<CrimeAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = ActivityCrimeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val caa = caas[holder.adapterPosition]
        holder.bind(caa, listener)
    }
    fun removeAt(position: Int) {
        caas.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = caas.size
// bind data from Caamodel to activity crime binding layout
    inner class MainHolder(val binding: ActivityCrimeBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        val readOnlyRow = readOnly
        fun bind(caa: CaaModel, listener: ReportClickListener) {
            binding.FullName.text = caa.name
            binding.Type.text = caa.type
            binding.root.tag = caa
            binding.caa = caa
            Picasso.get().load(caa.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onReportClick(caa) }
            binding.executePendingBindings()
        }
    }
}