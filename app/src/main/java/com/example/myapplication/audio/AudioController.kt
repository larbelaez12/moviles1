package com.example.myapplication.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class AudioController {

    private val sampleRate = 44100
    private var backgroundTrack: AudioTrack? = null
    private var backgroundJob: Job? = null
    private var loopFrameCount: Int = 0
    private var pausedFramePosition: Int = 0

    // -------------------------------------------------------
    //  Melodía: escala pentatónica en Do mayor, loop de ~4 s
    //  Notas: C4 D4 E4 G4 A4 G4 E4 D4  (8 negras a 120 BPM)
    // -------------------------------------------------------
    private val melody = floatArrayOf(
        261.63f, 293.66f, 329.63f, 392.00f,
        440.00f, 392.00f, 329.63f, 293.66f
    )

    private fun buildMelodyLoop(): ShortArray {
        val bpm = 120
        val beatSamples = (60.0 / bpm * sampleRate).toInt()   // muestras por negra
        val totalSamples = beatSamples * melody.size
        val buf = ShortArray(totalSamples)

        for ((noteIdx, freq) in melody.withIndex()) {
            val offset = noteIdx * beatSamples
            for (s in 0 until beatSamples) {
                val t = s.toDouble() / sampleRate
                // ADSR simple: ataque 5%, decaimiento 10%, sostenido 75%, release 10%
                val env = when {
                    s < beatSamples * 0.05 -> s / (beatSamples * 0.05)
                    s < beatSamples * 0.15 -> 1.0 - 0.3 * (s - beatSamples * 0.05) / (beatSamples * 0.10)
                    s < beatSamples * 0.90 -> 0.70
                    else -> 0.70 * (beatSamples - s) / (beatSamples * 0.10)
                }
                // fundamental + segunda armónica suave
                val wave = sin(2.0 * PI * freq * t) * 0.7 +
                           sin(2.0 * PI * freq * 2.0 * t) * 0.15
                buf[offset + s] = (wave * env * Short.MAX_VALUE * 0.45).toInt().toShort()
            }
        }
        return buf
    }

    fun startBackground(scope: CoroutineScope) {
        // Si ya hay track creado, solo lo reanuda y evita reconstrucción.
        backgroundTrack?.let {
            if (it.playState != AudioTrack.PLAYSTATE_PLAYING) it.play()
            return
        }

        if (backgroundJob?.isActive == true) return

        backgroundJob = scope.launch(Dispatchers.Default) {
            val loopBuf = buildMelodyLoop()
            loopFrameCount = loopBuf.size

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(loopBuf.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(loopBuf, 0, loopBuf.size)
            track.setLoopPoints(0, loopBuf.size, -1)   // loop infinito
            track.setVolume(0.5f)
            track.play()
            backgroundTrack = track
            backgroundJob = null
        }
    }

    fun stopBackground() {
        backgroundJob?.cancel()
        backgroundJob = null
        backgroundTrack?.pause()
        backgroundTrack?.flush()
        backgroundTrack?.release()
        backgroundTrack = null
        loopFrameCount = 0
        pausedFramePosition = 0
    }

    fun pauseBackground() {
        backgroundTrack?.let { track ->
            if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                pausedFramePosition = track.playbackHeadPosition
                track.pause()
            }
        }
    }

    fun resumeBackground() {
        backgroundTrack?.let { track ->
            if (track.playState != AudioTrack.PLAYSTATE_PLAYING) {
                val seekPosition = if (loopFrameCount > 0) {
                    pausedFramePosition % loopFrameCount
                } else {
                    pausedFramePosition
                }
                runCatching { track.setPlaybackHeadPosition(seekPosition.coerceAtLeast(0)) }
                track.play()
            }
        }
    }

    // ---- Sonido de giro: whoosh suave con sweep de frecuencia ----
    fun playSpin(durationMs: Long) {
        val totalSamples = (durationMs * sampleRate / 1000L).toInt().coerceAtLeast(sampleRate / 4)
        val buffer = ShortArray(totalSamples)

        for (i in buffer.indices) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / totalSamples

            val freqSweep = if (progress < 0.4)
                80.0 + progress * 600.0
            else
                80.0 + (1.0 - progress) * 400.0

            val env = exp(-progress * 2.5) * 0.8 + 0.2 * (1.0 - progress)

            val whoosh  = sin(2.0 * PI * freqSweep * t) * 0.5
            val shimmer = sin(2.0 * PI * freqSweep * 3.0 * t) * 0.15

            buffer[i] = ((whoosh + shimmer) * env * Short.MAX_VALUE * 0.55).toInt().toShort()
        }

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()
        track.setNotificationMarkerPosition(buffer.size - 1)
        track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(t: AudioTrack) { t.stop(); t.release() }
            override fun onPeriodicNotification(t: AudioTrack) = Unit
        })
    }
}
