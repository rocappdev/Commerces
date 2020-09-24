package com.rocappdev.commercelist.ui.commerceDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rocappdev.commercelist.R
import com.rocappdev.commercelist.databinding.ActivityCommerceDetailBinding
import com.rocappdev.commercelist.domain.Categories
import com.rocappdev.commercelist.domain.Commerce
import com.rocappdev.commercelist.ui.commerceList.CommerceListActivity
import com.rocappdev.commercelist.util.loadImage
import kotlinx.android.synthetic.main.activity_commerce_detail.*

class CommerceDetailActivity : AppCompatActivity() {
    private lateinit var selectedCommerce: Commerce
    private lateinit var binding: ActivityCommerceDetailBinding
    private var categoryIcon: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            selectedCommerce =
                intent.getSerializableExtra(CommerceListActivity.SELECTED_COMMERCE) as Commerce
        }
        setViews()
    }

    private fun setViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_commerce_detail)
        binding.selectedCommerce = selectedCommerce

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = selectedCommerce.name

        selectedCommerce.logo?.url?.let { imageUrl ->
            loadImage(logo, imageUrl)
        }

        phone.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone.text.toString(), null))
            startActivity(intent)
        }

        email.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email.text.toString(), null
                )
            )
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }

        web.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(web.text.toString())
            startActivity(i)
        }

        facebook.setOnClickListener {
            openSocialIntent(selectedCommerce.social?.facebook.toString())
        }
        instagram.setOnClickListener {
            openSocialIntent(selectedCommerce.social?.instagram.toString())
        }
        twitter.setOnClickListener {
            openSocialIntent(selectedCommerce.social?.twitter.toString())
        }

        when (selectedCommerce.category) {
            Categories.FOOD.categoryName -> {
                categoryIcon = R.drawable.ic_food
            }
            Categories.BEAUTY.categoryName -> {
                categoryIcon = R.drawable.ic_beauty
            }
            Categories.SHOPPING.categoryName -> {
                categoryIcon = R.drawable.ic_shopping
            }
        }
        category.setCompoundDrawablesWithIntrinsicBounds(0, 0, categoryIcon, 0)

        val fullAddress: String =
            selectedCommerce.address?.street + ". " + selectedCommerce.address?.zip + ", " + selectedCommerce.address?.city + ". " + selectedCommerce.address?.country
        address.text = fullAddress.trim()

        setFeatures(selectedCommerce.features, features)
    }


    private fun openSocialIntent(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFeatures(features: List<String>?, view: ChipGroup) {
        features?.forEach { feature ->
            val mChip =
                this.layoutInflater.inflate(R.layout.item_chip_feature, null, false) as Chip
            mChip.text = feature
            view.addView(mChip)
        }
    }
}