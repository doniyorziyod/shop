package uz.ictschool.shop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.ictschool.shop.R
import uz.ictschool.shop.models.Product
import kotlin.math.roundToInt

class ProductAdapter(var products:List<Product>, private val context: Context, val productClicked: ProductClicked) : RecyclerView.Adapter<ProductAdapter.MyHolder>() {
    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.product_item_image)
        val title : TextView = itemView.findViewById(R.id.product_item_title)
        val price : TextView = itemView.findViewById(R.id.product_item_price)
        val rating : TextView = itemView.findViewById(R.id.product_item_rating)
        val card : CardView = itemView.findViewById(R.id.card_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = products[position]
        holder.image.load(product.images[0])
        holder.rating.text = ((product.rating*10).roundToInt().toDouble()/10).toString()
        holder.title.text = product.title
        holder.price.text = product.price.toString() + " $"
        holder.itemView.setOnClickListener {
            productClicked.onClicked(product)
        }
        holder.card.animation = AnimationUtils.loadAnimation(context, R.anim.products_anim)
    }
    interface ProductClicked {
        fun onClicked(product: Product)
    }
}
