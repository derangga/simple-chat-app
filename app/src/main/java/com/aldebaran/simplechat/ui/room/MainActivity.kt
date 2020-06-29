package com.aldebaran.simplechat.ui.room

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.simplechat.R
import com.aldebaran.simplechat.base.BaseActivity
import com.aldebaran.simplechat.database.table.Messages
import com.aldebaran.simplechat.databinding.ActivityMainBinding
import com.aldebaran.simplechat.helper.goToActivity
import com.aldebaran.simplechat.helper.initViewModel
import com.aldebaran.simplechat.ui.login.LoginActivity
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject lateinit var factory: MainFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var mAdapter: MessageAdapter
    private lateinit var recyclerObserver: RecyclerView.AdapterDataObserver

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onCreateScope(savedInstanceState: Bundle?) {
        getComponent().inject(this)
        viewModel = initViewModel(factory)
        viewModel.streamDb()
        setupRecycler()
        eventListener()
        subscribeUI()
    }

    override fun onDestroy() {
        mAdapter.unregisterAdapterDataObserver(recyclerObserver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_room, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout -> {
                viewModel.actionLogout{
                    goToActivity<LoginActivity>()
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUI(){
        viewModel.observerMessage().observe(this, Observer { messages ->
            mAdapter.submitList(messages)
            binding.chatList.smoothScrollToPosition(0)
        })
    }

    private fun eventListener(){
        binding.btnSend.setOnClickListener {
            val message = binding.textBox.text.toString()
            if(message.isNotEmpty()){
                viewModel.sendMessage(viewModel.handleLeadingSpace(message), viewModel.getUserName())
                binding.textBox.setText("")
            }
        }
    }

    private fun setupRecycler(){
        val linearManager = LinearLayoutManager(this)
        mAdapter = MessageAdapter(viewModel.getUserName(), getDiffCallBack())
        binding.chatList.apply {
            linearManager.reverseLayout = true
            linearManager.stackFromEnd = true
            layoutManager = linearManager
            adapter = mAdapter
            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                run {
                    if (bottom < oldBottom) {
                        postDelayed(
                            { smoothScrollToPosition(0) },
                            100
                        )
                    }}
            }
        }
        recyclerObserver = object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val dataCount = mAdapter.itemCount
                val lastVisiblePosition = linearManager.findLastCompletelyVisibleItemPosition()
                if(lastVisiblePosition == -1 ||
                    (positionStart >= (dataCount - 1) &&
                            lastVisiblePosition == (positionStart - 1))
                ){
                    binding.chatList.smoothScrollToPosition(0)
                }
            }
        }
        mAdapter.registerAdapterDataObserver(recyclerObserver)
    }

    private fun getDiffCallBack(): DiffUtil.ItemCallback<Messages>{
        return object: DiffUtil.ItemCallback<Messages>(){
            override fun areItemsTheSame(oldItem: Messages, newItem: Messages): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Messages, newItem: Messages): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }
        }
    }
}
