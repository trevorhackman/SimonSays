package hackman.trevor.copycat

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    @get:LayoutRes
    abstract val layout: Int

    private val requireContext: Context by lazy {
        requireContext()
    }

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    abstract override fun onViewCreated(view: View, savedInstanceState: Bundle?)

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}
