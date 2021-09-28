package com.example.kotlin_assignment_eighteen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_assignment_eighteen.activity.MainActivity
import com.example.kotlin_assignment_eighteen.databinding.TaskRowListBinding
import com.example.kotlin_assignment_eighteen.model.DataItem

class TaskAdapter(private val dataAllTask: List<DataItem>) : RecyclerView.Adapter<TaskAdapter.ListTaskHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListTaskHolder(private val binding: TaskRowListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tasks: DataItem) {
            with(binding){
                tvIdTask.text = tasks.id
                tvNamaTask.text = tasks.taskName
                tvDueDate.text = tasks.taskDate

                btnEdit.setOnClickListener {
                    onItemClickCallback.onItemClicked(tasks)
                }
                btnDelete.setOnClickListener {
                    MainActivity().deleteTask(itemView.context, tvIdTask.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTaskHolder {
        val viewHolder = TaskRowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListTaskHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ListTaskHolder, position: Int) {
        holder.bind(dataAllTask[position])
    }

    override fun getItemCount() = dataAllTask.size

}