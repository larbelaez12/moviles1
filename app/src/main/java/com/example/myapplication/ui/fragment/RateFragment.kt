package com.example.myapplication.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.ui.screens.RateScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class RateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyApplicationTheme {
                    RateScreen(
                        onBack = { findNavController().popBackStack() },
                        onOpenStore = { openStore() }
                    )
                }
            }
        }
    }

    private fun openStore() {
        val url = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}


