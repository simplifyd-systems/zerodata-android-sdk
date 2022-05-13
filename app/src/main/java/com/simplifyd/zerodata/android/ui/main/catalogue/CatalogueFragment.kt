package com.simplifyd.zerodata.android.ui.main.catalogue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalogue.*

class CatalogueFragment : Fragment(R.layout.fragment_catalogue), (ListedApp) -> Unit {

    private val listedAppsAdapter by lazy { ListedAppsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listedAppRecyclerView.adapter = listedAppsAdapter
        listedAppsAdapter.listedApps = listedApps
        requireActivity().toolbar_title_.text = getString(R.string.supported_apps)

        requireActivity().findViewById<View>(R.id.notifications_link).setOnClickListener {
            (requireActivity() as? MainActivity)?.let {

                findNavController().navigate(R.id.action_navigation_catalogue_to_navigation_notification)


            }
        }

    }


    companion object {
        val listedApps = listOf(
            ListedApp(1, "Whatsapp",R.string.social, R.drawable.ic_whatsapp_logo),
            ListedApp(2, "Wikipedia",R.string.books_ref, R.drawable.ic_wikipedia_logo),
            ListedApp(3, "Livescore",R.string.sports, R.drawable.ic_livescore_logo),
            ListedApp(4, "Nairaland", R.string.social, R.drawable.ic_nairaland_logo),
            ListedApp(5, "Talksay",R.string.social, R.drawable.ic_talksay_logo),

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

    override fun invoke(p1: ListedApp) {
        TODO("Not yet implemented")
    }

}