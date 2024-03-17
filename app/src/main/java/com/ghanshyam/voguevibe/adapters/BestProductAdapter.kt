package com.ghanshyam.voguevibe.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghanshyam.voguevibe.data.Product
import com.ghanshyam.voguevibe.databinding.BestDealItemBinding
import com.ghanshyam.voguevibe.databinding.ProductItemBinding

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    inner class BestProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let {
                    val remainingPricePercentage = 1f - it
                    val priceAfterOffer = remainingPricePercentage * product.price
                    productNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    productOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                if (product.offerPercentage == null) {
                    productNewPrice.visibility = View.VISIBLE
                }

                productOldPrice.text = "$ ${product.price}"
                productName.text = product.name
            }

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bindProduct(product)
    }
}