package com.mtgofa.eservice

import android.app.Application
import com.mtgofa.eservice.data.Session

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Session.init(this)
    }
}
