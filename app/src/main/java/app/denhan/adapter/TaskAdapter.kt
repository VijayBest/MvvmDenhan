package app.denhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.denhan.android.R
import app.denhan.android.databinding.TaskAdapterBinding
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.util.AppConstants

class TaskAdapter(arrayList: ArrayList<MaintenanceJob>, val listener:TaskAdapterListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var arrayList: ArrayList<MaintenanceJob>


    init {
        setHasStableIds(true)
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: TaskAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.task_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
        holder.binding.mainLayout.setOnClickListener {

            listener.onItemClick(arrayList[position])
        }

    }

    fun notifyAdapter(resultList: ArrayList<MaintenanceJob>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: TaskAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MaintenanceJob) {

            item.f_modified_date?.let {
                binding.dateTime.text = it
            }
            item.job_detail?.let {
                binding.taskTittle.text = it
            }




            when(item.status){

                AppConstants.notStarted->{

                    binding.statusImage.setImageResource(R.drawable.ic_icon_not_completed)
                    binding.taskStatus.text = binding.taskStatus.context.resources.getString(R.string.not_completed_status)
                }
                AppConstants.inProgress->{
                    binding.statusImage.setImageResource(R.drawable.ic_icon_inprogress)
                    binding.taskStatus.text = binding.taskStatus.context.resources.getString(R.string.in_progress_text)

                }

                AppConstants.completed->{

                    binding.statusImage.setImageResource(R.drawable.ic_icon_complted)
                    binding.taskStatus.text = binding.taskStatus.context.resources.getString(R.string.completed_status)
                }
            }

        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface TaskAdapterListener {
        fun onItemClick(selectedTaskData: MaintenanceJob)
    }



}
