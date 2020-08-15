package kr.ac.kpu.mywordbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragquiz.*
import kr.ac.kpu.mywordbook.R
/*
내가 만든 단어장을 띄워줌
 */
class QuizFragment : Fragment() {

    val database= Firebase.database
    lateinit var listview : ListView
    lateinit var adapter: WordBookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragquiz, container,false)


        setHasOptionsMenu(true)


        listview = view!!.findViewById(R.id.lv_wordbook) as ListView


        registerForContextMenu(listview)


        //adapter.notifyDataSetChanged() //삭제하거나 추가할때 리스트뷰 갱신

        //Toast.makeText(activity,"$email",Toast.LENGTH_SHORT).show()
        val email = activity!!.intent.getStringExtra("email")
        val myRef1 = database.getReference("users/$email")

        val intent2 = Intent(activity, SharedFragment::class.java)
        intent2.putExtra("email2",email)
        //Toast.makeText(activity, "$email", Toast.LENGTH_SHORT).show()

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {
                    val myRef2 = database.getReference("users/$email/${snapshot.key}")
                    myRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            adapter = WordBookAdapter()
                            listview.adapter = adapter

                            for (snapshot in p0.children) {
                                wbList.add(ListWordBook("${snapshot.key.toString()}", "${myRef2.key.toString()}"))
                                //Toast.makeText(activity,"${snapshot.key.toString()}",Toast.LENGTH_SHORT).show()
                            }

                            for (i in 0 until wbList.size) {
                                adapter.addItem("${wbList[i].title}", "${wbList[i].date}")
                                //Log.d(ContentValues.TAG, "###############################")
                            }
                        }
                    })
                }
            }
        })
        listview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(activity, QuizActivity::class.java)
                intent.putExtra("email",email)
                intent.putExtra("title", wbList[position].title)
                intent.putExtra("date", wbList[position].date)
                startActivity(intent)
            }
        wbList.clear()
        return view
    }
}