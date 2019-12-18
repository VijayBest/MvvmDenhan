package app.denhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.denhan.android.R
import app.denhan.android.databinding.OpenJobAdapterBinding
import app.denhan.model.jobs.Maintenance

class OpenJobsAdapter(arrayList: ArrayList<Maintenance>, val listener:OpenJobsAdapterListener) :
    RecyclerView.Adapter<OpenJobsAdapter.ViewHolder>() {
    private var arrayList: ArrayList<Maintenance>


    init {
        setHasStableIds(true)
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: OpenJobAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.open_job_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])

    }

    fun notifyAdapter(resultList: ArrayList<Maintenance>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: OpenJobAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Maintenance) {

            binding.dateTime.text = item.f_created_date+" At "+item.f_created_time
            binding.jobTittle.text = item.repair_code
            binding.addressText.text = (item.property.address)

        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OpenJobsAdapterListener {
        fun onItemClick(selectedListData: Maintenance)
    }



}
