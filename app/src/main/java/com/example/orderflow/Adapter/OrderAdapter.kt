package com.example.orderflow.Adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderflow.Data.Order
import com.example.orderflow.R
import com.journeyapps.barcodescanner.BarcodeEncoder

class OrderAdapter(
    private var orders: List<Order>,
    private val onEditClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerName: TextView = itemView.findViewById(R.id.customerNameTextView)
        val orderDate: TextView = itemView.findViewById(R.id.orderDateTextView)
        val furnitureType: TextView = itemView.findViewById(R.id.furnitureTypeTextView)
        val model: TextView = itemView.findViewById(R.id.model)
        val quantity: TextView = itemView.findViewById(R.id.quantityTextView)
        val status: TextView = itemView.findViewById(R.id.statusTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val image: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.customerName.text = order.customerName
        holder.orderDate.text = order.orderDate
        holder.furnitureType.text = order.furnitureType
        holder.model.text = order.model
        holder.quantity.text = order.quantity.toString()
        holder.status.text = order.status


        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(order.imageResourceId, com.google.zxing.BarcodeFormat.CODE_128, 400, 200)
            holder.image.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.editButton.setOnClickListener {
            onEditClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
