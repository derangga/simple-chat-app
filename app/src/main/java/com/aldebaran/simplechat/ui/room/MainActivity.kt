package com.aldebaran.simplechat.ui.room

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.simplechat.Injection
import com.aldebaran.simplechat.R
import com.aldebaran.simplechat.databinding.ActivityMainBinding
import com.aldebaran.simplechat.helper.goToActivity
import com.aldebaran.simplechat.ui.UpdateBottomDialog
import com.aldebaran.simplechat.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var factory: MainFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var mAdapter: MessageAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerObserver: RecyclerView.AdapterDataObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = Injection.provideMainFactory(applicationContext)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

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
        viewModel.chat.observe(this, Observer { messages ->
            mAdapter.setChatList(messages)
            binding.chatList.smoothScrollToPosition(0)
        })

        viewModel.bubbleChat.observe(this, Observer { chat ->
            val bottomSheet = UpdateBottomDialog(chat, viewModel)
            bottomSheet.show(supportFragmentManager, "")
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
        mAdapter = Injection.provideMessageAdapter(viewModel, viewModel.getUserName())
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
}
