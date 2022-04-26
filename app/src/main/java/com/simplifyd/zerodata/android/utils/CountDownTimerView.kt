package com.simplifyd.zerodata.android.utils

import android.content.Context
import android.os.CountDownTimer
import android.text.Html
import android.util.AttributeSet
import com.simplifyd.zerodata.android.R
import java.util.concurrent.TimeUnit

class CountDownTimerView : androidx.appcompat.widget.AppCompatTextView {

    var timeOut: Long = 0
    var countDownListener: CountDownTimerListener? = null
    var currentNumberOfMinutes = 0

    //cancel timer
    fun cancelTimer() {
        countDownTimer?.cancel()
        countDownListener?.onCountDownCancelled()
    }

    fun runCountDownTimer() {
        countDownTimer?.start()
    }


    private var countDownTimer: CountDownTimer? = null

    private fun formatMilliSecondsToTime(milliseconds: Long): String {

        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()
        return (twoDigitString(minutes.toLong()) + " : "
                + twoDigitString(seconds.toLong()))
    }

    private fun twoDigitString(number: Long): String {

        if (number == 0L) {
            return "00"
        }

        return if (number / 10 == 0L) {
            "0$number"
        } else number.toString()

    }


    private fun initializeCountDownTimer(timeOut: Long) {
        countDownTimer = object : CountDownTimer(timeOut, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                currentNumberOfMinutes = TimeUnit.MILLISECONDS
                    .toMinutes(this@CountDownTimerView.timeOut - millisUntilFinished).toInt()
                    this@CountDownTimerView.text =Html.fromHtml(
                            context.resources.getString(
                                R.string.resend_code_in,
                                formatMilliSecondsToTime(millisUntilFinished)
                            ))


            }

            override fun onFinish() {
                countDownListener?.onCountDownDone()
            }
        }
    }

    fun setTimeOutMinutes(timeInMinutes: Int) {
        timeOut = TimeUnit.MINUTES.toMillis(timeInMinutes.toLong())
        initializeCountDownTimer(timeOut)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface CountDownTimerListener {
        fun onCountDownDone()
        fun onTimerStarted()
        fun onCountDownCancelled()
    }
}