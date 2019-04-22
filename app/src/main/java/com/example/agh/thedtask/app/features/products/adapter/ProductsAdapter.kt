package com.example.agh.wheatherapp.features.home.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.agh.thedtask.R
import com.example.agh.thedtask.entities.Product

import java.io.Serializable

@GlideModule
class GlideModule : AppGlideModule()


const val ACTION_SHOW_PRODUCT_BUTTON_CLICKED = "ACTION_SHOW_PRODUCT_BUTTON_CLICKED"
const val EXTRA_PRODUCT = "EXTRA_PRODUCT"

class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    // using findView... instead of synthetic because it can cause some problems in adapter
    private val tvTitle by lazy { view.findViewById<TextView>(R.id.tv_title) }
    private val tvPrice by lazy { view.findViewById<TextView>(R.id.tv_price) }
    private val ivProductImage by lazy { view.findViewById<ImageView>(R.id.iv_product_image) }

    fun bind(product: Product) {
        tvTitle.text = product.name
        tvPrice.text ="$${product.price}"



        Glide.with(view.context)
                .load(product.image.link)
                .apply(  RequestOptions().
                        diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .override(product.image.width.toInt() , product.image.height.toInt()))


                .into(ivProductImage)
        view.setOnClickListener {
            Intent(ACTION_SHOW_PRODUCT_BUTTON_CLICKED)
                    .putExtra(EXTRA_PRODUCT, product as Serializable)
                    .also { view.context.sendBroadcast(it) }
        }

    }

}

class ProductsAdapter(
        lifecycleOwner: LifecycleOwner,
        private val productsResult: MutableLiveData<List<Product>>
) : RecyclerView.Adapter<ProductViewHolder>() {

    init {
        // using lifeCycleOwner instead of context
        productsResult.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parentView: ViewGroup, p1: Int): ProductViewHolder {
        return LayoutInflater
                .from(parentView.context)
                .inflate(R.layout.recycler_item_products, parentView, false)
                .let { ProductViewHolder(it) }
    }

    override fun onBindViewHolder(viewHolder: ProductViewHolder, position: Int) {
        viewHolder.bind(productsResult.value!![position])

    }

    override fun getItemCount() = productsResult.value?.size ?: 0

}