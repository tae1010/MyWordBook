package kr.ac.kpu.mywordbook

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
/*
리스트뷰에 단어장이름과 날짜를 띄워줌
 */
class WordBookAdapter : BaseAdapter() {

    private var wordbookList = ArrayList<ListWordBook>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.wordbook_list, parent, false)
        }

        val titleText = view!!.findViewById(R.id.list_title) as TextView
        val dateText = view!!.findViewById(R.id.list_date) as TextView

        val wordbookListItem = wordbookList[position]

        titleText.setTextColor(Color.WHITE)
        dateText.setTextColor(Color.GRAY)

        titleText.setText(wordbookListItem.title)
        dateText.setText(wordbookListItem.date)

        return view

    }

    override fun getCount(): Int {
        return wordbookList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return wordbookList[position]
    }
    fun addItem(title: String,date: String) {
        val item = ListWordBook(title,date)

        item.title = title
        item.date = date

        wordbookList.add(item)
    }
}