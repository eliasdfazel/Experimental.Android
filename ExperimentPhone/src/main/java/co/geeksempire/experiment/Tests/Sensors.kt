/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/23/23, 4:29 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Tests

import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.SensorsLayoutBinding


class Sensors : AppCompatActivity(), SensorEventListener {

    val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }



    lateinit var sensorsLayoutBinding: SensorsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorsLayoutBinding = SensorsLayoutBinding.inflate(layoutInflater)
        setContentView(sensorsLayoutBinding.root)

        sensorsLayoutBinding.root.setBackgroundColor(getColor(R.color.premiumDark))

        window.navigationBarColor = getColor(R.color.premiumLight)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.premiumLight)))

        val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val linearAcceleration: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        sensorsLayoutBinding.getSensorData.setOnClickListener {

            sensorManager.registerListener(this@Sensors, linearAcceleration, SensorManager.SENSOR_DELAY_UI)

            Handler(Looper.getMainLooper()).postDelayed({

                sensorManager.unregisterListener(this@Sensors)

            }, 37)

        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                Log.d(this@Sensors.javaClass.simpleName, sensorEvent.values.size.toString())

                val xValue = sensorEvent.values[0]
                val yValue = sensorEvent.values[1]
                val zValue = sensorEvent.values[2]

                Log.d(this@Sensors.javaClass.simpleName, "X: $xValue | Y: $yValue | Z: $zValue")
            }
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                Log.d(this@Sensors.javaClass.simpleName, sensorEvent.values.size.toString())

                val xValue = sensorEvent.values[0]
                val yValue = sensorEvent.values[1]
                val zValue = sensorEvent.values[2]

                Log.d(this@Sensors.javaClass.simpleName, "X Acceleration: $xValue | Y Acceleration: $yValue | Z Acceleration: $zValue")
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {



    }

}