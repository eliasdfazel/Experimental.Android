/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/20/23, 6:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Tests

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class Sensors : AppCompatActivity(), SensorEventListener {

    val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    val accelerometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager.registerListener(this@Sensors, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this@Sensors)

    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        if (sensorEvent?.sensor?.type ==Sensor.TYPE_ACCELEROMETER) {

            val xValue = sensorEvent.values[0]
            val yValue = sensorEvent.values[1]
            val zValue = sensorEvent.values[2]

        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {



    }

}