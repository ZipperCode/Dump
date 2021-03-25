package com.zipper.dump.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.zipper.dump.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val cardView = findViewById<CardView>(R.id.test_card)
        val image = findViewById<ImageView>(R.id.test_img)
        val title = findViewById<TextView>(R.id.text_title)
        cardView.setOnClickListener{
            cardView.setCardBackgroundColor(resources.getColor(R.color.card_unable_color))
            image.setImageResource(R.drawable.ic_baseline_cancel_64)
            title.text =resources.getText( R.string.service_stopped)
        }

    }
}