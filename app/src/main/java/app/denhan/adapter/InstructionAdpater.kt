package app.denhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.denhan.android.R
import app.denhan.android.databinding.InstructionAdapterBinding
import app.denhan.model.jobs.MaintenanceInstruction

class InstructionAdapter(arrayList: ArrayList<MaintenanceInstruction>) :
    RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {
    private var arrayList: ArrayList<MaintenanceInstruction>


    init {
        setHasStableIds(true)
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: InstructionAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.instruction_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])


    }

    fun notifyAdapter(resultList: ArrayList<MaintenanceInstruction>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: InstructionAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MaintenanceInstruction) {

            binding.txtInstruction.text = "# "+ item.detail
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
