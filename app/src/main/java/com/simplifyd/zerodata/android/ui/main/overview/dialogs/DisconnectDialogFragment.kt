package com.simplifyd.zerodata.android.ui.main.overview.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.dialog_disconnect.*

class DisconnectDialogFragment: DialogFragment(R.layout.dialog_disconnect) {

    private val parentCallback: Callback
        get() = parentFragment as Callback



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.setOnClickListener {
           parentCallback.onSelectSortParameter(PauseMode.DISABLE)
            dismiss()
        }

        cancel_action.setOnClickListener {
            dismiss()
        }


    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object {

        fun newInstance(): DisconnectDialogFragment {
            val args = Bundle()
            val fragment = DisconnectDialogFragment()
            fragment.arguments = args
            return fragment
        }

        interface Callback {
            fun onSelectSortParameter(pauseMode: PauseMode)
        }
    }
}
