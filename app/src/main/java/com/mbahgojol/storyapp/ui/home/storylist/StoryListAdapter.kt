package com.mbahgojol.storyapp.ui.home.storylist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.databinding.ItemStoryBinding

class StoryListAdapter(
    private var listener: (
        GetAllStoryResponse.Story,
        ImageView,
        TextView,
        TextView
    ) -> Unit
) : PagingDataAdapter<GetAllStoryResponse.Story, StoryListAdapter.MainViewHolder>(REPO_COMPARATOR) {

    class MainViewHolder(val v: ItemStoryBinding) : RecyclerView.ViewHolder(v.root) {
        fun onBind(model: GetAllStoryResponse.Story) {
            v.title.text = model.name
            v.ivFoto.load(model.photoUrl)
            v.deskirpsi.text = model.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.onBind(item)
            holder.itemView.setOnClickListener {
                listener(item, holder.v.ivFoto, holder.v.title, holder.v.deskirpsi)
            }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GetAllStoryResponse.Story>() {
            override fun areItemsTheSame(
                oldItem: GetAllStoryResponse.Story,
                newItem: GetAllStoryResponse.Story
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: GetAllStoryResponse.Story,
                newItem: GetAllStoryResponse.Story
            ): Boolean =
                oldItem == newItem
        }
    }
}