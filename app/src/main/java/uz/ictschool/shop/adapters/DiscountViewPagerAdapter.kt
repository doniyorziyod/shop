package uz.ictschool.shop.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import uz.ictschool.shop.R
import uz.ictschool.shop.models.Product

class DiscountViewPagerAdapter(val products:List<Product>, private val context: Context, val discountClicked: DiscountClicked) : RecyclerView.Adapter<DiscountViewPagerAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ShapeableImageView = itemView.findViewById(R.id.viewpage_img)
        var title: TextView = itemView.findViewById(R.id.viewpage_title)
        var skidka: TextView = itemView.findViewById(R.id.viewpage_skidka)
        var constraint : ConstraintLayout = itemView.findViewById(R.id.constraint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var product = products[position]
        holder.img.load(product.images[0])
        holder.title.text = product.title
        holder.skidka.text = product.discountPercentage.toString() + "%"
        holder.constraint.animation = AnimationUtils.loadAnimation(context, R.anim.discount_anim)
        holder.itemView.setOnClickListener {
            discountClicked.onClicked(product)
        }
    }

    interface DiscountClicked {
        fun onClicked(product: Product)
    }
}