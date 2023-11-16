/*
 * Copyright © 2023 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/16/23, 4:02 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.Widgets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.experiment.databinding.WidgetConfigurationsLayoutBinding

class WidgetsConfigurations : AppCompatActivity() {

    lateinit var widgetsConfigurationsLayoutBinding: WidgetConfigurationsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetsConfigurationsLayoutBinding = WidgetConfigurationsLayoutBinding.inflate(layoutInflater)
        setContentView(widgetsConfigurationsLayoutBinding.root)



    }

}