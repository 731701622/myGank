package com.wkx.gank.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wkx.gank.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.instance = this
        return binding.root
    }

}