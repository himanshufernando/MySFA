package tkhub.project.sfa.ui.fragment.customer.newcustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tkhub.project.sfa.data.model.route.Route

import tkhub.project.sfa.databinding.ListviewRoutesBinding

class RoutesAdapter : ListAdapter<Route, RecyclerView.ViewHolder>(RoutesDiffCallback()) {
    lateinit var mClickListener: ClickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val routeitem = getItem(position)

        (holder as DistrictItemViewHolder).bind(routeitem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DistrictItemViewHolder(ListviewRoutesBinding.inflate(LayoutInflater.from(parent.context), parent, false), mClickListener)
    }
    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(selectedRoute: Route, aView: View)
    }
    class DistrictItemViewHolder(private val binding: ListviewRoutesBinding ,var mClickListener: ClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { binding.route?.let { selectedRoutes -> mClickListener.onClick(selectedRoutes,it) } }
        }
        fun bind(rec: Route) {
            binding.apply { route = rec
                executePendingBindings()
            }

        }
    }
}

private class RoutesDiffCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.customer_route_id == newItem.customer_route_id
    }
    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }
}
