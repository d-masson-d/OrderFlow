package com.example.orderflow

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import com.example.orderflow.Adapter.OrderAdapter
import com.example.orderflow.Data.Order
import com.example.orderflow.UI.EditOrderFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream

class WorkScreen : AppCompatActivity() {
    private lateinit var orderList: MutableList<Order>
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var searchEditText: EditText
    private lateinit var button_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_screen)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        button_back = findViewById(R.id.button_back)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val barcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap =
            barcodeEncoder.encodeBitmap("123456789", BarcodeFormat.CODE_128, 500, 200)

        orderList = mutableListOf(
            Order(1, "Иван Иванов", "2023-10-01", "Стол", "2323sdsszx", 2, "в ожидании",
                bitmapToByteArray(bitmap)
            ),
            Order(2, "Петр Петров", "2023-10-02", "Стул", "343dffds", 5, "в производстве",
                bitmapToByteArray(bitmap)
            ),
            Order(3, "Иван Иванов", "2023-10-01", "Стол", "2323sdsszx", 2, "в ожидании",
            bitmapToByteArray(bitmap)
            ),
            Order(4, "Петр Петров", "2023-10-02", "Стул", "343dffds", 5, "в производстве",
            bitmapToByteArray(bitmap)
            ),
            Order(5, "Петр Петров", "2023-10-02", "Стул", "343dffds", 5, "в производстве",
                bitmapToByteArray(bitmap)
            ),
            Order(6, "Иван Иванов", "2023-10-01", "Стол", "2323sdsszx", 2, "в ожидании",
                bitmapToByteArray(bitmap)
            )
        )

        orderAdapter = OrderAdapter(orderList) { order -> editOrder(order) }
        recyclerView.adapter = orderAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterOrders(s.toString())
            }
        })

        button_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterOrders(query: String) {
        val filteredList = orderList.filter {
            it.customerName.contains(query, ignoreCase = true) ||
                    it.furnitureType.contains(query, ignoreCase = true)
        }
        orderAdapter.updateList(filteredList)
    }

    private fun editOrder(order: Order) {
        val editOrderFragment = EditOrderFragment.newInstance(order)
        editOrderFragment.show(supportFragmentManager, "EditOrderFragment")
    }
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
