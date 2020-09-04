package ie.wit.seatingplan.signUp_Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ie.wit.seatingplan.R
import ie.wit.seatingplan.activities.Home
import ie.wit.seatingplan.main.MainActivity
import kotlinx.android.synthetic.main.register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app: MainActivity
    private lateinit var planAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        planAuth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            verifyEmail()
        } else {
            login()
        }

        val loginBtn = findViewById<View>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            login()
        }

        register_button.setOnClickListener {
            startActivityForResult(intentFor<RegisterActivity>(), 0)
        }
    }

    private fun verifyEmail()
    {
        val user = planAuth.currentUser
        if(user!=null) {
            if (user.isEmailVerified) {

                startActivity(Intent(this, Home::class.java))
                toast("Log in success")
            } else {
                toast("email not verified")
                planAuth.signOut()
            }
        }
    }

    private fun login() {
        val emailText = findViewById<View>(R.id.emailText) as EditText
        val email = emailText.text.toString()
        val passwordText = findViewById<View>(R.id.passwordText) as EditText
        val password = passwordText.text.toString()

        planAuth.currentUser?.reload()

        if (email.isNotEmpty() || password.isNotEmpty()) {
            planAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        verifyEmail()
                    } else {
                        toast("Log in failed")
                    }

                }
        }
        else {
            toast("Enter email and password")
        }
    }
}