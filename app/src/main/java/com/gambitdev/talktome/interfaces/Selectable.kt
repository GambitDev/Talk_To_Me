package com.gambitdev.talktome.interfaces

import androidx.recyclerview.widget.RecyclerView

interface Selectable {
    fun <T : RecyclerView.ViewHolder> select(position: Int, holder: T)
    fun <T : RecyclerView.ViewHolder>deselect(position: Int, holder: T)
}