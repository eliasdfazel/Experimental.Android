/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/23/23, 4:11 AM
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

    val accelerometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    lateinit var sensorsLayoutBinding: SensorsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorsLayoutBinding = SensorsLayoutBinding.inflate(layoutInflater)
        setContentView(sensorsLayoutBinding.root)

        sensorsLayoutBinding.root.setBackgroundColor(getColor(R.color.premiumDark))

        window.navigationBarColor = getColor(R.color.premiumLight)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.premiumLight)))

        sensorsLayoutBinding.getSensorData.setOnClickListener {

            sensorManager.registerListener(this@Sensors, accelerometer, SensorManager.SENSOR_DELAY_UI)

            Handler(Looper.getMainLooper()).postDelayed({

                sensorManager.unregisterListener(this@Sensors)

            }, 37)

        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        if (sensorEvent?.sensor?.type ==Sensor.TYPE_ACCELEROMETER) {

            val xValue = sensorEvent.values[0]
            val yValue = sensorEvent.values[1]
            val zValue = sensorEvent.values[2]

            Log.d(this@Sensors.javaClass.simpleName, "X: $xValue | Y: $yValue | Z: $zValue")
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {



    }

}