package uz.ictschool.shop.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import uz.ictschool.shop.R
import uz.ictschool.shop.models.ProductNumber

class  CartAdapter(private val products : List<ProductNumber>, val context: Context) : RecyclerView.Adapter<CartAdapter.MyHandler>() {
    inner class MyHandler(itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.cart_item_title)
        val price : TextView = itemView.findViewById(R.id.cart_item_price)
        val quantity : TextView = itemView.findViewById(R.id.cart_item_quantity)
        val totalPrice : TextView = itemView.findViewById(R.id.cart_item_total_price)
        val card : CardView = itemView.findViewById(R.id.cart_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHandler {
        return MyHandler(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHandler, position: Int) {
        val product = products[position]
        holder.title.text = product.title
        holder.price.text =  "$" + product.price.toString()
        holder.quantity.text = product.quantity.toString()
        holder.totalPrice.text = "$" + product.total.toString()
        holder.card.animation = AnimationUtils.loadAnimation(context, R.anim.products_anim)
    }
}
