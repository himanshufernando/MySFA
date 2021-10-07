package tkhub.project.sfa.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tkhub.project.sfa.data.model.MenuItems
import tkhub.project.sfa.databinding.ListviewDrawerItemsBinding


class MenusAdapter : ListAdapter<MenuItems, RecyclerView.ViewHolder>(MenusiffCallback()) {
    lateinit var mClickListener: ClickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuitem = getItem(position)
        (holder as MenuItemViewHolder).bind(menuitem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemViewHolder(
            ListviewDrawerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            mClickListener
        )
    }
    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(selectedMenu: MenuItems, aView: View)
    }
    class MenuItemViewHolder(private val binding: ListviewDrawerItemsBinding ,var mClickListener: ClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { binding.menuitems?.let { selectedMenu -> mClickListener.onClick(selectedMenu,it) } }
        }
        fun bind(items: MenuItems) {
            binding.apply { menuitems = items
                executePendingBindings()
            }

        }
    }
}

private class MenusiffCallback : DiffUtil.ItemCallback<MenuItems>() {
    override fun areItemsTheSame(oldItem: MenuItems, newItem: MenuItems): Boolean {
        return oldItem.menuItemID == newItem.menuItemID
    }
    override fun areContentsTheSame(oldItem: MenuItems, newItem: MenuItems): Boolean {
        return oldItem == newItem
    }
}
