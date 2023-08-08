package com.example.drinkwater.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.drinkwater.databinding.FragmentHomeBinding
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    companion object {
//        const val CHANNEL_ID = "MyAppChannel"
//        const val NOTIFICATION_ID = 101
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAmount
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonIncrease: Button = binding.btnIncrease
        val buttonDecrease: Button = binding.btnDecrease
        val buttonAdd: Button = binding.btnAdd

        val textAmount: TextView = binding.textAmount
        val textTotalAmount: TextView = binding.textTotalAmount
        var amountTobeAdded = 0

        buttonIncrease.setOnClickListener {
            //Log.i("MY_TAG", "hello world");
            System.out.println("clicked button")
            val currentAmount = textAmount.text.toString().toInt()
            System.out.println("amount now " + currentAmount)
            val newAmount = currentAmount + 50
            System.out.println("amount new " + newAmount)
            textAmount.setText(newAmount.toString())
            amountTobeAdded = newAmount
        }

        buttonDecrease.setOnClickListener {
            if (!textAmount.text.toString().equals("0")) {
                val currentAmount = textAmount.text.toString().toInt()
                val newAmount = currentAmount - 50
                textAmount.setText(newAmount.toString())
                amountTobeAdded = newAmount

            }
        }

        buttonAdd.setOnClickListener {
            val currentAmount = textTotalAmount.text.toString().toInt()
            val newAmount = currentAmount + amountTobeAdded
            System.out.println("total amount current " + textTotalAmount.text)
            textTotalAmount.setText(newAmount.toString())
            System.out.println("total amount now " + textTotalAmount.text)
            //textAmount.setText("0")

        }

        // Schedule periodic work using WorkManager
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 1, // Repeat every 1 hour
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueueUniquePeriodicWork(
            "my_periodic_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

//    private fun showNotification() {
//        val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "MyApp Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
//            .setContentTitle("MyApp Notification")
//            .setContentText("You clicked the button!")
//            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//            .build()
//
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}