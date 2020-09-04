package ie.wit.seatingplan.signUp_Login


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import ie.wit.seatingplan.R
import ie.wit.seatingplan.activities.Home
import ie.wit.seatingplan.main.MainActivity
import ie.wit.seatingplan.models.UserModel
import kotlinx.android.synthetic.main.register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.HashMap
import java.util.regex.Pattern

class RegisterActivity: AppCompatActivity(), AnkoLogger {

    val planAuth = FirebaseAuth.getInstance()
    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val registerBtn = findViewById<View>(R.id.register_button)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivityForResult(intentFor<Home>(), 0)
        } else {
            signUp()
        }

        registerBtn.setOnClickListener {
            signUp()
        }

        loginBtn.setOnClickListener {
            startActivityForResult(intentFor<LoginActivity>(), 0)
        }
    }

    private fun signUp() {

        val emailText = findViewById<View>(R.id.email_text) as EditText
        val passwordText = findViewById<View>(R.id.password_text) as EditText

        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        fun checkPassword(): Boolean {
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(password)
            if (password.length < 8 && !matcher.matches()) {
                return false
            }

            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (!matcher.matches()) {
                return false
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (!matcher.matches()) {
                return false
            }
            return true
        }

        var user_id = username.text.toString()

        if (email.isNotEmpty() && checkPassword()) {
            planAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = planAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    toast("Verification Email sent")
                                    writeUserDetails(UserModel(username = user_id))
                                    startActivityForResult(intentFor<LoginActivity>(), 0)
                                }
                            }
                    }
                    else {
                        Toast.makeText(this, "Error. Please try again", Toast.LENGTH_LONG).show()
                    }
                })

        } else {
            toast("Please complete all fields correctly")
        }

    }

    private fun writeUserDetails(user: UserModel){
        val id = planAuth.currentUser!!.uid
        val key = app.database.child("User Details").push().key
        user.id = key
        val userValues = user.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/User Details/$id/$key"]=userValues
        app.database.updateChildren(childUpdates)
    }
}