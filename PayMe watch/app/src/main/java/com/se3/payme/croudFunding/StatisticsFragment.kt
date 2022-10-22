package com.se3.payme.croudFunding

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.se3.payme.R

class StatisticsFragment : Fragment() {
private lateinit var aaChartView:AAChartView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_statistics, container, false)
        aaChartView = view.findViewById<AAChartView>(R.id.aa_chart_view)



        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
//            .title("title")
//            .subtitle("subtitle")

            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf("#000000"))
            .gradientColorEnable(true)
            .touchEventEnabled(false)
            .markerRadius(0)
            .backgroundColor(Color.TRANSPARENT)
            .margin(arrayOf(6))

//            .yAxisLabelsEnabled(false)
            .yAxisGridLineWidth(0)
            .xAxisGridLineWidth(0)
//            .xAxisVisible(false)
//            .xAxisLabelsEnabled(false)

            .series(arrayOf(
                AASeriesElement( )
                    .lineWidth(4)
                    .name("")
//                    .colorByPoint(true)
                    .data(arrayOf(10,30, 4, 40, 30, 45)),
//                AASeriesElement()
//                    .name("NewYork")
//                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
//                AASeriesElement()
//                    .name("London")
//                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
//                AASeriesElement()
//                    .name("Berlin")
//                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )

        aaChartView.aa_drawChartWithChartModel(aaChartModel)


        return  view
    }

}