package kr.ac.kpu.mywordbook


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_wordbook.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val dlgView = layoutInflater.inflate(R.layout.add_wordbook, null)
            val btn_picture : Button = dlgView.findViewById(R.id.add_picture)
            val btn_text : Button = dlgView.findViewById(R.id.add_text)
            val dlgBuilder = AlertDialog.Builder(this)

            btn_picture.setOnClickListener {
                val intent = Intent(this,PictureTextActivity::class.java)
                startActivity(intent)
            }
            btn_text.setOnClickListener {

            }

            dlgBuilder.setView(dlgView)
            dlgBuilder.show()

        }

    }
}