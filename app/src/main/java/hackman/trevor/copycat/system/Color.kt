package hackman.trevor.copycat.system

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

// Makes an AARRGGBB color brighter by a given percentage [0.0,1.0]
fun brightenColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) percentage: Double
): Int {
    val a: Int = Color.alpha(color)
    var r: Int = Color.red(color)
    var g: Int = Color.green(color)
    var b: Int = Color.blue(color)
    r += ((0xff - r) * percentage).toInt()
    g += ((0xff - g) * percentage).toInt()
    b += ((0xff - b) * percentage).toInt()
    return Color.argb(a, r, g, b)
}

// Common Colors - Taken from Google Color Guide
class Color {
    companion object {
        const val Transparent = 0x00000000
        const val Shade1 = 0x11000000
        const val Shade2 = 0x22000000
        const val Shade3 = 0x33000000
        const val Shade4 = 0x44000000
        const val Shade5 = 0x55000000
        const val Shade6 = 0x66000000
        const val Shade7 = 0x77000000
        const val Shade8 = 0x88000000
        const val Shade9 = 0x99000000
        const val Shade10 = 0xAA000000
        const val Shade11 = 0xBB000000
        const val Shade12 = 0xCC000000
        const val Shade13 = 0xDD000000
        const val Shade14 = 0xEE000000
        const val Shade15 = 0xFF000000

        const val Red50 = 0xFFFFEBEE
        const val Red100 = 0xFFFFCDD2
        const val Red200 = 0xFFEF9A9A
        const val Red300 = 0xFFE57373
        const val Red400 = 0xFFEF5350
        const val Red500 = 0xFFF44336
        const val Red600 = 0xFFE53935
        const val Red700 = 0xFFD32F2F
        const val Red800 = 0xFFC62828
        const val Red900 = 0xFFB71C1C
        const val RedAccent100 = 0xFFFF8A80
        const val RedAccent200 = 0xFFFF5252
        const val RedAccent400 = 0xFFFF1744
        const val RedAccent700 = 0xFFD50000

        const val Pink50 = 0xFFFCE4EC
        const val Pink100 = 0xFFF8BBD0
        const val Pink200 = 0xFFF48FB1
        const val Pink300 = 0xFFF06292
        const val Pink400 = 0xFFEC407A
        const val Pink500 = 0xFFE91E63
        const val Pink600 = 0xFFD81B60
        const val Pink700 = 0xFFC2185B
        const val Pink800 = 0xFFAD1457
        const val Pink900 = 0xFF880E4F
        const val PinkAccent100 = 0xFFFF80AB
        const val PinkAccent200 = 0xFFFF4081
        const val PinkAccent400 = 0xFFF50057
        const val PinkAccent700 = 0xFFC51162

        const val Purple50 = 0xFFF3E5F5
        const val Purple100 = 0xFFE1BEE7
        const val Purple200 = 0xFFCE93D8
        const val Purple300 = 0xFFBA68C8
        const val Purple400 = 0xFFAB47BC
        const val Purple500 = 0xFF9C27B0
        const val Purple600 = 0xFF8E24AA
        const val Purple700 = 0xFF7B1FA2
        const val Purple800 = 0xFF6A1B9A
        const val Purple900 = 0xFF4A148C
        const val PurpleAccent100 = 0xFFC51162
        const val PurpleAccent200 = 0xFFE040FB
        const val PurpleAccent400 = 0xFFD500F9
        const val PurpleAccent700 = 0xFFAA00FF

        const val DeepPurple50 = 0xFFEDE7F6
        const val DeepPurple100 = 0xFFD1C4E9
        const val DeepPurple200 = 0xFFB39DDB
        const val DeepPurple300 = 0xFF9575CD
        const val DeepPurple400 = 0xFF7E57C2
        const val DeepPurple500 = 0xFF673AB7
        const val DeepPurple600 = 0xFF5E35B1
        const val DeepPurple700 = 0xFF512DA8
        const val DeepPurple800 = 0xFF4527A0
        const val DeepPurple900 = 0xFF311B92
        const val DeepPurpleAccent100 = 0xFFB388FF
        const val DeepPurpleAccent200 = 0xFF7C4DFF
        const val DeepPurpleAccent400 = 0xFF651FFF
        const val DeepPurpleAccent700 = 0xFF6200EA

        const val Indigo50 = 0xFFE8EAF6
        const val Indigo100 = 0xFFC5CAE9
        const val Indigo200 = 0xFF9FA8DA
        const val Indigo300 = 0xFF7986CB
        const val Indigo400 = 0xFF5C6BC0
        const val Indigo500 = 0xFF3F51B5
        const val Indigo600 = 0xFF3949AB
        const val Indigo700 = 0xFF303F9F
        const val Indigo800 = 0xFF283593
        const val Indigo900 = 0xFF1A237E
        const val IndigoAccent100 = 0xFF8C9EFF
        const val IndigoAccent200 = 0xFF536DFE
        const val IndigoAccent300 = 0xFF3D5AFE
        const val IndigoAccent700 = 0xFF304FFE

        const val Blue50 = 0xFFE3F2FD
        const val Blue100 = 0xFFBBDEFB
        const val Blue200 = 0xFF90CAF9
        const val Blue300 = 0xFF64B5F6
        const val Blue400 = 0xFF42A5F5
        const val Blue500 = 0xFF2196F3
        const val Blue600 = 0xFF1E88E5
        const val Blue700 = 0xFF1976D2
        const val Blue800 = 0xFF1565C0
        const val Blue900 = 0xFF0D47A1
        const val BlueAccent100 = 0xFF82B1FF
        const val BlueAccent200 = 0xFF448AFF
        const val BlueAccent400 = 0xFF2979FF
        const val BlueAccent700 = 0xFF2962FF

        const val LightBlue50 = 0xFFE1F5FE
        const val LightBlue100 = 0xFFB3E5FC
        const val LightBlue200 = 0xFF81D4FA
        const val LightBlue300 = 0xFF4FC3F7
        const val LightBlue400 = 0xFF29B6F6
        const val LightBlue500 = 0xFF03A9F4
        const val LightBlue600 = 0xFF039BE5
        const val LightBlue700 = 0xFF0288D1
        const val LightBlue800 = 0xFF0277BD
        const val LightBlue900 = 0xFF01579B
        const val LightBlueAccent100 = 0xFF80D8FF
        const val LightBlueAccent200 = 0xFF40C4FF
        const val LightBlueAccent400 = 0xFF00B0FF
        const val LightBlueAccent700 = 0xFF0091EA

        const val Cyan50 = 0xFFE0F7FA
        const val Cyan100 = 0xFFB2EBF2
        const val Cyan200 = 0xFF80DEEA
        const val Cyan300 = 0xFF4DD0E1
        const val Cyan400 = 0xFF26C6DA
        const val Cyan500 = 0xFF00BCD4
        const val Cyan600 = 0xFF00ACC1
        const val Cyan700 = 0xFF0097A7
        const val Cyan800 = 0xFF00838F
        const val Cyan900 = 0xFF006064
        const val CyanAccent100 = 0xFF84FFFF
        const val CyanAccent200 = 0xFF18FFFF
        const val CyanAccent400 = 0xFF00E5FF
        const val CyanAccent700 = 0xFF00B8D4

        const val Teal50 = 0xFFE0F2F1
        const val Teal100 = 0xFFB2DFDB
        const val Teal200 = 0xFF80CBC4
        const val Teal300 = 0xFF4DB6AC
        const val Teal400 = 0xFF26A69A
        const val Teal500 = 0xFF009688
        const val Teal600 = 0xFF00897B
        const val Teal700 = 0xFF00796B
        const val Teal800 = 0xFF00695C
        const val Teal900 = 0xFF004D40
        const val TealAccent100 = 0xFFA7FFEB
        const val TealAccent200 = 0xFF64FFDA
        const val TealAccent400 = 0xFF1DE9B6
        const val TealAccent700 = 0xFF00BFA5

        const val Green50 = 0xFFE8F5E9
        const val Green100 = 0xFFC8E6C9
        const val Green200 = 0xFFA5D6A7
        const val Green300 = 0xFF81C784
        const val Green400 = 0xFF66BB6A
        const val Green500 = 0xFF4CAF50
        const val Green600 = 0xFF43A047
        const val Green700 = 0xFF388E3C
        const val Green800 = 0xFF2E7D32
        const val Green900 = 0xFF1B5E20
        const val GreenAccent100 = 0xFFB9F6CA
        const val GreenAccent200 = 0xFF69F0AE
        const val GreenAccent400 = 0xFF00E676
        const val GreenAccent700 = 0xFF00C853

        const val LightGreen50 = 0xFFF1F8E9
        const val LightGreen100 = 0xFFDCEDC8
        const val LightGreen200 = 0xFFC5E1A5
        const val LightGreen300 = 0xFFAED581
        const val LightGreen400 = 0xFF9CCC65
        const val LightGreen500 = 0xFF8BC34A
        const val LightGreen600 = 0xFF7CB342
        const val LightGreen700 = 0xFF689F38
        const val LightGreen800 = 0xFF558B2F
        const val LightGreen900 = 0xFF33691E
        const val LightGreenAccent100 = 0xFFCCFF90
        const val LightGreenAccent200 = 0xFFB2FF59
        const val LightGreenAccent400 = 0xFF76FF03
        const val LightGreenAccent700 = 0xFF64DD17

        const val Lime50 = 0xFFF9FBE7
        const val Lime100 = 0xFFF0F4C3
        const val Lime200 = 0xFFE6EE9C
        const val Lime300 = 0xFFDCE775
        const val Lime400 = 0xFFD4E157
        const val Lime500 = 0xFFCDDC39
        const val Lime600 = 0xFFC0CA33
        const val Lime700 = 0xFFAFB42B
        const val Lime800 = 0xFF9E9D24
        const val Lime900 = 0xFF827717
        const val LimeAccent100 = 0xFFF4FF81
        const val LimeAccent200 = 0xFFEEFF41
        const val LimeAccent400 = 0xFFC6FF00
        const val LimeAccent700 = 0xFFAEEA00

        const val Yellow50 = 0xFFFFFDE7
        const val Yellow100 = 0xFFFFF9C4
        const val Yellow200 = 0xFFFFF59D
        const val Yellow300 = 0xFFFFF176
        const val Yellow400 = 0xFFFFEE58
        const val Yellow500 = 0xFFFFEB3B
        const val Yellow600 = 0xFFFDD835
        const val Yellow700 = 0xFFFBC02D
        const val Yellow800 = 0xFFF9A825
        const val Yellow900 = 0xFFF57F17
        const val YellowAccent100 = 0xFFFFFF8D
        const val YellowAccent200 = 0xFFFFFF00
        const val YellowAccent400 = 0xFFFFEA00
        const val YellowAccent700 = 0xFFFFD600

        const val Amber50 = 0xFFFFF8E1
        const val Amber100 = 0xFFFFECB3
        const val Amber200 = 0xFFFFE082
        const val Amber300 = 0xFFffd54f
        const val Amber400 = 0xFFFFCA28
        const val Amber500 = 0xFFFFC107
        const val Amber600 = 0xFFFFB300
        const val Amber700 = 0xFFFFA000
        const val Amber800 = 0xFFFF8F00
        const val Amber900 = 0xFFFF6F00
        const val AmberAccent100 = 0xFFFFE57F
        const val AmberAccent200 = 0xFFFFD740
        const val AmberAccent400 = 0xFFFFC400
        const val AmberAccent700 = 0xFFFFAB00

        const val Orange50 = 0xFFFFF3E0
        const val Orange100 = 0xFFFFE0B2
        const val Orange200 = 0xFFFFCC80
        const val Orange300 = 0xFFFFB74D
        const val Orange400 = 0xFFFFA726
        const val Orange500 = 0xFFFF9800
        const val Orange600 = 0xFFFB8C00
        const val Orange700 = 0xFFF57C00
        const val Orange800 = 0xFFEF6C00
        const val Orange900 = 0xFFE65100
        const val OrangeAccent100 = 0xFFFFD180
        const val OrangeAccent200 = 0xFFFFAB40
        const val OrangeAccent400 = 0xFFFF9100
        const val OrangeAccent700 = 0xFFFF6D00

        const val DeepOrange50 = 0xFFFBE9E7
        const val DeepOrange100 = 0xFFFFCCBC
        const val DeepOrange200 = 0xFFFFAB91
        const val DeepOrange300 = 0xFFFF8A65
        const val DeepOrange400 = 0xFFFF7043
        const val DeepOrange500 = 0xFFFF5722
        const val DeepOrange600 = 0xFFF4511E
        const val DeepOrange700 = 0xFFE64A19
        const val DeepOrange800 = 0xFFD84315
        const val DeepOrange900 = 0xFFBF360C
        const val DeepOrangeAccent100 = 0xFFFF9E80
        const val DeepOrangeAccent200 = 0xFFFF6E40
        const val DeepOrangeAccent400 = 0xFFFF3D00
        const val DeepOrangeAccent700 = 0xFFDD2C00

        const val Brown50 = 0xFFEFEBE9
        const val Brown100 = 0xFFD7CCC8
        const val Brown200 = 0xFFBCAAA4
        const val Brown300 = 0xFFA1887F
        const val Brown400 = 0xFF8D6E63
        const val Brown500 = 0xFF795548
        const val Brown600 = 0xFF6D4C41
        const val Brown700 = 0xFF5D4037
        const val Brown800 = 0xFF4E342E
        const val Brown900 = 0xFF3E2723

        const val Grey50 = 0xFFFAFAFA
        const val Grey100 = 0xFFF5F5F5
        const val Grey200 = 0xFFEEEEEE
        const val Grey300 = 0xFFE0E0E0
        const val Grey400 = 0xFFBDBDBD
        const val Grey500 = 0xFF9E9E9E
        const val Grey600 = 0xFF757575
        const val Grey700 = 0xFF616161
        const val Grey800 = 0xFF424242
        const val Grey900 = 0xFF212121

        const val BlueGrey50 = 0xFFECEFF1
        const val BlueGrey100 = 0xFFCFD8DC
        const val BlueGrey200 = 0xFFB0BEC5
        const val BlueGrey300 = 0xFF90A4AE
        const val BlueGrey400 = 0xFF78909C
        const val BlueGrey500 = 0xFF607D8B
        const val BlueGrey600 = 0xFF546E7A
        const val BlueGrey700 = 0xFF455A64
        const val BlueGrey800 = 0xFF37474F
        const val BlueGrey900 = 0xFF263238

        const val Black = 0xff000000
        const val White = 0xffffffff
    }
}
