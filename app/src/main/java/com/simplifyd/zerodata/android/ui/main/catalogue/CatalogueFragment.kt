package com.simplifyd.zerodata.android.ui.main.catalogue

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.ui.main.notification.NotificationViewModel
import com.simplifyd.zerodata.android.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalogue.*
import kotlinx.android.synthetic.main.fragment_catalogue.swipeRefresh
import kotlinx.android.synthetic.main.fragment_notification.*
import java.lang.Exception

class CatalogueFragment : Fragment(R.layout.fragment_catalogue), (ListedApp) -> Unit {
    private val viewModel by viewModels<CatalogueViewModel>()

    private val listedAppsAdapter by lazy { ListedAppsAdapter(this) }
    private val catalogueFilterAdapter by lazy { CatalogueFilterAdapter{
        when(it){
            "All" -> listedAppsAdapter.listedApps = listedApps
            else -> {
                listedAppsAdapter.listedApps = listedApps
                val listedAppsFiltered = listedApps.filter { listedApp -> listedApp.category == it }

                if(listedAppsFiltered.isEmpty()){
                    showToast("There are currently no apps in this category")
                }else{
                    listedAppsAdapter.listedApps = listedAppsFiltered
                }
            }
        }
    } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listedAppRecyclerView.adapter = listedAppsAdapter
        categoryRecyclerView.adapter = catalogueFilterAdapter

        requireActivity().toolbar_title_.text = getString(R.string.supported_apps)

        observeListedApps()
        viewModel.fetchAppCategories()

        requireActivity().findViewById<View>(R.id.notifications_link).setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                findNavController().navigate(R.id.action_navigation_catalogue_to_navigation_notification)


            }
        }



        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }




    private fun filter(text: String) {
        val filteredList = mutableListOf<ListedApp>()
        for (item in listedApps) {
            if (item.title.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        listedAppsAdapter.filterList(filteredList)
    }


    companion object {
        var listedApps = mutableListOf<ListedApp>()

        var categoryList =mutableListOf<String>()
//        val categoryList = listOf(
//            R.string.all,
//            R.string.social,
//            R.string.entertainment,
//            R.string.sports,
//            R.string.news,
//            R.string.books_ref
//        )
    }

    override fun invoke(listedApp: ListedApp) {

        when(listedApp.title){

             "WhatsApp" ->{
                 try {
                     openWebUrl("whatsapp://send?text=Join me in browsing apps and websites Data-Free on Zerodata: https://zerodata.app")

                 }catch (e: Exception){
                     openWebUrl(listedApp.url)
                 }
            }
            "Twitter" ->{
                try {
                    openWebUrl("twitter://user?user_id=1491948907059978242")

                }catch (e: Exception){
                    openWebUrl(listedApp.url)
                }
            }

            else ->{
                openWebUrl(listedApp.url)

            }
        }

    }

    private fun openWebUrl(url: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        swipeRefresh.isEnabled = isLoading
        swipeRefresh.isRefreshing = isLoading
    }

    fun observeListedApps(){
        viewModel.fetchCategories.observe(viewLifecycleOwner){
            showLoading(false)
            if (it != null){
                categoryList = it.toMutableList()
                catalogueFilterAdapter.filters = categoryList
                viewModel.fetchListedApps()
            }
        }

        viewModel.fetchListedApp.observe(viewLifecycleOwner) {
            showLoading(false)

            if (it != null) {

                listedApps = it.toMutableList()

                listedAppsAdapter.listedApps = it

            } else {
                showToast("You currently don't have any apps")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it){
                showLoading(true)
            }else{
                showLoading(false)
            }
        }

        viewModel.message.observe(viewLifecycleOwner){
            if (it != null) {
                showLoading(false)
                showToast(it)
            }
        }

    }


}