/*
 * Copyright © 2022 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/29/22, 7:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package co.geeksempire.blurry.effect.utils

import android.graphics.Path
import android.util.Log
import androidx.core.graphics.PathParser

class VectorPathParser {

    fun parser(vectorPath: String, scaleAmount: Int = 10) : Path {

        val path = Path()

        PathParser.createNodesFromPathData(vectorPath).forEachIndexed { index, pathDataNode ->
            Log.d(this@VectorPathParser.javaClass.simpleName, "${pathDataNode.mType} ::: ${pathDataNode.mParams.toList()}")

            when (pathDataNode.mType.toString()) {
                "M", "m" -> { // Move

                    val moveToData = pathDataNode.mParams.toList()
                    path.moveTo(moveToData[0] * scaleAmount, moveToData[1] * scaleAmount)

                }
                "L", "l" -> { // Line

                    val lineToData = pathDataNode.mParams.toList()
                    path.lineTo(lineToData[0] * scaleAmount, lineToData[1] * scaleAmount)

                }
                "H", "h" -> { // Horizontal

                    val horizontalToData = pathDataNode.mParams.toList()
                    if (horizontalToData.size == 2) {

                        path.rLineTo(horizontalToData[0] * scaleAmount, horizontalToData[1] * scaleAmount)

                    } else {

                        path.rLineTo(horizontalToData[0] * scaleAmount, 0f)

                    }

                }
                "V", "v" -> { // Vertical

                    val verticalToData = pathDataNode.mParams.toList()
                    if (verticalToData.size == 2) {

                        path.rLineTo(verticalToData[0] * scaleAmount, verticalToData[1] * scaleAmount)

                    } else {

                        path.rLineTo(verticalToData[0] * scaleAmount, verticalToData[0] * scaleAmount)

                    }

                }
                "A", "a" -> { //

                    val arcToData = pathDataNode.mParams.toList()
                    path.arcTo(arcToData[0] * scaleAmount, arcToData[1] * scaleAmount, arcToData[2] * scaleAmount, arcToData[3] * scaleAmount, arcToData[4], arcToData[5], false)

                }
                "C", "c" -> { // Vertical

                    val cubicToData = pathDataNode.mParams.toList()
                    path.cubicTo(cubicToData[0] * scaleAmount, cubicToData[1] * scaleAmount, cubicToData[2] * scaleAmount, cubicToData[3] * scaleAmount, cubicToData[4] * scaleAmount, cubicToData[5] * scaleAmount)

                }
                "Z", "z" -> { // Close

                    path.close()

                }
            }

        }

        return path

    }

}