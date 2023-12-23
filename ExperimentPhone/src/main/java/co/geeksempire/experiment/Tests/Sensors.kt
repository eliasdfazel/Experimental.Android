/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/23/23, 7:28 AM
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
import co.geeksempire.experiment.Utils.Numbers.roundTo
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

        val gravity: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorsLayoutBinding.getSensorData.text = "TYPE_ACCELEROMETER"

            sensorsLayoutBinding.getSensorData.setOnClickListener {

            val sensorRegistration = sensorManager.registerListener(this@Sensors, accelerometer, SensorManager.SENSOR_DELAY_UI)

            if (sensorRegistration) {

                Handler(Looper.getMainLooper()).postDelayed({

                    sensorManager.unregisterListener(this@Sensors)

                }, 37)

            }

        }

        sensorsLayoutBinding.getSensorData.setOnLongClickListener {

            sensorsLayoutBinding.SensorData.text = ""

            true
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_GRAVITY -> {
                Log.d(this@Sensors.javaClass.simpleName, sensorEvent.values.size.toString())

                val xValue = sensorEvent.values[0].roundTo(5)
                val yValue = sensorEvent.values[1].roundTo(5)
                val zValue = sensorEvent.values[2].roundTo(5)

                Log.d(this@Sensors.javaClass.simpleName, "TYPE_GRAVITY -> X: $xValue | Y: $yValue | Z: $zValue")
            }
            Sensor.TYPE_GYROSCOPE -> {
                Log.d(this@Sensors.javaClass.simpleName, sensorEvent.values.size.toString())

                val xValue = sensorEvent.values[0].roundTo(5)
                val yValue = sensorEvent.values[1].roundTo(5)
                val zValue = sensorEvent.values[2].roundTo(5)

                Log.d(this@Sensors.javaClass.simpleName, "TYPE_GYROSCOPE -> X: $xValue | Y: $yValue | Z: $zValue")
            }
            Sensor.TYPE_ACCELEROMETER -> {
                Log.d(this@Sensors.javaClass.simpleName, sensorEvent.values.size.toString())

                val xValue = sensorEvent.values[0].roundTo(5)
                val yValue = sensorEvent.values[1].roundTo(5)
                val zValue = sensorEvent.values[2].roundTo(5)

                sensorsLayoutBinding.SensorData.append("X: $xValue | Y: $yValue | Z: $zValue\n")

                Log.d(this@Sensors.javaClass.simpleName, "TYPE_ACCELEROMETER -> X: $xValue | Y: $yValue | Z: $zValue")
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {



    }

}