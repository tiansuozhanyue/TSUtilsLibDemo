package com.example.tsutilslib

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.*

/**
 * 作者： HeroCat
 * 时间：2019/10/31/031
 * 描述：
 */
class VerticalScrollTestxView constructor(context: Context, attrs: AttributeSet? = null) : ScrollView(context, attrs) {

    var datas = ArrayList<String>()
    var layout: LinearLayout? = null
    var index = 0

    init {
        initView(context)
    }

    private fun initView(context: Context) {

        isVerticalScrollBarEnabled = false

        layout = LinearLayout(context)

        var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        layout!!.layoutParams = lp
        layout!!.orientation = LinearLayout.VERTICAL

        addView(layout)

    }

    fun updataView(p: Int?) {

        layout!!.addView(createTextView(datas[p!!]))

        val anim = ValueAnimator.ofInt(0, height)
        anim.duration = 1000
        anim.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int
            scrollY = currentValue
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (layout!!.childCount > 1)
                    layout!!.removeViewAt(0)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        anim.start()

    }

    fun setData(mActivity: Activity?, data: ArrayList<String>?, time: Long) {
        if (datas?.size > 0)
            datas.clear()
        datas.addAll(data!!)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (index == datas.size) {
                    index = 0
                }

                showView(mActivity, index)

                index++
            }
        }, time, time)

    }

    private fun showView(mActivity: Activity?, p: Int?) {
        mActivity?.runOnUiThread {
            updataView(p)
        }
    }

    fun createTextView(info: String): TextView {
        var textView = TextView(context)
        textView!!.gravity = Gravity.CENTER_VERTICAL
        textView!!.text = info
        textView!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        return textView
    }

}
