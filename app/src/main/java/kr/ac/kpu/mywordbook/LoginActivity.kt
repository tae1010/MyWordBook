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
/*
로그인 화면 구성
 */
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

            //.이 들어가면 데이터베이스에 저장이 되지 않으므로 .을 공백으로 변환
            var nodotemail :String = email.replace("."," ")
            val password = ed_password.text.toString()

            //만약 이메일이나 비밀번호가 입력되지 않았다면 토스트메세지 출력
            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    baseContext, "이메일 또는 비밀번호가 입력되지 않았습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                //아닐경우 로그인 완료
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            editor.putString("idKey", ed_email.text.toString()).apply()
                            Toast.makeText(
                                baseContext, "로그인 되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //val bundle = Bundle()
                            //bundle.putSerializable("email",nodotemail)
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
//회원가입 버튼을 누르면 회원가입창으로 이동
        btn_signUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}