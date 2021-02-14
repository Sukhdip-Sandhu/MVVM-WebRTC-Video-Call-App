package com.example.webrtc_videocallapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.webrtc_videocallapp.databinding.ItemUserBinding


class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var _listOfUsers = ArrayList<String>()

    class ViewHolder(private val itemUserBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemUserBinding.root) {
        fun bind(username: String) {
            itemUserBinding.listItemUsername.text = username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemUserBinding)
    }

    override fun getItemCount(): Int {
        return _listOfUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = _listOfUsers[position]
        holder.bind(user)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(user) }
            }
        }
    }

    fun setUsersList(listOfUsers: java.util.ArrayList<String>?) {
        _listOfUsers.clear()
        _listOfUsers.addAll(listOfUsers!!)
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

}