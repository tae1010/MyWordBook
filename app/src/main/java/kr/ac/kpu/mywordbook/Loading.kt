package kr.ac.kpu.mywordbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity


class Loadding : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
        setContentView(R.layout.loading)

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }





    override fun onBackPressed() {

    }
}