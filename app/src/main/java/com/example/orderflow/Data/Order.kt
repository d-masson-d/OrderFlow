package com.example.orderflow.Data
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Order(
    @DocumentId val id: String = "",
    var customerName: String = "",
    var orderDate: String = "",
    var furnitureType: String = "",
    var model: String = "",
    var quantity: Long = 0,
    var status: String = "",
    var imageResourceId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        customerName = parcel.readString() ?: "",
        orderDate = parcel.readString() ?: "",
        furnitureType = parcel.readString() ?: "",
        model = parcel.readString() ?: "",
        quantity = parcel.readLong(),
        status = parcel.readString() ?: "",
        imageResourceId = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(customerName)
        parcel.writeString(orderDate)
        parcel.writeString(furnitureType)
        parcel.writeString(model)
        parcel.writeLong(quantity)
        parcel.writeString(status)
        parcel.writeString(imageResourceId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order = Order(parcel)
        override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
    }
}