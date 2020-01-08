package app.denhan.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ImageSliderAdapterBinding
import app.denhan.model.jobs.Attachment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageSliderAdapter(private val context: Context, private val imageModelArrayList: ArrayList<String>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val binding: ImageSliderAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.image_slider_adapter, view, false)
        Glide.with(binding.image.context)
            .load(imageModelArrayList.get(position))
            .apply(RequestOptions().placeholder(R.drawable.image_place_holder).error(R.drawable.image_place_holder))
            .into(binding.image)
        view.addView(binding.root, 0)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}