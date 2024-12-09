package com.example.orderflow.UI
import FirestoreRepository
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.orderflow.Data.Order
import com.example.orderflow.R
import java.util.*

class EditOrderFragment : DialogFragment() {
    private lateinit var order: Order
    private lateinit var customerNameEditText: TextView
    private lateinit var orderDateTextView: TextView
    private lateinit var furnitureTypeEditText: TextView
    private lateinit var modelText: TextView
    private lateinit var quantityEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var firestoreRepository: FirestoreRepository

    private val statuses = arrayOf("в ожидании", "в производстве", "произведен", "в транспортировке", "доставлен", "завершен", "отменен")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerNameEditText = view.findViewById(R.id.customerNameEditText)
        orderDateTextView = view.findViewById(R.id.orderDateTextView)
        furnitureTypeEditText = view.findViewById(R.id.furnitureTypeEditText)
        modelText = view.findViewById(R.id.model)
        quantityEditText = view.findViewById(R.id.quantityEditText)
        statusSpinner = view.findViewById(R.id.statusSpinner)
        val saveButton: Button = view.findViewById(R.id.saveButton)

        order = arguments?.getParcelable("order") ?: throw IllegalArgumentException("Order must be provided")
        firestoreRepository = FirestoreRepository()

        customerNameEditText.setText(order.customerName)
        orderDateTextView.text = order.orderDate
        furnitureTypeEditText.setText(order.furnitureType)
        modelText.setText(order.model)
        quantityEditText.setText(order.quantity.toString())

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        val statusIndex = statuses.indexOf(order.status)
        if (statusIndex >= 0) {
            statusSpinner.setSelection(statusIndex)
        }

        orderDateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        saveButton.setOnClickListener {
            order.customerName = customerNameEditText.text.toString()
            order.orderDate = orderDateTextView.text.toString()
            order.furnitureType = furnitureTypeEditText.text.toString()
            order.model = modelText.text.toString()
            order.quantity = quantityEditText.text.toString().toLongOrNull() ?: 0
            order.status = statusSpinner.selectedItem.toString()


            firestoreRepository.updateOrder(order,
                onSuccess = { dismiss() },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), "Ошибка обновления заказа: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            orderDateTextView.text = formattedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    companion object {
        fun newInstance(order: Order): EditOrderFragment {
            val fragment = EditOrderFragment()
            val args = Bundle()
            args.putParcelable("order", order)
            fragment.arguments = args
            return fragment
        }
    }
}


