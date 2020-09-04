package ie.wit.seatingplan.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ie.wit.seatingplan.R
import ie.wit.seatingplan.signUp_Login.LoginActivity


class Splash : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)


        Handler().postDelayed({

            startActivity(Intent(this,LoginActivity::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }
}