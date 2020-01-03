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
//
//        var scrollTestxView = findViewById<VerticalScrollTestxView>(R.id.scrollTestxView)
//
//        var datas = ArrayList<String>()
//        datas.add("床前明月光")
//        datas.add("疑是地上霜")
//        datas.add("举头望明月")
//        datas.add("低头思故乡")
//
//        scrollTestxView.setData(this, datas, 3000)


        var selfAdaptionGridView = findViewById<SelfAdaptionGridView>(R.id.selfAdaptionGridView)
        var datas = ArrayList<String>()
        datas.add("床前明月光")
        datas.add("疑是地上霜")
        datas.add("举头望")
        datas.add("举头")
        datas.add("低头思故乡")
        selfAdaptionGridView.setDatas(datas, this)

    }

    override fun checkDatas(datas: MutableList<String>?) {
        ToastUtil.longToast(this, datas.toString())
    }

}
