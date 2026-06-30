package com.example.myapplication.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.AppViewModel

class HomeFragment : Fragment() {
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyApplicationTheme {
                    val isAudioOn = viewModel.isAudioOn.collectAsState().value
                    HomeScreen(
                        isAudioOn = isAudioOn,
                        onRate = { openStoreForRating() },
                        onToggleAudio = { viewModel.toggleAudio() },
                        onInstructions = { findNavController().navigate(R.id.instructionsFragment) },
                        onChallenges = { findNavController().navigate(R.id.challengesFragment) },
                        onShare = { shareApp() },
                        onPauseBackground = { viewModel.pauseAudioTemporarily() },
                        onResumeBackground = { viewModel.resumeAudioIfOn() },
                        onPlaySpinSound = { duration -> viewModel.playSpinSound(duration) },
                        loadRandomChallenge = { viewModel.getRandomChallengeText() },
                        loadRandomPokemonUrl = { viewModel.getRandomPokemonImage() }
                    )
                }
            }
        }
    }

    private fun openStoreForRating() {
        val appId = "com.nequi.MobileApp"
        val marketIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$appId".toUri())
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$appId&hl=es_419&gl=es".toUri()
        )

        try {
            startActivity(marketIntent)
        } catch (_: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    private fun shareApp() {
        val title = getString(R.string.share_app_title)
        val slogan = getString(R.string.share_app_slogan)
        val url = getString(R.string.share_app_url)
        val text = "$title\n$slogan\n$url"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }
}


