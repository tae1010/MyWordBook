package kr.ac.kpu.mywordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragsearch.*


class SearchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragsearch, container,false)


        val myWebView: WebView = view.findViewById<View>(R.id.webView) as WebView
        myWebView.loadUrl("https://dict.naver.com")


        return view
    }
}
