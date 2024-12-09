import com.example.orderflow.Data.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository {
    private val db: FirebaseFirestore = Firebase.firestore


    fun loginUser (login: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        db.collection("users")
            .whereEqualTo("login", login)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onComplete(false, "Неверный логин или пароль")
                } else {
                    onComplete(true, null)
                }
            }
            .addOnFailureListener { onComplete(false, it.message) }
    }


    fun addOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("orders").add(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getOrders(onSuccess: (List<Order>) -> Unit, onFailure: (Exception) -> Unit): ListenerRegistration {
        return db.collection("orders")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onFailure(e)
                    return@addSnapshotListener
                }

                val orders = mutableListOf<Order>()
                snapshot?.documents?.forEach { document ->
                    val order = document.toObject(Order::class.java)
                    order?.let { orders.add(it) }
                }
                onSuccess(orders)
            }
    }

    fun updateOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("orders").document(order.id)
            .set(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}