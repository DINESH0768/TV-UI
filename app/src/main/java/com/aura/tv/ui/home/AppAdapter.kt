package com.aura.tv.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aura.tv.R
import com.aura.tv.databinding.ItemAppCardBinding
import com.aura.tv.domain.models.InstalledApp

class AppAdapter(
    private val onAppClick: (InstalledApp) -> Unit
) : ListAdapter<InstalledApp, AppAdapter.AppViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding, onAppClick)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppViewHolder(
        private val binding: ItemAppCardBinding,
        private val onAppClick: (InstalledApp) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val item = itemView.tag as? InstalledApp
                if (item != null) {
                    onAppClick(item)
                }
            }

            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.animate().scaleX(1.08f).scaleY(1.08f).setDuration(150).start()
                } else {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }
            }
        }

        fun bind(app: InstalledApp) {
            itemView.tag = app
            binding.tvAppTitle.text = app.label
            if (app.icon != null) {
                binding.ivAppIcon.setImageDrawable(app.icon)
            } else {
                binding.ivAppIcon.setImageResource(R.drawable.ic_apps)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<InstalledApp>() {
        override fun areItemsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean =
            oldItem == newItem
    }
}
