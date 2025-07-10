package com.example.projectdb

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectdb.databinding.TaskFragBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class TasksFragment : Fragment() {
    private var _binding: TaskFragBinding? = null
    private val binding get() = _binding!!
    private var c1: Int = 0
    private var c2: Int = 0
    private var c3: Int = 0
    private var vc = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = TaskFragBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TasksViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TaskItemAdapter(
            updateTaskDone = { task -> viewModel.updateTask(task) },
            deleteTask = { task -> viewModel.deleteTask(task) }
        )

        binding.tasksList.adapter = adapter
        binding.saveButton.setOnClickListener {
            if (binding.taskName.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Task failed to add because empty edit text ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Task added " + binding.taskName.text,
                    Toast.LENGTH_SHORT
                )
                    .show()
                viewModel.addTask()
            }
        }
        binding.viewInfo.setOnClickListener {

            lifecycleScope.launch {

                c1 = viewModel.getnboftasks()
                c2 = viewModel.getnbofdonetasks()
                c3 = viewModel.getnbofundonetasks()

                val action =
                    TasksFragmentDirections.actionTasksFragmentToAnalyzeFragment(c1, c2, c3)
                findNavController().navigate(action)

            }
        }


        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        // Get current date in milliseconds
        val currentDate = System.currentTimeMillis()

        // Initialize the calendar view to show today's date
        binding.calendarView.date = currentDate // Set the current date
        binding.calendarView.minDate =
            currentDate // Set the minimum date to today (no future dates allowed)

        // Set up listener for date changes
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a Calendar instance and set the selected date
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            // Get the selected date in milliseconds
            val selectedDate = calendar.timeInMillis

            // Pass the selected date to the ViewModel
            viewModel.newTaskDate.value = selectedDate
        }

        // Set up the filter button to show the DatePickerDialog
        binding.filter.setOnClickListener {
            showDatePickerDialog(viewModel)
        }

        return view
    }

    // Function to show the DatePickerDialog
    private fun showDatePickerDialog(viewModel: TasksViewModel) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.timeInMillis

                // Pass the selected date to the ViewModel
                viewModel.newTaskDate.value = selectedDate
                viewModel.filterTasksByDate(selectedDate)
            },
            year, month, day
        )


        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}