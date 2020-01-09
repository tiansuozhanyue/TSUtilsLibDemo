package com.example.tsutilslib

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.tslibrary.util.ToastUtil
import com.example.tslibrary.widget.SelfAdaptionGridView
import com.example.tslibrary.widget.VerticalScrollTestxView
import java.util.*


class MainActivity : AppCompatActivity(), SelfAdaptionGridView.CheckListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var selfAdaptionGridView = findViewById<SelfAdaptionGridView>(R.id.selfAdaptionGridView)
        var datas = ArrayList<String>()
        datas.add("技术好")
        datas.add("声音甜美")
        datas.add("人长得漂亮")
        datas.add("长腿")
        datas.add("打酱油的")
        datas.add("骗子")
        selfAdaptionGridView.setDatas(datas, null, SelfAdaptionGridView.GRAY)

    }

    override fun checkDatas(datas: MutableList<String>?) {
        ToastUtil.longToast(this, datas.toString())
    }

}
