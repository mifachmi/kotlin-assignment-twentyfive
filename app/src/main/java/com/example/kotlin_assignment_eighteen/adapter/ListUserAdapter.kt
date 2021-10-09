package com.example.kotlin_assignment_eighteen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_assignment_eighteen.activity.UsersActivity
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.FILE_PATH
import com.example.kotlin_assignment_eighteen.databinding.UserRowListBinding
import com.example.kotlin_assignment_eighteen.model.DataItemUser

class ListUserAdapter(private val listUser: List<DataItemUser>) : RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemUser)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListUserHolder(private val binding: UserRowListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: DataItemUser) {
            with(binding){
                tvIdUser.text = users.id
                tvFullNameHistory.text = users.nameUser
                tvEmailHistory.text = users.emailUser
                tvPasswordHistory.text = users.password

                Glide.with(itemView.context)
                    .load(FILE_PATH + users.photoUser)
                    .into(civImageHistory)

                btnEditUser.setOnClickListener {
                    onItemClickCallback.onItemClicked(users)
                }
                btnDeleteUser.setOnClickListener {
                    UsersActivity().deleteUser(itemView.context, tvIdUser.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserHolder {
        val viewHolder = UserRowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ListUserHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount() = listUser.size
}