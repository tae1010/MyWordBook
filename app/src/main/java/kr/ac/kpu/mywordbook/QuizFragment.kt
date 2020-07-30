package kr.ac.kpu.mywordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragquiz.*
import kr.ac.kpu.mywordbook.R

class QuizFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragquiz, container,false)

           Toast.makeText(activity,"3",Toast.LENGTH_SHORT).show()

        return view
    }
}