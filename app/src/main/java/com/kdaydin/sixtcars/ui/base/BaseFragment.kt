package com.kdaydin.sixtcars.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getViewModelType(): VM

    var viewModel: VM? = null
    var binding: DB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel == null || viewModel !is VM) {
            viewModel = getViewModelType()
        }
        viewModel?.onCreate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as BaseViewModel).let { vmod ->
            if (!vmod.state.hasObservers())
                vmod.state.observe(viewLifecycleOwner, Observer {
                    onStateChanged(vmod.state.value)
                })
        }
    }

    open fun onStateChanged(state: VMState?) {
        throw Exception("FATAL ERROR: State has changed but onStateChanged is not overridden")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        }
        binding!!.lifecycleOwner = this
        binding?.setVariable(BR.viewModel, viewModel)
        viewModel?.onCreateView()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel?.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel?.onStart()
    }

    fun getBaseActivity(): BaseActivity<*, *>? = activity as BaseActivity<*, *>?
}