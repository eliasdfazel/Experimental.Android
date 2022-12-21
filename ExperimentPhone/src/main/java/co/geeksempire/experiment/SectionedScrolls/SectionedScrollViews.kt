/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/21/22, 4:59 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.experiment.SectionedScrolls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import co.geeksempire.experiment.R
import co.geeksempire.experiment.databinding.SectionedScrollsLayoutBinding

class SectionedScrollViews : AppCompatActivity() {

    private val allColors by lazy {
        intArrayOf(
            applicationContext.getColor(R.color.default_color_bright),
            applicationContext.getColor(R.color.default_color_game_bright),
            applicationContext.getColor(R.color.cyberGreen),
            applicationContext.getColor(R.color.purple),
            applicationContext.getColor(R.color.transparent),
            applicationContext.getColor(R.color.yellow),
            applicationContext.getColor(R.color.pink),
            applicationContext.getColor(R.color.white),
            applicationContext.getColor(R.color.brown),
            applicationContext.getColor(R.color.brownLight),
            applicationContext.getColor(R.color.transparent),
            applicationContext.getColor(R.color.black),
            applicationContext.getColor(R.color.green),
            applicationContext.getColor(R.color.default_color_game_dark),
        ).toList()
    }

    lateinit var sectionedScrollsLayoutBinding: SectionedScrollsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sectionedScrollsLayoutBinding = SectionedScrollsLayoutBinding.inflate(layoutInflater)
        setContentView(sectionedScrollsLayoutBinding.root)

        val sectionedScrollsAdapter = SectionedScrollsAdapter(this@SectionedScrollViews, sectionedScrollsLayoutBinding.nextedRecyclerView, allColors)

        val sectionedLayoutManager = GridLayoutManager(applicationContext, 3, GridLayoutManager.VERTICAL, false)

        sectionedLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                return when (allColors[position]) {
                    getColor(R.color.transparent) -> {
                        3
                    }
                    else -> {
                        1
                    }
                }
            }

        }

        sectionedScrollsLayoutBinding.nextedRecyclerView.layoutManager = sectionedLayoutManager
        sectionedScrollsLayoutBinding.nextedRecyclerView.adapter = sectionedScrollsAdapter

    }

}