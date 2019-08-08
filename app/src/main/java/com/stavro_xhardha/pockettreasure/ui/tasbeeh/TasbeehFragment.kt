package com.stavro_xhardha.pockettreasure.ui.tasbeeh


import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import kotlinx.android.synthetic.main.fragment_tasbeeh.*

class TasbeehFragment : BaseFragment() {

    private val tasbeehViewModel by lazy {
        ViewModelProviders.of(this).get(TasbeehViewModel::class.java)
    }
    private lateinit var adapter: TasbeehAdapter

    override fun initializeComponents() {
        adapter = TasbeehAdapter()
        rvTasbeeh.adapter = adapter
    }

    override fun performDi() {
    }

    override fun observeTheLiveData() {
        tasbeehViewModel.tasbeehList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tasbeeh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.reset, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            tasbeehViewModel.initList()
            adapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}