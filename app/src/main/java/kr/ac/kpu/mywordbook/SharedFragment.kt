package kr.ac.kpu.mywordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragshared.*
import kr.ac.kpu.mywordbook.R

class SharedFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragshared, container,false)


            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show()


        return view
    }
}