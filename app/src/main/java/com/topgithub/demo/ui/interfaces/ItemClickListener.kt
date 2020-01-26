package com.topgithub.demo.ui.interfaces

import android.view.View
import com.topgithub.demo.models.RepositoryItem

@FunctionalInterface
interface ItemClickListener {
    fun onItemClick(item: RepositoryItem, view: View)
}