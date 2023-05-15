package com.jldevelops.neverhaveiever

import android.app.Dialog
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jldevelops.neverhaveiever.R

class CameraMiniGameDialogFragment : DialogFragment() {
    private lateinit var textX: TextView
    private lateinit var textY: TextView
    private lateinit var sensorManager: SensorManager
    private lateinit var timertext: TextView
    private lateinit var sensor: Sensor
    private lateinit var backColor: ImageView
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_camera_mini_game, null)

        if (activity != null) {
            sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            textX = view.findViewById(R.id.textX)
            textY = view.findViewById(R.id.textY)
            timertext = view.findViewById(R.id.timer)
            backColor = view.findViewById(R.id.back)

            playSound()

            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timertext.text = buildString {
        append("Seconds Remaining: ")
        append(millisUntilFinished)
        append(1000)
    }
                }

                override fun onFinish() {
                    stopSound()
                    dismiss()
                }
            }.start()
        }

        builder.setView(view)
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(gyroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(gyroListener)
    }

    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context, R.raw.ticking)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    private fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private val gyroListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, acc: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]

            if (x.toInt() > 1 || y.toInt() > 1 || x.toInt() < -1 || y.toInt() < -1) {
                backColor.setBackgroundResource(R.color.Green)
            }
            if (x.toInt() > 3 || y.toInt() > 3 || x.toInt() < -3 || y.toInt() < -3) {
                backColor.setBackgroundResource(R.color.Yellow)
            }
            if (x.toInt() >= 5 || y.toInt() >= 5 || x.toInt() <= -5 || y.toInt() <= -5) {
                stopSound()
                dismiss()
            }
            textX.text = buildString {
        append("X : ")
        append(x.toInt())
        append(" rad/s")
    }
            textY.text = buildString {
        append("Y : ")
        append(y.toInt())
        append(" rad/s")
    }
        }
    }
}
