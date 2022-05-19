package com.simplifyd.zerodata.android.ui.main.catalogue

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalogue.*
import java.lang.Exception

class CatalogueFragment : Fragment(R.layout.fragment_catalogue), (ListedApp) -> Unit {

    private val listedAppsAdapter by lazy { ListedAppsAdapter(this) }
    private val catalogueFilterAdapter by lazy { CatalogueFilterAdapter{
        when(it){
            R.string.all -> listedAppsAdapter.listedApps = listedApps

            R.string.social -> {
                listedAppsAdapter.listedApps = listedApps
                listedAppsAdapter.listedApps = listedApps.filter { listedApp -> listedApp.category == it }
            }

            R.string.sports ->{
                listedAppsAdapter.listedApps = listedApps
                listedAppsAdapter.listedApps = listedApps.filter { listedApp -> listedApp.category == it }
            }

            R.string.books_ref ->{
                listedAppsAdapter.listedApps = listedApps
                listedAppsAdapter.listedApps = listedApps.filter { listedApp -> listedApp.category == it }
            }
            else -> showToast("There are currently no apps in this category")
        }
    } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listedAppRecyclerView.adapter = listedAppsAdapter
        categoryRecyclerView.adapter = catalogueFilterAdapter
        listedAppsAdapter.listedApps = listedApps
        catalogueFilterAdapter.filters = categoryList

        requireActivity().toolbar_title_.text = getString(R.string.supported_apps)

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
        val listedApps = listOf(
            ListedApp(1, "Whatsapp","https://www.whatsapp.com", R.string.social, R.drawable.ic_whatsapp_logo),
            ListedApp(2, "Twitter","https://twitter.com/getzerodata", R.string.social, R.drawable.ic_twitter_logo),
            ListedApp(3, "Wikipedia","https://www.wikipedia.org", R.string.books_ref, R.drawable.ic_wikipedia_logo),
            ListedApp(4, "Livescore","https://www.livescore.com/en/", R.string.sports, R.drawable.ic_livescore_logo),
            ListedApp(5, "Nairaland","https://www.nairaland.com", R.string.social, R.drawable.ic_nairaland_logo),
            ListedApp(6, "Talksay","https://www.talksay.io", R.string.social, R.drawable.ic_talksay_logo),
        )

        val categoryList = listOf(
            R.string.all,
            R.string.social,
            R.string.entertainment,
            R.string.sports,
            R.string.news,
            R.string.books_ref
        )
    }

    override fun invoke(listedApp: ListedApp) {

        when(listedApp.title){

             "Whatsapp" ->{
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


}