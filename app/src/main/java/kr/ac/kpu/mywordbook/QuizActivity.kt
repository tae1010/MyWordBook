package kr.ac.kpu.mywordbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_quiz.*
/*
단어장안에 있는 단어를 이용하여 퀴즈를 내주는 화면
 */
class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val database = Firebase.database

        var wList=arrayListOf<ListWord>()
        var krList = arrayListOf<String>()    //모든 뜻을 저장
        var answerList = arrayListOf<String>()    //버튼에 들어갈 4개의 뜻

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val email = intent.getStringExtra("email")
        var count: Int = 0  //문제 갯수
        val myRef1 = database.getReference("users/$email/$date/$title")
        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            fun setTest(count:Int, list:ArrayList<String>){


            }
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var answer: String? =null
                for (snapshot in p0.children) {
                    wList.add(
                        ListWord(
                            "${snapshot.key.toString()}",
                            "${snapshot.value.toString()}"
                        )
                    )
                }
                for (i in 0 until wList.size) {
                    krList.add(wList[i].krWord.toString())
                }// 영단어중 뜻만 모여있는 arrayList
                wList.shuffle()
                krList.shuffle()
                answerList.add(wList[count].krWord.toString())
                answerList.add(krList[0])
                answerList.add(krList[1])
                answerList.add(krList[2])
                answerList.shuffle()

                textWord.text=wList[count].egWord.toString()
                answer=wList[count].krWord.toString()
                button1.text=answerList[0]
                button2.text=answerList[1]
                button3.text=answerList[2]
                button4.text=answerList[3]

                button1.setOnClickListener {
                    if(answer==button1.text){
                        textWord.text="정답"
                        answerList.clear()
                    }
                }
                button2.setOnClickListener {
                    if(answer==button2.text){
                        textWord.text="정답"
                        answerList.clear()
                    }
                }
                button3.setOnClickListener {
                    if(answer==button3.text){
                        textWord.text="정답"
                        answerList.clear()
                    }
                }
                button4.setOnClickListener {
                    if(answer==button4.text){
                        textWord.text="정답"
                        answerList.clear()
                    }
                }
                btnNext.setOnClickListener {
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                wList.clear()
            }//ondataChange 끝
        })




    }  //onCreate method

}
