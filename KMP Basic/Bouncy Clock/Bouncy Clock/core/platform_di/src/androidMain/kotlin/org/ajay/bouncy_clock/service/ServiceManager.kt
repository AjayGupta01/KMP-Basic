package org.ajay.bouncy_clock.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class ServiceManager<T>(
    private val context: Context,
    private val serviceClass: Class<out Service>
) {
    var isBound by mutableStateOf(false)
    private var service: T? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as ServiceBinder<T>).getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    fun bindService() {
        val intent = Intent(context, serviceClass)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }

    fun getService(): T {
        return service!!
    }

    interface ServiceBinder<T> {
        fun getService(): T
    }
}

fun triggeredForegroundService(context: Context, serviceClass: Class<out Service>, action: String) {
    Intent(context, serviceClass).apply {
        this.action = action
        context.startForegroundService(this)
    }
}