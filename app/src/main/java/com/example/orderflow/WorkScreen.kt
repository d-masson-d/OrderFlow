package com.example.orderflow

import FirestoreRepository
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.orderflow.Adapter.OrderAdapter
import com.example.orderflow.Data.Order
import com.example.orderflow.UI.EditOrderFragment
import com.google.firebase.firestore.ListenerRegistration


class WorkScreen : AppCompatActivity() {
    private lateinit var orderList: MutableList<Order>
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var searchEditText: EditText
    private lateinit var buttonBack: ImageButton
    private lateinit var firestoreRepository: FirestoreRepository
    private var orderListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_screen)


        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        buttonBack = findViewById(R.id.button_back)


        recyclerView.layoutManager = LinearLayoutManager(this)
        orderList = mutableListOf()
        orderAdapter = OrderAdapter(orderList) { order -> editOrder(order) }
        recyclerView.adapter = orderAdapter

        firestoreRepository = FirestoreRepository()


        loadOrders()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterOrders(s.toString())
            }
        })

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun loadOrders() {
        orderListener = firestoreRepository.getOrders(
            onSuccess = { orders ->
                orderList.clear()
                orderList.addAll(orders)
                orderAdapter.notifyDataSetChanged()
            },
            onFailure = { exception ->
                Log.e("WorkScreen", "Ошибка загрузки заказов: ${exception.message}")
                Toast.makeText(this, "Ошибка загрузки заказов", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun editOrder(order: Order) {
        val editOrderFragment = EditOrderFragment.newInstance(order)
        editOrderFragment.show(supportFragmentManager, "EditOrderFragment")
    }

    private fun filterOrders(query: String) {
        val filteredList = orderList.filter {
            it.customerName.contains(query, ignoreCase = true) ||
                    it.furnitureType.contains(query, ignoreCase = true)
        }
        orderAdapter.updateList(filteredList)
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListener?.remove()
    }
}



