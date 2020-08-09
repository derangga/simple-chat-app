package com.aldebaran.simplechat.ui.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.simplechat.data.Chat
import com.aldebaran.simplechat.databinding.AdapterBubbleChatBinding
import com.aldebaran.simplechat.databinding.AdapterBubbleOpponentChatBinding
import com.aldebaran.simplechat.helper.toSimpleDateTime

class MessageAdapter(
    private val viewModel: MainViewModel,
    private val me: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MY_CHAT = 0
    private val OPPONENT_CHAT = 1
    private var chatList: List<Chat> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == MY_CHAT){
            val view = AdapterBubbleChatBinding
                .inflate(inflater, parent, false)
            MyChatView(view.root, view)
        } else {
            val view = AdapterBubbleOpponentChatBinding
                .inflate(inflater, parent, false)
            OpponentView(view.root, view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatList[position]
        if(holder is MyChatView){
            holder.bindView(message)
        } else if(holder is OpponentView){
            holder.bindView(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatList[position].from.orEmpty() == me) MY_CHAT else OPPONENT_CHAT
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setChatList(chatList: List<Chat>){
        this.chatList = chatList
        notifyDataSetChanged()
    }

    inner class MyChatView(
        root: View, private val binds: AdapterBubbleChatBinding
    ): RecyclerView.ViewHolder(root){
        fun bindView(chat: Chat){
            binds.message.text = chat.message.orEmpty()
            binds.time.text = chat.createdAt.toSimpleDateTime()

            binds.bubble.setOnLongClickListener {
                viewModel.onBubbleChatLongClick(chat)
                true
            }
        }
    }

    inner class OpponentView(
        root: View, private val binds: AdapterBubbleOpponentChatBinding
    ): RecyclerView.ViewHolder(root){
        fun bindView(chat: Chat){
            binds.from.text = chat.from.orEmpty()
            binds.message.text = chat.message.orEmpty()
            binds.time.text = chat.createdAt.toSimpleDateTime()
        }
    }
}