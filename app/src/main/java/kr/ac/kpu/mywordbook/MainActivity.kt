package kr.ac.kpu.mywordbook


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_wordbook.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFrag(0)

        im_myWord.setOnClickListener {
            setFrag(0)
        }

        im_sharedWord.setOnClickListener {
            setFrag(1)
        }

        im_quiz.setOnClickListener {
            setFrag(2)
        }

        im_search.setOnClickListener {
            setFrag(3)
        }

        button.setOnClickListener {
            val dlgView = layoutInflater.inflate(R.layout.add_wordbook, null)
            val btn_picture : Button = dlgView.findViewById(R.id.add_picture)
            val btn_text : Button = dlgView.findViewById(R.id.add_text)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgView)
            dlgBuilder.show()

            btn_picture.setOnClickListener {
                val intent = Intent(this,PictureTextActivity::class.java)
                startActivity(intent)
            }
            btn_text.setOnClickListener {


                val dlg = layoutInflater.inflate(R.layout.input_wordbook, null)
                val dlgBuilder = AlertDialog.Builder(this)
                dlgBuilder.setView(dlg)
                val etWord = dlgView.findViewById<EditText>(R.id.edit_word)
                val etMeaning = dlgView.findViewById<EditText>(R.id.edit_meaning)

                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->
                }.show()
            }
        }
    }

    private fun setFrag(fregNum : Int){
        val ft = supportFragmentManager.beginTransaction()
        when(fregNum)
        {
            0 ->{
                ft.replace(R.id.mainFrame,myWordBookFragment()).commit()
            }
            1 ->{
                ft.replace(R.id.mainFrame, SharedFragment()).commit()
            }
            2 ->{
                ft.replace(R.id.mainFrame, QuizFragment()).commit()
            }
            3 ->{
                ft.replace(R.id.mainFrame, SearchFragment()).commit()
            }

        }
    }
}