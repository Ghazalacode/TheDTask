package com.example.agh.thedtask.app.features.products

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
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
import com.example.agh.thedtask.domain.engine.logd
import com.example.agh.thedtask.domain.engine.toast
import com.example.agh.wheatherapp.features.home.adapter.ACTION_SHOW_PRODUCT_BUTTON_CLICKED
import com.example.agh.wheatherapp.features.home.adapter.EXTRA_PRODUCT
import com.example.agh.wheatherapp.features.home.adapter.ProductsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.fragment_products_list.*
import java.io.Serializable

typealias  FragmentList = MutableLiveData<MutableList<Fragment>>
class ActivityViewModel(  ) : ViewModel(){
    // list of opened fragment used on rotation and back press
    val fragment: FragmentList = MutableLiveData()

}

class ProductsActivity : AppCompatActivity() {

  val viewModel by lazy { ViewModelProviders.of(this).get(ActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

// using activity viewmodel to control screen rotation
        if (viewModel.fragment.value == null)  transact(ProductsListFragment())
        else transact(viewModel.fragment.value!!.last())

        RxJavaPlugins.setErrorHandler { throwable -> throwable.message?.logd() }
    }


    override fun onBackPressed() {
        super.onBackPressed()

        val fragmentList  = viewModel.fragment.value
        // popping last fragment and inflating the one in turn
        fragmentList?.apply { remove(last()) }
        if (! fragmentList?.isEmpty()!!) transact(fragmentList.last()) else finish()
    }

    fun transact(fragment: Fragment){
        supportFragmentManager.beginTransaction().
            replace(R.id.container, fragment).commit()
    }

}

class ProductsListFragment : Fragment() {

    val activityViewModel by lazy { ViewModelProviders.of(activity!!).get(ActivityViewModel::class.java) }
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
          // clearing database data and retrieve new data
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

        // adding current fragment to activity ViewModel
    activityViewModel.fragment.value.takeIf { it==null }
            .apply {
                val list = mutableListOf<Fragment>()
                list.add(this@ProductsListFragment)
                activityViewModel.fragment.postValue(list)
            }

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
        fragmentManager!!.beginTransaction().add(R.id.container, productDetailsFragment)
                .addToBackStack("DetailFragment").commit()

    }

    private fun returnDetailFragment(productSerializable: Serializable): ProductDetailsFragment {

        val bundle  =Bundle().apply {  putSerializable(EXTRA_PRODUCT, productSerializable) }
        return ProductDetailsFragment().apply {arguments =  bundle }

    }
}

