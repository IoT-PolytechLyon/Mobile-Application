package com.polytech.bmh.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import com.polytech.bmh.databinding.ItemViewBinding

class MyListAdapter(val clickListener: ConnectedDeviceListener) : ListAdapter<ConnectedDeviceProperties, MyListAdapter.ViewHolder>(UserDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<ConnectedDeviceProperties>() {
        override fun areItemsTheSame(oldItem: ConnectedDeviceProperties, newItem: ConnectedDeviceProperties): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ConnectedDeviceProperties, newItem: ConnectedDeviceProperties): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder private constructor(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConnectedDeviceProperties, clickListener: ConnectedDeviceListener) {
            binding.connectedDevice = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class ConnectedDeviceListener(val clickListener: (connectedDeviceId: String) -> Unit) {


    fun onClick(connectedDevice: ConnectedDeviceProperties) = clickListener(connectedDevice._id.toString())
}