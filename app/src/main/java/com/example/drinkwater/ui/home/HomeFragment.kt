package com.example.drinkwater.ui.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.drinkwater.util.DataStoreProvider
import com.example.drinkwater.R
import com.example.drinkwater.databinding.FragmentHomeBinding
import com.example.drinkwater.ui.dashboard.SharedViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    lateinit var myDataStore: DataStoreProvider
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //share ViewModel between fragments by passing same Activity
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textGoal: TextView = binding.textGoal

        // Listen to changes in settings
        sharedViewModel.itemLiveData.observe(requireActivity(), { itemStr ->
            textGoal.text = itemStr
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataStore = DataStoreProvider(requireContext())

        val buttonIncrease: Button = binding.btnIncrease
        val buttonDecrease: Button = binding.btnDecrease
        val buttonAdd: Button = binding.btnAdd
        val buttonClear: Button = binding.btnClear

        val textAmount: TextView = binding.textAmount
        val textTotalAmount: TextView = binding.textTotalAmount
        val textDailyGoal: TextView = binding.textGoal
        val imgBottle: ImageView = binding.homeImg
        var amountTobeAdded = 0

        textDailyGoal.setText(myDataStore.getDailyGoal().toString() + " ml")
        textTotalAmount.setText(myDataStore.getTotalAmount().toString())

        updateImage(imgBottle)

        textTotalAmount.setText(myDataStore.getTotalAmount().toString())

        buttonIncrease.setOnClickListener {
            System.out.println("clicked button increase")
            val currentAmount = textAmount.text.toString().toInt()
            val newAmount = currentAmount + 50
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
            //val currentAmount = textTotalAmount.text.toString().toInt()

            val currentAmount = myDataStore.getTotalAmount()
            System.out.println("total amount before: " + currentAmount)

            val newAmount = currentAmount?.plus(amountTobeAdded)

            if (newAmount != null) {
                myDataStore.updateTotalAmount(newAmount)
            }

            textTotalAmount.setText(myDataStore.getTotalAmount().toString())

            updateImage(imgBottle)

            // if daily goal reached
            if (myDataStore.getTotalAmount()!! >= myDataStore.getDailyGoal()!!) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setTitle("Congratulations! \uD83C\uDF89 \uD83C\uDF88 \uD83E\uDD73")
                    .setMessage("You have reached your goal \uD83C\uDF1F")

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }

        buttonClear.setOnClickListener {
            System.out.println("clicked button clear")
            myDataStore.updateTotalAmount(0)
            textTotalAmount.setText(myDataStore.getTotalAmount().toString())
            updateImage(imgBottle)

        }


    }

    private fun updateImage(imgBottle: ImageView) {
        val currentTotal = myDataStore.getTotalAmount()
        val currentGoal = myDataStore.getDailyGoal()
        val result = currentTotal?.div(currentGoal!!)

        if (result != null) {
            if (result >= 1) {
                imgBottle.setImageResource(R.drawable.bottle_10)
            } else if (result >= (9 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_9)
            } else if (result >= (8 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_8)
            } else if (result >= (7 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_7)
            } else if (result >= (6 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_6)
            } else if (result >= (5 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_5)
            } else if (result >= (4 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_4)
            } else if (result >= (3 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_3)
            } else if (result >= (2 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_2)
            } else if (result >= (1 / 10.0)) {
                imgBottle.setImageResource(R.drawable.bottle_1)
            } else if (result == 0.0) {
                imgBottle.setImageResource(R.drawable.bottle_0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}