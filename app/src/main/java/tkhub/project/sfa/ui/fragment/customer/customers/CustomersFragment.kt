package tkhub.project.sfa.ui.fragment.customer.customers

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_customers.view.*
import tkhub.project.sfa.R
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.SfaViewModelResult
import tkhub.project.sfa.databinding.FragmentCustomersBinding
import tkhub.project.sfa.ui.activity.MainActivity
import tkhub.project.sfa.ui.dialog.InfoDialog
import tkhub.project.sfa.viewmodels.customer.CustomerViewModel


class CustomersFragment : Fragment(), InfoDialog.InfoDialogListener,View.OnClickListener {

    private val viewmodel: CustomerViewModel by viewModels { CustomerViewModel.LiveDataVMFactory }
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentCustomersBinding
    private val adapterCustomer = CustomersAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customers, container, false)
        binding.customerViewModel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.root.img_navigation_customers.setOnClickListener(this)
        binding.root.img_search.setOnClickListener(this)
        binding.root.cl_add_new_customers.setOnClickListener(this)

        setCustomerListRecyclerView()




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
        mainActivity.lockDrawer()

        getCustomers()


    }



    override fun onClick(v: View) {
       when(v.id){
           R.id.img_navigation_customers ->   mainActivity.onBackPressed()
           R.id.img_search ->{
               binding.root.txt_menu_name.visibility = View.GONE
               binding.root.edit_text_search.visibility = View.VISIBLE

           }
           R.id.cl_add_new_customers ->   NavHostFragment.findNavController(this).navigate(R.id.fragment_customer_to_add_new)


       }
    }

    private fun getCustomers() {
        viewmodel.getCustomer().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    viewmodel.customerList.value = it.data.data
                    adapterCustomer.submitList(viewmodel.customerList.value)

                }
                is SfaViewModelResult.ExceptionError -> {

                }
                is SfaViewModelResult.LogicalError -> {

                }

            }

        })
    }

    override fun onInfoDialogPositive(code: Int) {

    }
    private fun setCustomerListRecyclerView() {
        binding.root.recyclerView_customers.adapter = adapterCustomer
        adapterCustomer.setOnItemClickListener(object : CustomersAdapter.ClickListener {
            override fun onClick(selectedCustomer: Customer, aView: View, position: Int) {
                when (aView.id) {
                    R.id.cl_drop -> {
                        selectedCustomer.customerIsExpand = !selectedCustomer.customerIsExpand
                        adapterCustomer.notifyItemChanged(position)

                    }
                    R.id.cl_location -> {


                    }

                }
            }
        })
    }
}