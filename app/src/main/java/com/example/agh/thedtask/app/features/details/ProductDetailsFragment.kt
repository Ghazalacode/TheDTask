package com.example.agh.thedtask.app.features.details

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.agh.thedtask.R
import com.example.agh.thedtask.app.features.products.ActivityViewModel
import com.example.agh.thedtask.entities.Product
import com.example.agh.wheatherapp.features.home.adapter.EXTRA_PRODUCT
import kotlinx.android.synthetic.main.fragment_product_details.view.*
import java.io.Serializable

class ProductDetailsFragment : Fragment() {

   val activityViewModel by lazy { ViewModelProviders.of(activity!!).get(ActivityViewModel::class.java) }
 override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = "Product Details"

    arguments?.apply {
                  //  productSerializable = getSerializable(EXTRA_PRODUCT)
                    val product = getSerializable(EXTRA_PRODUCT) as Product
                with(view){

                    //getting choosed product from list and displaying it
                    tv_price.text  = "$${product.price}"
                    tv_title.text  = product.name
                    tv_product_descreption.text  = product.productDescription
                    Glide.with(view.context)
                            .load(product.image.link)
                            .apply(  RequestOptions().
                                    diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .override(product.image.width.toInt() , product.image.height.toInt()))

                            .into(iv_product_image)
                }
                }

        // adding current fragment to activity ViewModel if not already added
        with(activityViewModel.fragment.value!!){
            if ( ! this.contains(this@ProductDetailsFragment) )add(this@ProductDetailsFragment)
        }


    }


}