package com.assignment.extension

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder


object G {
    val gson = GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()
}

inline fun <reified T : Any> String?.fromJson() = G.gson.fromJson<T>(this, T::class.java)


inline fun <reified T : Any> AppCompatActivity.extraSerializable(key: String, default: T) = lazy {
    val value = intent?.extras?.getSerializable(key)
    if (value is T) value else default
}

inline fun <reified T : AppCompatActivity> Context.startActivity(body: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.body()
    ContextCompat.startActivity(this, intent, null)
}