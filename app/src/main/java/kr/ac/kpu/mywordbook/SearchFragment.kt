package kr.ac.kpu.mywordbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragsearch.*
/*
webview 액티비티를 인텐트
 */

class SearchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragsearch, container,false)


        val intent = Intent(activity,WebViewActivity::class.java)
        startActivity(intent)

        return view
    }
}
