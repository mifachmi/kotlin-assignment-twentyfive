package com.example.kotlin_assignment_eighteen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_assignment_eighteen.databinding.UserRowListBinding
import com.example.kotlin_assignment_eighteen.model.DataItemUser

class ListUserAdapter(private val listUser: List<DataItemUser>) : RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {

    inner class ListUserHolder(private val binding: UserRowListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: DataItemUser) {
            with(binding){
                tvIdUser.text = users.id
                tvFullNameHistory.text = users.nameUser
                tvEmailHistory.text = users.emailUser
                tvPasswordHistory.text = users.password

                Glide.with(itemView.context)
                    .load("http://192.168.1.6/kotlin-assignment-nineteen/uploaded_image/${users.photoUser}")
                    .into(civImageHistory)

//                btnEdit.setOnClickListener {
//                    onItemClickCallback.onItemClicked(tasks)
//                }
//                btnDelete.setOnClickListener {
//                    MainActivity().deleteTask(itemView.context, tvIdTask.text.toString())
//                }
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