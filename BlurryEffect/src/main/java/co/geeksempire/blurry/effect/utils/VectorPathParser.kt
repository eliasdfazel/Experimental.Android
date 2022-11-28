/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/28/22, 3:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.blurry.effect.utils

import android.graphics.Path
import android.util.Log
import androidx.core.graphics.PathParser

class VectorPathParser {

    fun parser(vectorPath: String) : Path {

        val path = Path()

        val aPath = PathParser.createNodesFromPathData("M20,10C22,13 17,22 15,22C13,22 13,21 12,21C11,21 11,22 9,22C7,22 2,13 4,10C6,7 9,7 11,8V5C5.38,8.07 4.11,3.78 4.11,3.78C4.11,3.78 6.77,0.19 11,5V3H13V8C15,7 18,7 20,10Z")

        aPath.forEachIndexed { index, pathDataNode ->
            Log.d(this@VectorPathParser.javaClass.simpleName, "${pathDataNode.mType} ::: ${pathDataNode.mParams.toList()}")

            when (pathDataNode.mType.toString()) {
                "M", "m" -> { // Move

                    val moveToData = pathDataNode.mParams.toList()
                    path.moveTo(moveToData[0], moveToData[1])

                }
                "L", "l" -> { // Line



                }
                "H", "h" -> { // Horizontal

                    val horizontalToData = pathDataNode.mParams.toList()
                    path.lineTo(horizontalToData[0], horizontalToData[1])

                }
                "V", "v" -> { // Vertical

                    val verticalToData = pathDataNode.mParams.toList()
                    path.lineTo(verticalToData[0], verticalToData[1])

                }
                "A", "a" -> { // Vertical



                }
                "C", "c" -> { // Vertical

                    val cubicToData = pathDataNode.mParams.toList()
                    path.cubicTo(cubicToData[0], cubicToData[1], cubicToData[2], cubicToData[3], cubicToData[4], cubicToData[5])

                }
                "Z", "z" -> { // Close

                    path.close()

                }
            }

        }

        return path

    }

}