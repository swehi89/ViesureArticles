package com.sp.viesurearticles.articles

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sp.viesurearticles.data.entity.Article

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Article>?){
    items?.let { (listView.adapter as ArticlesAdapter).submitList(items) }
}