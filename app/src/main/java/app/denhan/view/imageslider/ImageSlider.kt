package app.denhan.view.imageslider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.denhan.adapter.ImageSliderAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivityImageSliderBinding
import app.denhan.util.ArrayConstant
import app.denhan.util.ArrayConstant.attachmentArrayList

class ImageSlider : AppCompatActivity() {
    lateinit var binding : ActivityImageSliderBinding
    lateinit var imageSliderAdapter: ImageSliderAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_slider)
        setAdapter()


    }
    /*setAdapter=>  Here we are setting the adapter for the attach images in
    * the selected Job  and user can see the image using the swipe gestures
    * */
    private fun setAdapter() {

        imageSliderAdapter = ImageSliderAdapter(this,attachmentArrayList)
        binding.imageSliderView.adapter= imageSliderAdapter
    }
}
