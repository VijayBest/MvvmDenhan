package app.denhan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.denhan.android.R
import app.denhan.android.databinding.ImageAdapterBinding
import app.denhan.model.subtask.MaintenanceJobImage
import com.bumptech.glide.Glide

class AttachmentAdapter(arrayList: ArrayList<MaintenanceJobImage>, adapterStatus:String,val listener:AttachmentAdapterListener) :
    RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {
    private var arrayList: ArrayList<MaintenanceJobImage>
    private lateinit var adapterStatus:String


    init {
        setHasStableIds(true)
        this.arrayList = arrayList
        this.adapterStatus=adapterStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ImageAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.image_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList,position)


        holder.binding.removeImage.setOnClickListener {
            listener.removeImage(arrayList[position],adapterStatus)
        }
        if (position==0) {
            holder.binding.imageGallery.setOnClickListener {
                listener.onItemClickListener(arrayList[position], adapterStatus)
            }
        }


    }

    fun notifyAdapter(resultList: ArrayList<MaintenanceJobImage>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: ImageAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            arrayList: ArrayList<MaintenanceJobImage>,
            position: Int) {
            val item =arrayList[position]
            if (position==0){
                Glide
                    .with(binding.imageGallery.context)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.ic_icon_camera)
                    .into(binding.imageGallery)
                binding.removeImage.visibility = View.GONE
            }
            else{
                Glide
                    .with(binding.imageGallery.context)
                    .load(item.image_path)
                    .centerCrop()
                    .placeholder(R.drawable.ic_icon_camera)
                    .into(binding.imageGallery)
                binding.removeImage.visibility = View.VISIBLE
            }


        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface AttachmentAdapterListener{
        fun onItemClickListener(
            clickedData: MaintenanceJobImage,
            adapterStatus: String
        )
        fun removeImage(
            clickedData: MaintenanceJobImage,
            adapterStatus: String
        )


    }
}

