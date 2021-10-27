package com.pirkovitaliysoft.notificationstest.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pirkovitaliysoft.notificationstest.presentation.viewModel.MainVM
import com.pirkovitaliysoft.notificationstest.databinding.FragmentTemplateBinding

private const val ARG_FRAGMENT_INDEX = "fragmentIndex"

class TemplateFragment : Fragment() {

    private var actualFragmentPosition: Int = 0

    private var _binding: FragmentTemplateBinding? = null
    private val binding get() = _binding!!

    private val vm: MainVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            actualFragmentPosition = it.getInt(ARG_FRAGMENT_INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val fragmentPositionForUser = actualFragmentPosition + 1
        binding.tvIndex.text = fragmentPositionForUser.toString()

        initializeButtonsListeners()

        vm.currentCount.observe(viewLifecycleOwner) { count ->
            binding.btRemove.visibility = if (count < 2) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        return binding.root
    }

    private fun initializeButtonsListeners() {
        binding.btAdd.setOnClickListener {
            vm.plusOne()
        }
        binding.btRemove.setOnClickListener {
            vm.minusOne()
        }

        binding.btCreateNotification.setOnClickListener {
            (context as Notificator).pushNotificationForFragment(actualFragmentPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentPosition: Int) =
            TemplateFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FRAGMENT_INDEX, fragmentPosition)
                }
            }
    }
}