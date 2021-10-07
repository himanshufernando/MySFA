package tkhub.project.sfa.ui.fragment.customer.newcustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tkhub.project.sfa.data.model.Districts
import tkhub.project.sfa.databinding.ListviewDistrictsBinding

class DistrictsAdapter : ListAdapter<Districts, RecyclerView.ViewHolder>(DistrictsDiffCallback()) {
    lateinit var mClickListener: ClickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val districtitem = getItem(position)
        (holder as DistrictItemViewHolder).bind(districtitem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DistrictItemViewHolder(ListviewDistrictsBinding.inflate(LayoutInflater.from(parent.context), parent, false), mClickListener)
    }
    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(selectedDistrict: Districts, aView: View)
    }
    class DistrictItemViewHolder(private val binding: ListviewDistrictsBinding ,var mClickListener: ClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { binding.districts?.let { selectedDistricts -> mClickListener.onClick(selectedDistricts,it) } }
        }
        fun bind(rec: Districts) {
            binding.apply { districts = rec
                executePendingBindings()
            }

        }
    }
}

private class DistrictsDiffCallback : DiffUtil.ItemCallback<Districts>() {
    override fun areItemsTheSame(oldItem: Districts, newItem: Districts): Boolean {
        return oldItem.district_id == newItem.district_id
    }
    override fun areContentsTheSame(oldItem: Districts, newItem: Districts): Boolean {
        return oldItem == newItem
    }
}
