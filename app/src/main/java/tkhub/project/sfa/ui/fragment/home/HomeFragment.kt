package tkhub.project.sfa.ui.fragment.home

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.fragment_home.view.*
import tkhub.project.sfa.R
import tkhub.project.sfa.databinding.FragmentHomeBinding
import tkhub.project.sfa.ui.activity.MainActivity
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener {
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentHomeBinding

    private var chart: BarLineChartBase<*>? = null


    internal lateinit var viewPagerAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.img_home_navigation_drawer.setOnClickListener(this)
        binding.root.cl_customers.setOnClickListener(this)

        viewPagerAdapter = HomeViewPagerAdapter(this)
        binding.root.view_pager_chat.adapter = viewPagerAdapter


        binding.root.view_pager_chat.apply {
            offscreenPageLimit = HomeViewPagerAdapter.TRANSACTION_SCREEN_OFFSCREEN_LIMIT as Int
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })

        }


    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        mainActivity = (activity as MainActivity)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()

        trasperat(mainActivity)
        mainActivity.unLockDrawer()

        /*  chart1()
          chart2()
          chart3()*/
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_home_navigation_drawer -> {
                mainActivity.openDrawer()
            }
            R.id.cl_customers ->{
                binding.root.cl_customers.setCardBackgroundColor(getColor(requireContext(), R.color.colorPrimaryDark))
                binding.root.img_customer.setColorFilter(getColor(requireContext(), R.color.colorAppWhite))
                binding.root.txt_customer.setTextColor(getColor(requireContext(), R.color.colorAppWhite))

                NavHostFragment.findNavController(this).navigate(R.id.fragment_home_to_customers)
            }

        }
    }


    private fun trasperat(activity: Activity) {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity?.window!!.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = requireActivity().window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }


    /*  private fun chart1(){

          binding.root.chart1.setUsePercentValues(true)
          binding.root.chart1.dragDecelerationFrictionCoef = 0.95f



          binding.root.chart1.description = Description()


          binding.root.chart1.isDrawHoleEnabled = true
           binding.root.chart1.setHoleColor(Color.WHITE)

           binding.root.chart1.setTransparentCircleColor(Color.WHITE)
           binding.root.chart1.setTransparentCircleAlpha(5)

          binding.root.chart1.holeRadius = 5f
          binding.root.chart1.transparentCircleRadius = 5f



          binding.root.chart1.rotationAngle = 0f

          binding.root.chart1.isRotationEnabled = true
          binding.root.chart1.isHighlightPerTapEnabled = true

          binding.root.chart1.animateY(1400, Easing.EaseInOutQuad)



          val entries = ArrayList<PieEntry>()

          entries.add(PieEntry(35F, ""))

          entries.add(PieEntry(65F, ""))


          val dataSet = PieDataSet(entries, "")

          dataSet.setDrawIcons(false)

          dataSet.sliceSpace = 3f
          dataSet.iconsOffset = MPPointF(0F, 40F)
          dataSet.selectionShift = 5f


          // add a lot of colors
          val colors = ArrayList<Int>()



          for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

          for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)



          colors.add(ColorTemplate.getHoloBlue())

          dataSet.colors = colors
          val data = PieData(dataSet)
          data.setValueFormatter(PercentFormatter())
          data.setValueTextSize(11f)
          data.setValueTextColor(Color.WHITE)

          binding.root.chart1.data = data

          binding.root.chart1.highlightValues(null)


          binding.root.chart1.description.isEnabled = false

          binding.root.chart1.legend.isEnabled = false

          binding.root.chart1.invalidate()

      }



      private fun chart2(){

          binding.root.chart2.setUsePercentValues(true)
          binding.root.chart2.dragDecelerationFrictionCoef = 0.95f



          binding.root.chart2.description = Description()


          binding.root.chart2.isDrawHoleEnabled = true
          binding.root.chart2.setHoleColor(Color.WHITE)

          binding.root.chart2.setTransparentCircleColor(Color.WHITE)
          binding.root.chart2.setTransparentCircleAlpha(5)

          binding.root.chart2.holeRadius = 5f
          binding.root.chart2.transparentCircleRadius = 5f



          binding.root.chart2.rotationAngle = 0f

          binding.root.chart2.isRotationEnabled = true
          binding.root.chart2.isHighlightPerTapEnabled = true

          binding.root.chart2.animateY(1400, Easing.EaseInOutQuad)



          val entries = ArrayList<PieEntry>()

          entries.add(PieEntry(35F, ""))

          entries.add(PieEntry(20F, ""))

          entries.add(PieEntry(45F, ""))


          val dataSet = PieDataSet(entries, "")

          dataSet.setDrawIcons(false)

          dataSet.sliceSpace = 3f
          dataSet.iconsOffset = MPPointF(0F, 40F)
          dataSet.selectionShift = 5f


          // add a lot of colors
          val colors = ArrayList<Int>()


          for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

          for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

          for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)



          colors.add(ColorTemplate.getHoloBlue())

          dataSet.colors = colors
          val data = PieData(dataSet)
          data.setValueFormatter(PercentFormatter())
          data.setValueTextSize(11f)
          data.setValueTextColor(Color.WHITE)

          binding.root.chart2.data = data

          binding.root.chart2.highlightValues(null)


          binding.root.chart2.description.isEnabled = false

          binding.root.chart2.legend.isEnabled = false

          binding.root.chart2.invalidate()

      }



      private fun chart3(){



          binding.root.chart3.setDrawBarShadow(false)
          binding.root.chart3.setDrawValueAboveBar(true)



          binding.root.chart3.setMaxVisibleValueCount(60)

          binding.root.chart3.setPinchZoom(false)
          binding.root.chart3.setDrawGridBackground(false)


          // chart.setDrawYLabels(false);
          val xAxis = binding.root.chart3!!.xAxis
          xAxis.isEnabled = false

          val leftAxis = binding.root.chart3!!.axisLeft
          leftAxis.setLabelCount(6, false)
          leftAxis.axisMinimum =0f
          leftAxis.axisMaximum = 10f



          val rightAxis = binding.root.chart3!!.axisRight
          rightAxis.setDrawGridLines(false)
          rightAxis.setLabelCount(6, false)
          rightAxis.axisMinimum = 0f
          rightAxis.axisMaximum = 100f



        *//*  val l = binding.root.chart3!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f*//*

        binding.root.chart3!!.animateXY(1500, 1500)


        val entries = ArrayList<BarEntry>()

        entries.add(BarEntry(1F, 10F))
        entries.add(BarEntry(2F, 50F))


        val set: BarDataSet

        if (binding.root.chart3!!.data != null &&
            binding.root.chart3!!.data.dataSetCount > 0
        ) {
            set = binding.root.chart3!!.data.getDataSetByIndex(0) as BarDataSet
            set.values = entries
            binding.root.chart3!!.data.notifyDataChanged()
            binding.root.chart3!!.notifyDataSetChanged()
        } else {
            set = BarDataSet(entries, "")
            set.color = Color.rgb(240, 120, 124)
        }

        val data = BarData(set)
        data.setValueTextSize(10f)

        data.setDrawValues(false)
        data.barWidth = 0.8f


        binding.root.chart3.description.isEnabled = false
        binding.root.chart3.legend.isEnabled = false
        binding.root.chart3!!.data = data


    }
*/

}