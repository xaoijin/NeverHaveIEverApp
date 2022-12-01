package com.example.drinkinggame

import android.content.Intent
import android.graphics.Color.parseColor
import android.hardware.*
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CameraMiniGame : AppCompatActivity() {
    lateinit var textX: TextView
    lateinit var textY:TextView
    lateinit var sensorManager: SensorManager
    lateinit var timertext: TextView
    lateinit var sensor: Sensor
    lateinit var backColor: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_mini_game)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        textX = findViewById(R.id.textX)
        textY = findViewById(R.id.textY)
        timertext=findViewById(R.id.timer)
        backColor=findViewById(R.id.back)


        object : CountDownTimer(10000, 1000)
        {
            override fun onTick(millisUntilFinished: Long)
            {
                timertext.text = "Seconds Remaining: " + millisUntilFinished / 1000
            }

            override fun onFinish()
            {
                val intent = Intent(this@CameraMiniGame, SafeScreen::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(gyroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()
        sensorManager!!.unregisterListener(gyroListener)
    }

    var gyroListener: SensorEventListener = object : SensorEventListener
    {
        override fun onAccuracyChanged(sensor: Sensor?, acc: Int) {}
        override fun onSensorChanged(event: SensorEvent)
        {
            val x = event.values[0]
            val y = event.values[1]
            if (x.toInt()>1||y.toInt()>1||x.toInt()<-1||y.toInt()<-1)
            {
                backColor.setBackgroundResource(R.color.Green)
            }
            if (x.toInt()>3||y.toInt()>3||x.toInt()<-3||y.toInt()<-3)
            {
                backColor.setBackgroundResource(R.color.Yellow)
            }
            if(x.toInt()>=5||y.toInt()>=5||x.toInt()<=-5||y.toInt()<=-5)
            {
                val intent = Intent(this@CameraMiniGame, FailScreen::class.java)
                startActivity(intent)
                finish()
            }
            textX!!.setText("X : " + x.toInt() + " rad/s")
            textY!!.setText("Y : " + y.toInt() + " rad/s")
        }
    }
}
