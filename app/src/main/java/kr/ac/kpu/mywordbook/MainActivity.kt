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

        val intent = Intent(this, myWordBookFragment::class.java)
        val intent2 = Intent(this,SharedFragment::class.java)

        val email = intent.getStringExtra("email")
        intent.putExtra("email",email)
        intent2.putExtra("email",email)



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