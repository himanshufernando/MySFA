package tkhub.project.sfa.ui.fragment.customer.editcustmer

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_edit_customer.view.*
import tkhub.project.sfa.R
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.databinding.FragmentEditCustomerBinding
import tkhub.project.sfa.ui.activity.MainActivity
import tkhub.project.sfa.ui.fragment.customer.newcustomer.CustomerStatusAdapter
import tkhub.project.sfa.ui.fragment.customer.newcustomer.DistrictsAdapter
import tkhub.project.sfa.ui.fragment.customer.newcustomer.RoutesAdapter
import tkhub.project.sfa.viewmodels.customer.CustomerViewModel


class EditCustomerFragment : Fragment() ,View.OnClickListener{


    private val viewmodel: CustomerViewModel by viewModels { CustomerViewModel.LiveDataVMFactory }
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentEditCustomerBinding

    private val adapterDistricts = DistrictsAdapter()
    private val adapterCustomerStatus = CustomerStatusAdapter()
    private val adapterRoutes = RoutesAdapter()

    var selectedCustomer = Customer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (arguments?.containsKey("selected_customer")!!) {
            selectedCustomer = arguments?.getParcelable<Customer>("selected_customer")!!
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_customer, container, false)
        binding.customerEditViewModel = viewmodel


        binding.root.cl_navigation_customers.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mainActivity = (activity as MainActivity)
    }

    override fun onStop() {
        super.onStop()


    }

    override fun onResume() {
        super.onResume()
        mainActivity.unLockDrawer()


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cl_navigation_customers -> {
                mainActivity.onBackPressed()
            }

        }

    }

}