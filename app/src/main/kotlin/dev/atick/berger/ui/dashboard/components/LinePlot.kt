/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.berger.ui.dashboard.components

import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Utils
import dev.atick.berger.R

@Composable
fun LinePlot(
    dataset: LineDataSet,
    modifier: Modifier = Modifier,
) {
    val isDarkThemeEnabled = isSystemInDarkTheme()
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            Utils.init(ctx)
            LineChart(ctx).apply {
                description.text = ""
                axisLeft.setDrawLabels(false)
                axisLeft.isEnabled = true
                axisLeft.setDrawGridLines(false)
                axisLeft.axisLineWidth = 2.0F
                axisLeft.setDrawLabels(true)
                axisLeft.labelCount = 4
                axisRight.setDrawLabels(false)
                axisRight.isEnabled = false
                xAxis.setDrawLabels(true)
                xAxis.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.axisLineWidth = 2.0F
                legend.isEnabled = true
                setTouchEnabled(false)
            }
        },
        update = { lineChart ->
            lineChart.xAxis.labelCount = 3
            lineChart.axisLeft.textColor = if (isDarkThemeEnabled) {
                Color.LTGRAY
            } else {
                Color.DKGRAY
            }
            lineChart.xAxis.textColor = if (isDarkThemeEnabled) {
                Color.LTGRAY
            } else {
                Color.DKGRAY
            }
            dataset.apply {
                color = context.getColor(R.color.primary)
                if (isDarkThemeEnabled) {
                    this.fillColor = context.getColor(R.color.primary)
                    this.fillAlpha = 40
                } else {
                    this.fillDrawable = ContextCompat.getDrawable(
                        context,
                        R.drawable.primary_gradient,
                    )
                }
                setDrawValues(false)
                setDrawFilled(true)
                setDrawCircleHole(false)
                setDrawCircles(false)
                lineWidth = 3.0F
            }
            val lineData = LineData(dataset)
            lineData.notifyDataChanged()
            lineChart.data = lineData
            lineChart.invalidate()
        },
        modifier = modifier,
    )
}
