package maxeem.america.sleep.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import maxeem.america.sleep.R
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.databinding.JournalItemBinding
import maxeem.america.sleep.ext.logPeriodEnd

class JournalAdapter : ListAdapter<JournalAdapter.Item, JournalAdapter.ViewHolder>(Companion) {

    sealed class Item(val id: Long) {
        class Virtual(id: Long) : Item(id)
        data class Real(val night: Night, val date: String, val count: Int?) : Item(night.id!!)
    }

    companion object Companion : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame   (oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

        const val VIEW_TYPE_NIGHT  = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_FOOTER = 2

        val virtualHeader = Item.Virtual(Long.MIN_VALUE)
        val virtualFooter = Item.Virtual(Long.MIN_VALUE+1)

        const val VIRTUALS_PLUS_ONE = 3
        const val FIRST_REAL_ITEM_INDEX = 1
    }

    var onItemClick : View.OnClickListener? = null
    var onItemLongClick : View.OnLongClickListener? = null

    init { setHasStableIds(true) }
    override fun getItemId(position: Int) = getItem(position)?.id ?: NO_ID

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also { holder.bind(it, onItemClick, onItemLongClick) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        VIEW_TYPE_HEADER -> ViewHolder.HeaderHolder.of(parent)
        VIEW_TYPE_FOOTER -> ViewHolder.FooterHolder.of(parent)
        else -> ViewHolder.NightHolder.of(parent)
    }

    override fun getItemViewType(position: Int) = when(getItem(position)) {
        virtualHeader -> VIEW_TYPE_HEADER
        virtualFooter -> VIEW_TYPE_FOOTER
        else -> VIEW_TYPE_NIGHT
    }

    public override fun getItem(position: Int) : Item? = super.getItem(position)

    fun submitData(list: List<Night>?) {
        if (list.isNullOrEmpty()) {
            super.submitList(emptyList())
            return
        }
        val data = mutableListOf<Item>()
        for ((endDate, nights) in list.groupBy { it.logPeriodEnd() }) {
            var idx = nights.size
            nights.forEach { night ->
                data.add(Item.Real(night, endDate, if (nights.size==1) null else idx--))
            }
        }
        super.submitList(data.apply {
            add(0, virtualHeader)
            add(virtualFooter)
        })
    }

    sealed class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        class HeaderHolder private constructor (view: View) : ViewHolder(view) {
            companion object {
                fun of(parent: ViewGroup) = HeaderHolder(
                        LayoutInflater.from(parent.context).inflate(R.layout.journal_header, parent, false)
                )
            }
        }

        class NightHolder private constructor (val binding: JournalItemBinding) : ViewHolder(binding.root) {
            companion object {
                fun of(parent: ViewGroup) = NightHolder(
                        JournalItemBinding .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            override fun bind(item: Item, onClickListener: View.OnClickListener?, onLongClickListener: View.OnLongClickListener?) {
                binding.apply {
                    date = (item as Item.Real).date
                    img = item.night.quality.img
                    count = item.count?.toString()
                    clickListener = onClickListener
                    longClickListener = onLongClickListener
                    itemView.tag = item.night
                    executePendingBindings()
                }
            }
        }

        class FooterHolder private constructor (view: View) : ViewHolder(view) {
            companion object {
                fun of(parent: ViewGroup) = FooterHolder(View(parent.context))
            }
        }

        open fun bind(item: Item, onClickListener: View.OnClickListener? = null,
                      onLongClickListener: View.OnLongClickListener? = null) {
            view.tag = item
        }

    }

}

val JournalAdapter.hasOnlyOneRealItem
        get() = itemCount == JournalAdapter.VIRTUALS_PLUS_ONE

fun JournalAdapter.isVirtualAt(position: Int)
        = getItem(position) in arrayOf(JournalAdapter.virtualHeader, JournalAdapter.virtualFooter)
