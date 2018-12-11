package com.example.searchview

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author Alan Dreamer
 * @since 2018-12-05 Created
 */
data class SuggestionsItem(val id: String,
                           val query: String,
                           val subtitle: String = "") : AbstractFlexibleItem<SuggestionsItem.ViewHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.search_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.title.text = query
        holder.subtitle.isVisible = subtitle.isNotBlank()
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
        var title: TextView = view.findViewById(R.id.search_title)
        var subtitle: TextView = view.findViewById(R.id.search_subtitle)
    }
}