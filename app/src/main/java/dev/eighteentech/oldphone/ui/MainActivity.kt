package dev.eighteentech.oldphone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.eighteentech.oldphone.R
import dev.eighteentech.oldphone.databinding.ActivityMainBinding
import dev.eighteentech.oldphone.ui.view.DialListener
import dev.eighteentech.oldphone.ui.view.MainViewModel

class MainActivity : AppCompatActivity(), DialListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: MainAdapter
    private lateinit var viewModel:MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel()
        adapter = MainAdapter()
        manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recycler.adapter = adapter
            recycler.layoutManager = manager
            dialer.setListener(this@MainActivity)
            button.setOnClickListener {
                dialed.text = ""
                viewModel.clearWordsList()
            }
            viewModel.words.observe(this@MainActivity){
                adapter.updateList(it)
            }
        }

    }

    override fun onDial(number: Int) {
        binding.dialed.text = "${binding.dialed.text}$number"
        viewModel.updateWordsList(number)
    }

    override fun onResume() {
        super.onResume()
        binding.dialer.setListener(this)
    }

    override fun onStop() {
        super.onStop()
        binding.dialer.removeListener()
    }
}