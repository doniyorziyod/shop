package uz.ictschool.shop.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import uz.ictschool.shop.R
import uz.ictschool.shop.databinding.FragmentHomeBinding
import java.util.Locale

class CategoryAdapter(private var categories:List<String>,
                      val context: Context,
                      private val categoryPressed: CategoryClicked) : RecyclerView.Adapter<CategoryAdapter.MyHolder>() {
    var current = 0
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.category_text)
        val card : CardView = itemView.findViewById(R.id.category_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return categories.size + 1
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if (position == 0) {
            holder.title.text = "All Products"
        } else {
            val category = categories[position-1]
            category.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }.also { holder.title.text = it }
        }
        holder.card.animation = AnimationUtils.loadAnimation(context, R.anim.categories_anim)
        if (current == position) {
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.white))
            holder.title.setTextColor(Color.BLACK)
        } else {
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.click_blue))
            holder.title.setTextColor(Color.WHITE)
        }
        holder.card.setOnClickListener {
            if (position != current) {
                notifyItemChanged(current)
                current = position
                notifyItemChanged(current)
                if (position == 0) categoryPressed.onClicked("")
                else categoryPressed.onClicked(categories[position - 1])
            }
        }
    }

    interface CategoryClicked {
        fun onClicked(category: String)
    }
}