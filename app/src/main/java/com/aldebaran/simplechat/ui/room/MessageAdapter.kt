package com.aldebaran.simplechat.ui.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.simplechat.R
import com.aldebaran.simplechat.database.table.Messages
import com.aldebaran.simplechat.databinding.AdapterBubbleChatBinding
import com.aldebaran.simplechat.databinding.AdapterBubbleOpponentChatBinding

class MessageAdapter(private val me: String, diffCallback: DiffUtil.ItemCallback<Messages>) :
    ListAdapter<Messages, RecyclerView.ViewHolder>(diffCallback) {

    private val MY_CHAT = 0
    private val OPPONENT_CHAT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == MY_CHAT){
            val view: AdapterBubbleChatBinding = DataBindingUtil
                .inflate(inflater, R.layout.adapter_bubble_chat, parent, false)
            MyChatView(view.root, view)
        } else {
            val view: AdapterBubbleOpponentChatBinding = DataBindingUtil
                .inflate(inflater, R.layout.adapter_bubble_opponent_chat, parent, false)
            OpponentView(view.root, view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyChatView){
            holder.binds.msg = getItem(position)
        } else if(holder is OpponentView){
            holder.binds.msg = getItem(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).from.orEmpty() == me) MY_CHAT else OPPONENT_CHAT
    }

    inner class MyChatView(root: View, val binds: AdapterBubbleChatBinding): RecyclerView.ViewHolder(root)
    inner class OpponentView(root: View, val binds: AdapterBubbleOpponentChatBinding): RecyclerView.ViewHolder(root)
}