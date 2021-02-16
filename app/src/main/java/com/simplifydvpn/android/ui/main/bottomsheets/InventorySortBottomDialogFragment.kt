package com.simplifydvpn.android.ui.main.bottomsheets

import android.os.Bundle
import android.view.View
import com.simplifydvpn.android.R
import kotlinx.android.synthetic.main.fragment_bottom_dialog_inventory_sort.*

class InventorySortBottomDialogFragment : BaseRoundedBottomSheetDialogFragment() {

    private val parentCallback: Callback
        get() = parentFragment as Callback

    override fun getLayoutRes(): Int = R.layout.fragment_bottom_dialog_inventory_sort

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // applies initial and subsequent check on selected button
        sort_btns_group.addOnButtonCheckedListener { _, checkedId, _ ->
            parentCallback.onSelectSortParameter(mapSelectedSortBtn(checkedId)!!)
            dismiss()
        }
    }

    private fun mapSelectedSortBtn(id: Int): PauseMode? {
        return when (id) {
            R.id.pause_for_fifteen -> PauseMode.PAUSE_FOR_FIFTEEN_MINUTES
            R.id.pause_for_one_hour -> PauseMode.PAUSE_FOR_ONE_HOUR
            R.id.disable_simplifyd -> PauseMode.DISABLE
            R.id.cancel_action -> PauseMode.CANCEL
            else -> null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object {

        fun newInstance(): InventorySortBottomDialogFragment {
            val args = Bundle()
            val fragment = InventorySortBottomDialogFragment()
            fragment.arguments = args
            return fragment
        }

        interface Callback {
            fun onSelectSortParameter(pauseMode: PauseMode)
        }
    }
}
