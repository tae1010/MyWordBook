package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragmywordbook.*

class myWordBookFragment : Fragment() {

    var wbList = arrayListOf<ListWordBook>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmywordbook, container,false)

        setHasOptionsMenu(true)


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu,inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newmemo -> {
                val dlgView = layoutInflater.inflate(R.layout.wordbookname, null)
                val addwordbook : EditText = dlgView.findViewById(R.id.ed_name)
                val dlgBuilder = AlertDialog.Builder(activity)
                dlgBuilder.setView(dlgView)

                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->

                    val listview : ListView
                    val adapter : WordBookAdapter

                    adapter = WordBookAdapter()

                    listview = view!!.findViewById(R.id.lv_wordbook) as ListView
                    listview.adapter = adapter

                    wbList.add(ListWordBook("${addwordbook.text}","2020-07-16"))

                    for(i in 0 until wbList.size){
                        adapter.addItem("${wbList[i].title}","2020-07-16")
                    }






                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
        return false
    }
}