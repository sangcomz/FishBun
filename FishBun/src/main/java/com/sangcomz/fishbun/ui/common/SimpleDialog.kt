package com.sangcomz.fishbun.ui.common

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sangcomz.fishbun.R

internal class SimpleDialog(private val onDismiss: (DialogInterface) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mesId = arguments?.getInt(ARG_KEY_MESSAGE)
            ?: throw IllegalArgumentException("can not access string resource.")
        return androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setMessage(mesId)
            .setPositiveButton(R.string.common_positive_button_label, null)
            .show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke(dialog)
    }

    internal companion object {

        private const val ARG_KEY_MESSAGE = "message"

        fun show(
            fm: FragmentManager,
            tag: String = "SimpleDialog",
            @StringRes mesId: Int,
            onDismiss: (DialogInterface) -> Unit,
        ) {
            SimpleDialog(onDismiss).apply {
                arguments = Bundle().apply {
                    putInt(ARG_KEY_MESSAGE, mesId)
                }
            }.show(fm, tag)
        }
    }
}
