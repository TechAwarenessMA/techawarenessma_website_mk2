package com.techawarenessma.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.techawarenessma.app.certification.CertificationStore

class TaaApplication : Application(), SingletonImageLoader.Factory {

    val certificationStore: CertificationStore by lazy { CertificationStore(this) }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context).crossfade(true).build()
}
