package hackman.trevor.copycat.ui.remove_ads

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.pixelTextSize
import hackman.trevor.tlibrary.billing.Product
import kotlin.properties.Delegates

class RemoveAdsProductButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel
    private var product: Product by Delegates.notNull()

    init {
        typeface = Typeface.DEFAULT_BOLD
        pixelTextSize = displayMinimum() * .045f
        background = getDrawable(R.drawable.bordered_rectangle_light)
        setTextColor(getColor(R.color.Black))
    }

    fun setup(removeAdsViewModel: RemoveAdsViewModel, product: Product) {
        this.removeAdsViewModel = removeAdsViewModel
        this.product = product
        setOnClickListener()
    }

    private fun setOnClickListener() = setOnClickListener {
        removeAdsViewModel.productClicked(product)
    }
}
