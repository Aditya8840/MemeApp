package com.codiyapa.myapplication

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.InputStream
import java.net.URL
import java.util.*


class MainActivity2 : AppCompatActivity() {
    var urls: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        loadMeme()
    }
    private fun loadMeme() {
        val imageView: ImageView = findViewById(R.id.memeimage)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    urls = response.getString("url")
                    Glide.with(this).load(urls).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity2, "something", Toast.LENGTH_SHORT).show()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    }).into(imageView)
                },
                { error ->
                    // TODO: Handle error
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                }
        )

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }
    fun share(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey!, check this funny meme from Meme app by CoDiYaPa $urls")
        val chooser = Intent.createChooser(intent, "Hey!")
        startActivity(chooser)
    }
    fun next(view: View) {
        loadMeme()
    }
    fun save(view: View){
        // Download Image from URL
        val input: InputStream = java.net.URL(urls).openStream()
        // Decode Bitmap
        val bitmap : Bitmap = BitmapFactory.decodeStream(input)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, bitmap)
        val chooser = Intent.createChooser(intent, "Hey!")
        startActivity(chooser)
    }

}