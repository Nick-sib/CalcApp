package com.nickolay.calcapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.about_dialog.*


class AboutDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_dialog)

        setTitle("About")

        bGotIt.setOnClickListener{
            dismiss()
        }

        bHH.setOnClickListener{
            openBrouser()
        }
    }

    private fun openBrouser(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://novosibirsk.hh.ru/resume/77fd453bff0228ed150039ed1f6453354a3273"))
        startActivity(context, browserIntent, null)
    }
}