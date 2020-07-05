package com.gambitdev.talktome.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.gambitdev.talktome.activities.GalleryActivity
import com.gambitdev.talktome.R
import com.gambitdev.talktome.activities.ChatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteItemsBottomSheet(private val selectedCount: Int,
                             private val images: Boolean)
    : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View =
                inflater.inflate(R.layout.delete_items_bottom_sheet,
                        container,
                        false)
        if (images)
            view.findViewById<TextView>(R.id.delete_conformation_tv)
                    .text = getString(R.string.delete_images_title, selectedCount)
        else
            view.findViewById<TextView>(R.id.delete_conformation_tv)
                    .text = getString(R.string.delete_messages_title, selectedCount)
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener { dismiss() }
        view.findViewById<Button>(R.id.delete_btn).setOnClickListener {
            if (images)
                (context as GalleryActivity).deleteImages()
            else
                (context as ChatActivity).deleteItems()
            dismiss()
        }
        return view
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}