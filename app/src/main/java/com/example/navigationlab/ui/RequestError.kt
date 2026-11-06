package com.example.navigationlab.ui

import retrofit2.HttpException
import java.io.IOException

internal fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> if (code() == 404) "Profil tidak ditemukan (HTTP 404)." else "Server mengembalikan HTTP ${code()}. Coba lagi."
    is IOException -> "Tidak dapat terhubung. Periksa koneksi internet, lalu coba lagi."
    else -> "Gagal membaca data profil. Silakan coba lagi."
}
