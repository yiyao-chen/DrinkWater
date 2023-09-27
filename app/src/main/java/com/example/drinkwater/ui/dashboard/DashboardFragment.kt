package com.example.drinkwater.ui.dashboard

import android.widget.ArrayAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.drinkwater.DataStoreProvider
import com.example.drinkwater.R
import com.example.drinkwater.databinding.FragmentDashboardBinding
import com.example.drinkwater.ui.home.NotificationWorker
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment() {
    lateinit var myDataStore: DataStoreProvider

    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var spinner: Spinner
    private lateinit var selectedItemText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Fragment之间通过传入同一个Activity来共享ViewModel
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myDataStore = DataStoreProvider(requireContext())

        //val textView: TextView = binding.textDashboard
        //notificationIntervalEditText = binding.edittextNotificationInterval
        val btnSaveDailyGoal: Button = binding.btnSaveDailyGoal
        val textDailyGoal: EditText = binding.textDailyGoal
        val text1: TextView = binding.text1
        val spinner = binding.spinner
        val selectedItemText = binding.selectedItemText

        sharedViewModel.text.observe(viewLifecycleOwner, Observer {
            text1.text = it
        })

        // Load current interval value from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
        btnSaveDailyGoal.setOnClickListener {
            val goal = textDailyGoal.text.toString()
            sharedViewModel.changeData(goal)
            myDataStore.updateDailyGoal(goal.toDouble())

            Toast.makeText(requireContext(), "Daily goal set", Toast.LENGTH_LONG).show()
        }


        // spinner
        // Create an ArrayAdapter for the spinner
        val items = arrayOf(1, 2, 3, 4)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter for the spinner
        spinner.adapter = adapter

        // Set an item selected listener for the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selectedItemText.text = "Selected interval: $selectedItem hour(s)"
                Toast.makeText(requireContext(), "Set interval to $selectedItem hour(s)", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing here
            }
        }


        return root
    }

    private fun saveNotificationInterval(interval: Long) {
        val sharedPreferences = requireContext().getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
        print("saved interval: " + interval)
        sharedPreferences.edit().putLong("notification_interval", interval).apply()
    }

    private fun rescheduleNotificationWorker(interval: Long) {
        val workManager = WorkManager.getInstance(requireContext())
        workManager.cancelAllWorkByTag("notification_periodic_worker")

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = interval,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).addTag("notification_periodic_worker")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "notification_periodic_worker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}