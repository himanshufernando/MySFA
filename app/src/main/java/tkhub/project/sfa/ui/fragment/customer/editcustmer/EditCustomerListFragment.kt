package tkhub.project.sfa.ui.fragment.customer.editcustmer

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_edit_customer_list.view.*
import tkhub.project.sfa.R
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.SfaViewModelResult
import tkhub.project.sfa.databinding.FragmentEditCustomerListBinding
import tkhub.project.sfa.ui.activity.MainActivity

import tkhub.project.sfa.viewmodels.customer.CustomerViewModel


class EditCustomerListFragment : Fragment() {

    private val viewmodel: CustomerViewModel by viewModels { CustomerViewModel.LiveDataVMFactory }
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentEditCustomerListBinding
    private val adapterCustomer =CustomersEditAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_customer_list, container, false)
        binding.editCustomerListViewModel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCustomerListRecyclerView()

        binding.root.cl_navigation_edit_customer.setOnClickListener {
            mainActivity.openDrawer()
        }

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

        if(viewmodel.customerList.value!!.isEmpty()){
            getCustomers()
        }else{
            adapterCustomer.notifyDataSetChanged()
        }
    }

    private fun getCustomers() {
        viewmodel.getCustomer().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    viewmodel.customerList.value = it.data.data
                    adapterCustomer.submitList(viewmodel.customerList.value)
                }
                is SfaViewModelResult.ExceptionError.ExError -> {

                }
                is SfaViewModelResult.LogicalError.LogError -> {

                }

            }

        })
    }

    private fun setCustomerListRecyclerView() {
        binding.root.recyclerView_edit_customers_list.adapter = adapterCustomer
        adapterCustomer.setOnItemClickListener(object : CustomersEditAdapter.ClickListener {
            override fun onClick(selectedCustomer: Customer, aView: View, position: Int) {
                goToCustomerEdit(selectedCustomer)

            }
        })
    }


    private fun goToCustomerEdit(customer: Customer){
        val bundle = bundleOf(Pair("selected_customer", customer))
        NavHostFragment.findNavController(this).navigate(
            R.id.fragmentEditCustomerListToEditCustomer,
            bundle
        )


    }

}