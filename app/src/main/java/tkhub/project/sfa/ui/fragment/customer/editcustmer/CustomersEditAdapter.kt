package tkhub.project.sfa.ui.fragment.customer.editcustmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.databinding.ListviewEditCustomersBinding


class CustomersEditAdapter : ListAdapter<Customer, RecyclerView.ViewHolder>(CustomersEditDiffCallback()) {
    lateinit var mClickListener: ClickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val customeritem = getItem(position)
        (holder as CustomersEditItemViewHolder).bind(customeritem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomersEditItemViewHolder(ListviewEditCustomersBinding.inflate(LayoutInflater.from(parent.context), parent, false), mClickListener)
    }
    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(selectedCustomer: Customer, aView: View, position: Int)
    }
    class CustomersEditItemViewHolder(private val binding: ListviewEditCustomersBinding ,var mClickListener: ClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { binding.customer?.let { selectedCustomer -> mClickListener.onClick(selectedCustomer,it,adapterPosition) } }
        }
        fun bind(rec: Customer) {
            binding.apply { customer = rec
                executePendingBindings()
            }

        }
    }
}

private class CustomersEditDiffCallback : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.customer_id == newItem.customer_id
    }
    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }
}
