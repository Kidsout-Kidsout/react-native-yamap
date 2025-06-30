package ru.vvdev.yamap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Field
import java.net.URL
import java.net.URLConnection

object ImageLoader {

  private fun getResId(resName: String, c: Class<*>): Int {
    return try {
      val idField: Field = c.getDeclaredField(resName)
      idField.getInt(idField)
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  @Throws(IOException::class)
  private fun getBitmap(context: Context, url: String): Bitmap? {
    return if (url.contains("http://") || url.contains("https://")) {
      val aURL = URL(url)
      val conn: URLConnection = aURL.openConnection()
      conn.connect()
      val istr = conn.getInputStream()
      val bis = BufferedInputStream(istr)
      val bitmap = BitmapFactory.decodeStream(bis)
      bis.close()
      istr.close()
      bitmap
    } else {
      val id = context.resources.getIdentifier(url, "drawable", context.packageName)
      BitmapFactory.decodeResource(context.resources, id)
    }
  }

  fun downloadImageBitmap(context: Context, url: String, cb: (Bitmap?) -> Unit) {
    Thread {
      try {
        val bitmap = getBitmap(context, url)
        if (bitmap != null) {
          Handler(Looper.getMainLooper()).post {
            cb(bitmap)
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }.start()
  }
}
