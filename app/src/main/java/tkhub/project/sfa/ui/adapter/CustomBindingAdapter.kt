package tkhub.project.sfa.ui.adapter


import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import tkhub.project.sfa.R
import tkhub.project.sfa.data.model.customers.Customer


object CustomBindingAdapter {

    @BindingAdapter("setMenuSelection")
    @JvmStatic
    fun setMenuSelection(view: ConstraintLayout, isSelect : Boolean) {
          if(isSelect){
              view.setBackgroundColor(Color.parseColor("#FFDA05"))
          }else{
              view.setBackgroundColor(Color.parseColor("#FFFFFF"))
          }

    }


   //[customer]
    @BindingAdapter("setCustomerApproveStatus")
    @JvmStatic
    fun setCustomerApproveStatus(view: ConstraintLayout, customer: Customer) {
       when {
           customer.customer_approved -> {
               view.setBackgroundResource(R.drawable.circle_shape_green_status)
           }
           customer.customer_reject -> {
               view.setBackgroundResource(R.drawable.circle_shape_red_status)
           }
           else -> {
               view.setBackgroundResource(R.drawable.circle_shape_yellow_status)
           }
       }
    }


    //[customer]
    @BindingAdapter("setCustomerMoreDetails")
    @JvmStatic
    fun setCustomerMoreDetails(view: ConstraintLayout, status: Boolean) {
         if(status){
             view.visibility = View.VISIBLE
         }else{
             view.visibility = View.GONE
         }

    }

}