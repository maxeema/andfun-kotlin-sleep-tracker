package maxeem.america.sleeping_journal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import maxeem.america.sleeping_journal.R
import maxeem.america.sleeping_journal.data.Night
import maxeem.america.sleeping_journal.databinding.JournalItemBinding

class JournalAdapter : ListAdapter<Night, ViewHolder>(DiffNightCallback()) {

    companion object {
        val virtualHeader = Night(Long.MIN_VALUE)
        val virtualFooter = Night(Long.MIN_VALUE+1)

        const val VIEW_TYPE_NIGHT = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_FOOTER = 2
    }

    var onItemClick : View.OnClickListener? = null
    var onItemLongClick : View.OnLongClickListener? = null

    val isVirtual : Night.()->Boolean = { this in arrayOf(virtualHeader, virtualFooter) }
    fun isVirtualAt(position: Int) = getItem(position)?.isVirtual() ?: false

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

    public override fun getItem(position: Int) : Night? = super.getItem(position)

    override fun submitList(list: List<Night>?) {
        super.submitList((if (list.isNullOrEmpty()) mutableListOf() else list.toMutableList()).apply {
            if (isNotEmpty())
                add(0, virtualHeader)
            add(virtualFooter)
        })
    }

}

class DiffNightCallback: DiffUtil.ItemCallback<Night>() {

    override fun areItemsTheSame   (oldItem: Night, newItem: Night) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Night, newItem: Night) = oldItem == newItem

}

sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class HeaderHolder private constructor (view: View) : ViewHolder(view) {
        companion object {
            fun of(parent: ViewGroup) = HeaderHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.journal_header, parent, false)
//                        .apply { setBackgroundColor(Color.YELLOW) }
            )
        }
    }

    class FooterHolder private constructor (view: View) : ViewHolder(view) {
        companion object {
            fun of(parent: ViewGroup) = FooterHolder(
                JournalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
//                        .apply { setBackgroundColor(Color.GREEN) }
            )
        }
    }

    class NightHolder private constructor (val binding: JournalItemBinding) : ViewHolder(binding.root) {
        companion object {
            fun of(parent: ViewGroup) = NightHolder(
                JournalItemBinding .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        override fun bind(item: Night, onClickListener: View.OnClickListener?, onLongClickListener: View.OnLongClickListener?) {
            binding.apply {
                night = item
                clickListener = onClickListener
                longClickListener = onLongClickListener
                itemView.tag = item
                executePendingBindings()
            }
        }
    }

    open fun bind(item: Night, onClickListener: View.OnClickListener? = null, onLongClickListener: View.OnLongClickListener? = null) { }

}
