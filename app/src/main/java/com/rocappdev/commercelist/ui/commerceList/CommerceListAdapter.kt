package com.rocappdev.commercelist.ui.commerceList

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rocappdev.commercelist.R
import com.rocappdev.commercelist.databinding.RowCommerceItemBinding
import com.rocappdev.commercelist.domain.Categories
import com.rocappdev.commercelist.domain.Commerce
import kotlinx.android.synthetic.main.row_commerce_item.view.*
import java.util.*


class CommerceListAdapter(
    private val onClickListener: (Commerce) -> Unit
) : RecyclerView.Adapter<CommerceListAdapter.ViewHolder>() {

    private var items: List<Commerce> = listOf()
    private lateinit var context: Context
    private var mLastClickTime: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = RowCommerceItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commerce = items[position]
        commerce.name = commerce.name?.removeSuffix("*copy*")
        commerce.name = commerce.name?.removeSuffix("**copy antiguo**")
        commerce.name = commerce.name?.removeSuffix("*copy 1º integracion*")
        commerce.name = commerce.name?.substringAfterLast("*")
        commerce.name = commerce.name?.trim()

        if (!commerce.name.isNullOrEmpty() && commerce.name!!.length > 1)
            commerce.name =
                commerce.name?.substring(0, 1)?.toUpperCase(Locale.ROOT) + commerce.name?.substring(
                    1
                )
                    ?.toLowerCase(Locale.ROOT)

        holder.appBinding.commerce = commerce

        holder.itemView.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            onClickListener(items[position])
        }

        when (commerce.category) {
            Categories.FOOD.categoryName -> {
                holder.itemView.category_logo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_food
                    )
                )
            }
            Categories.BEAUTY.categoryName -> {
                holder.itemView.category_logo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_beauty
                    )
                )
            }
            Categories.SHOPPING.categoryName -> {
                holder.itemView.category_logo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_shopping
                    )
                )
            }
        }
    }

    fun setData(list: List<Commerce>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(val appBinding: RowCommerceItemBinding) :
        RecyclerView.ViewHolder(appBinding.root)
}