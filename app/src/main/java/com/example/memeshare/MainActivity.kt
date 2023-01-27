package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue.RequestEventListener
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentImageUrl: String

    /* https://github.com/D3vd/Meme_Api */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        loadMeme()

        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, currentImageUrl)
            val chooser = Intent.createChooser(intent, "Share this meme...")
            startActivity(chooser)
        }

        binding.nextBtn.setOnClickListener {
            loadMeme()
        }
    }

    private fun loadMeme() {

        binding.progressBar.visibility = View.VISIBLE

        val memeUrl = "https://meme-api.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, memeUrl, null,
            { response ->
                // request successfully
                currentImageUrl = response.getString("url")

                loadImage(currentImageUrl)
            },
            {
                // request failed
                Log.d("failed request", it.localizedMessage)
            })

        // Add the request to the RequestQueue.
        MemeSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun loadImage(urlJson: String) {
        val imageView = binding.memeIv

        Glide.with(this)
            .load(urlJson)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

            })
            .into(imageView)
    }
}