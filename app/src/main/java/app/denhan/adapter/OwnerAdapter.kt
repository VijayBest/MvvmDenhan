package app.denhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.denhan.android.R
import app.denhan.android.databinding.InstructionAdapterBinding
import app.denhan.android.databinding.OwnerAdapterBinding
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.owner.OwnerNotAvailableData

class OwnerAdapter(arrayList: ArrayList<OwnerNotAvailableData>, val listener:OwnerAdapterListener) :
    RecyclerView.Adapter<OwnerAdapter.ViewHolder>() {
    private var arrayList: ArrayList<OwnerNotAvailableData>


    init {
        setHasStableIds(true)
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: OwnerAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.owner_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
        holder.binding.imageAttachment.setOnClickListener {
            listener.onItemClickListener(arrayList[position])
        }


    }

    fun notifyAdapter(resultList: ArrayList<OwnerNotAvailableData>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: OwnerAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OwnerNotAvailableData) {

            binding.txtStatus.text= item.comment
            binding.txtDateTime.text = item.created_date

        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OwnerAdapterListener{
        fun onItemClickListener(clickedData:OwnerNotAvailableData)
    }
}

