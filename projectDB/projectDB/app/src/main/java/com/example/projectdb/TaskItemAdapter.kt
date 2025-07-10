package com.example.projectdb
import android.app.AlertDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class TaskItemAdapter(
    private val updateTaskDone: (Task) -> Unit,
    private val deleteTask: (Task) -> Unit // Add a delete callback
) : RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder>() {
    var data = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        return TaskItemViewHolder.inflateFrom(parent, updateTaskDone, deleteTask)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class TaskItemViewHolder(
        private val rootView: CardView,
        private val updateTaskDone: (Task) -> Unit,
        private val deleteTask: (Task) -> Unit
    ) : RecyclerView.ViewHolder(rootView) {

        private val taskName: TextView = rootView.findViewById(R.id.task_name)
        private val taskDone: CheckBox = rootView.findViewById(R.id.task_done)
        private val taskDate: TextView = rootView.findViewById(R.id.task_date)

        companion object {
            fun inflateFrom(
                parent: ViewGroup,
                updateTaskDone: (Task) -> Unit,
                deleteTask: (Task) -> Unit
            ): TaskItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.task_item, parent, false) as CardView
                return TaskItemViewHolder(view, updateTaskDone, deleteTask)
            }
        }

        fun bind(item: Task) {
            taskName.text = item.taskName
            taskDone.isChecked = item.taskDone


            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = if (item.taskDate > 0) {
                dateFormat.format(Date(item.taskDate))
            } else {
                "No Date"
            }
            taskDate.text = formattedDate

            taskDone.setOnCheckedChangeListener { _, isChecked ->
                item.taskDone = isChecked
                updateTaskDone(item)
            }


            rootView.setOnLongClickListener {

                AlertDialog.Builder(rootView.context)
                    .setTitle("Delete Task")
                    .setMessage("Do you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        deleteTask(item)  // Delete the task
                    }
                    .setNegativeButton("No", null)
                    .show()

                true
            }
        }
    }
}