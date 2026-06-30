package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.ui.screens.InstructionsScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.AppViewModel

class InstructionsFragment : Fragment() {
    private val viewModel: AppViewModel by activityViewModels()
    private var resumeAudio = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resumeAudio = viewModel.isAudioOn.value
        if (resumeAudio) {
            viewModel.pauseAudioTemporarily()
        }
    }

    override fun onDestroy() {
        if (resumeAudio) {
            viewModel.resumeAudioIfOn()
        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyApplicationTheme {
                    InstructionsScreen(
                        onBack = { findNavController().popBackStack(R.id.homeFragment, false) }
                    )
                }
            }
        }
    }
}


