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
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.ed_email
import kotlinx.android.synthetic.main.activity_sign_up.ed_password

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        btn_make.setOnClickListener {
            val email = ed_email.text.toString()
            val password = ed_password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this, "이메일 또는 비밀번호가 입력되지 않았습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(
                                baseContext, "회원가입을 완료했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
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
    }
}