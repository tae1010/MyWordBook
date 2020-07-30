package kr.ac.kpu.mywordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragsearch.*
import kr.ac.kpu.mywordbook.R

class SearchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragsearch, container,false)

            Toast.makeText(activity, "4", Toast.LENGTH_SHORT).show()


        return view
    }
}
