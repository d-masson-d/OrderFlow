package com.example.orderflow.Data
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import java.io.ByteArrayOutputStream

data class Order(
    val id: Int,
    var customerName: String,
    var orderDate: String,
    var furnitureType: String,
    var model: String,
    var quantity: Int,
    var status: String,
    var imageResourceId: ByteArray? // Изменено на ByteArray
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createByteArray() // Чтение массива байтов
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(customerName)
        parcel.writeString(orderDate)
        parcel.writeString(furnitureType)
        parcel.writeString(model)
        parcel.writeInt(quantity)
        parcel.writeString(status)
        parcel.writeByteArray(imageResourceId) // Запись массива байтов
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
