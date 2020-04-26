package com.sp.viesurearticles.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.databinding.ArticleItemBinding

class ArticlesAdapter(private val viewModel: ArticlesViewModel) :
    ListAdapter<Article, ArticlesAdapter.ArticleViewHoler>(ArticleDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHoler {
        return ArticleViewHoler.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHoler, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    class ArticleViewHoler private constructor(val binding: ArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ArticlesViewModel, item: Article) {

            binding.viewmodel = viewModel
            binding.article = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ArticleViewHoler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArticleItemBinding.inflate(layoutInflater, parent, false)

                return ArticleViewHoler(binding)
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}