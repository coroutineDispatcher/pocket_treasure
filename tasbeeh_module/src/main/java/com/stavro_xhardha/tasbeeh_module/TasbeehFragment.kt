package com.stavro_xhardha.tasbeeh_module


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_tasbeeh.*

class TasbeehFragment : Fragment() {

    private val tasbeehViewModel: TasbeehViewModel by viewModels()
    private lateinit var adapter: TasbeehAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tasbeeh, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        observeTheLiveData()
        //todo refactor when breaking the monolith
    }

    fun initializeComponents() {
        adapter = TasbeehAdapter()
        rvTasbeeh.adapter = adapter
    }

    fun observeTheLiveData() {
        tasbeehViewModel.tasbeehList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.reset_counting, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            tasbeehViewModel.initList()
            adapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}