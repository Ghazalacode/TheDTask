package com.example.agh.thedtask.app.features.products

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agh.thedtask.R
import com.example.agh.thedtask.app.features.details.ProductDetailsFragment
import com.example.agh.thedtask.domain.engine.toast
import com.example.agh.wheatherapp.features.home.adapter.ACTION_SHOW_PRODUCT_BUTTON_CLICKED
import com.example.agh.wheatherapp.features.home.adapter.EXTRA_PRODUCT
import com.example.agh.wheatherapp.features.home.adapter.ProductsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_products_list.*
import java.io.Serializable
import io.reactivex.plugins.RxJavaPlugins



class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

       supportFragmentManager.beginTransaction().replace(R.id.container, ProductsListFragment()).commit()

        RxJavaPlugins.setErrorHandler { throwable -> }
    }

}

class ProductsListFragment : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(ProductsViewModel::class.java) }
    private val disposables = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = " Products List"


        viewModel.toastText
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it?.toast(context!!) }
                .also { disposables.add(it) }

        viewModel.retrieveProducts(activity)

        swipe_refresh_layout.setOnRefreshListener {
            viewModel.clearProducts()
            swipe_refresh_layout.isRefreshing = false
            viewModel.productsResult.postValue(listOf())
            viewModel.retrieveProducts(activity)

        }

        viewModel.retrieveProgress.observe(this, Observer {
            progressBar.visibility = if (it!!) View.VISIBLE else View.GONE

        })


        results_recycler_view.layoutManager = LinearLayoutManager(activity)
        results_recycler_view.adapter = ProductsAdapter(this, viewModel.productsResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(showButtonReceiver, IntentFilter(ACTION_SHOW_PRODUCT_BUTTON_CLICKED))

    }

    override fun onDestroy() {
        context?.unregisterReceiver(showButtonReceiver)
        disposables.dispose()
        super.onDestroy()
    }
    private val showButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            startDetailFragment(intent!!.getSerializableExtra(EXTRA_PRODUCT))
        }
    }

    private fun startDetailFragment(productSerializable: Serializable) {

        val productDetailsFragment = returnDetailFragment(productSerializable)
        fragmentManager!!.beginTransaction().add(R.id.container, productDetailsFragment).addToBackStack("ListFragment").commit()

    }

    private fun returnDetailFragment(productSerializable: Serializable): ProductDetailsFragment {
        val productDetailsFragment = ProductDetailsFragment()
        val extraBundle = Bundle()
        extraBundle.putSerializable(EXTRA_PRODUCT, productSerializable)
        productDetailsFragment.arguments = extraBundle
        return productDetailsFragment
    }
}