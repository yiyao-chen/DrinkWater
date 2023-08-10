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
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.drinkwater.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private val TOTAL_AMOUNT = intPreferencesKey("total_amount")

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
        System.out.println("   onViewCreated")
        System.out.println("   readNonNullData:")

        System.out.println(readNonNullData(TOTAL_AMOUNT, 0) )

        val buttonIncrease: Button = binding.btnIncrease
        val buttonDecrease: Button = binding.btnDecrease
        val buttonAdd: Button = binding.btnAdd

        val textAmount: TextView = binding.textAmount
        val textTotalAmount: TextView = binding.textTotalAmount
        var amountTobeAdded = 0



        buttonIncrease.setOnClickListener {
            //Log.i("MY_TAG", "hello world");
            System.out.println("clicked button increase")
            val currentAmount = textAmount.text.toString().toInt()
            val newAmount = currentAmount + 50
            System.out.println("amount to be added" + newAmount)
            textAmount.setText(newAmount.toString())
            amountTobeAdded = newAmount
        }

        buttonDecrease.setOnClickListener {
            System.out.println("clicked button decrease")

            if (!textAmount.text.toString().equals("0")) {
                val currentAmount = textAmount.text.toString().toInt()
                val newAmount = currentAmount - 50
                textAmount.setText(newAmount.toString())
                amountTobeAdded = newAmount

            }
        }

        buttonAdd.setOnClickListener {
            System.out.println("clicked button add")
            val currentAmount = textTotalAmount.text.toString().toInt()
            System.out.println("total amount before: " + currentAmount)

            val newAmount = currentAmount + amountTobeAdded
            textTotalAmount.setText(newAmount.toString())
            System.out.println("total amount now " + textTotalAmount.text)
            //textAmount.setText("0")

            runBlocking {
                saveAmountToPreferencesStore(newAmount)
            }
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

    private suspend fun saveAmountToPreferencesStore(amount: Int) {
        System.out.println("saveAmountToPreferencesStore")

        requireContext().dataStore.edit { preferences ->
            preferences[TOTAL_AMOUNT] = amount
            System.out.println("preferences[TOTAL_AMOUNT]... " + preferences[TOTAL_AMOUNT])
        }
        System.out.println("readNonNullData... " )

        System.out.println(readNonNullData(TOTAL_AMOUNT, 0) )
    }


    private fun getIntSync(key: String, defValue: Int): Int =
        readNonNullData(intPreferencesKey(key), defValue)


    fun <T> readNonNullData(key: Preferences.Key<T>, defValue: T): T {

        return runBlocking {
            requireContext().dataStore.data.map {
                it[key] ?: defValue
            }.first()
        }
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