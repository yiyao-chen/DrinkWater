package com.example.drinkwater.ui.dashboard

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
import com.example.drinkwater.R
import com.example.drinkwater.databinding.FragmentDashboardBinding
import com.example.drinkwater.ui.home.NotificationWorker
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var notificationIntervalEditText: EditText

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textDashboard
        notificationIntervalEditText = binding.edittextNotificationInterval
        val saveButton: Button = binding.btnSave

        val text1: TextView = binding.text1
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            text1.text = it
        })

        // Load current interval value from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
        val currentInterval = sharedPreferences.getLong("notification_interval", 1L)
        print("current interval : " + currentInterval)
        notificationIntervalEditText.setText(currentInterval.toString())

        saveButton.setOnClickListener {
            val intervalText = notificationIntervalEditText.text.toString()
            val interval = intervalText.toLongOrNull()
            if (interval != null && interval > 0) {
                saveNotificationInterval(interval)

                // Reschedule the work with the new interval
                rescheduleNotificationWorker(interval)
            }
        }


        // spinner

        val spinner: Spinner = binding.intervalSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.intervals_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        spinner.onItemSelectedListener = this

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

    //
    // spinner responding user selections
    //
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        print("on item selected")
        print("selected item:")

        if (parent != null) {
            print(parent.getItemAtPosition(pos))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        print("onNothingSelected,,")
    }
}