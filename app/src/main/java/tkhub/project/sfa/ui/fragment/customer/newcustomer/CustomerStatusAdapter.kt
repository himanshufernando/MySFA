package tkhub.project.sfa.ui.fragment.customer.newcustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tkhub.project.sfa.data.model.customers.CustomerStatus

import tkhub.project.sfa.databinding.ListviewCustomerStatusBinding


class CustomerStatusAdapter : ListAdapter<CustomerStatus, RecyclerView.ViewHolder>(
    CustomerStatusDiffCallback()
) {
    lateinit var mClickListener: ClickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val districtitem = getItem(position)
        (holder as CustomerStatusViewHolder).bind(districtitem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomerStatusViewHolder(ListviewCustomerStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false), mClickListener)
    }
    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(selectedDistrict: CustomerStatus, aView: View)
    }
    class CustomerStatusViewHolder(private val binding: ListviewCustomerStatusBinding ,var mClickListener: ClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { binding.customerstatus?.let { selectedDistricts -> mClickListener.onClick(selectedDistricts,it) } }
        }
        fun bind(rec: CustomerStatus) {
            binding.apply { customerstatus = rec
                executePendingBindings()
            }

        }
    }
}

private class CustomerStatusDiffCallback : DiffUtil.ItemCallback<CustomerStatus>() {
    override fun areItemsTheSame(oldItem: CustomerStatus, newItem: CustomerStatus): Boolean {
        return oldItem.customer_status_id == newItem.customer_status_id
    }
    override fun areContentsTheSame(oldItem: CustomerStatus, newItem: CustomerStatus): Boolean {
        return oldItem == newItem
    }
}
