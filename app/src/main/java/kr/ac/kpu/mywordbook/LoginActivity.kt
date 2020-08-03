package kr.ac.kpu.mywordbook

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = this.getPreferences(0)
        val editor = pref.edit()

        ed_email.setText(pref.getString("idKey", ""))

        auth = Firebase.auth

        btn_signIn.setOnClickListener {
            val email = ed_email.text.toString()
            var nodotemail :String = email.replace("."," ")
            val password = ed_password.text.toString()

            for(i in 0 until email.length){

            }

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    baseContext, "이메일 또는 비밀번호가 입력되지 않았습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            editor.putString("idKey", ed_email.text.toString()).apply()
                            Toast.makeText(
                                baseContext, "로그인 되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("email",nodotemail)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        btn_signUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}