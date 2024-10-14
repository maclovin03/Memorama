package js.apps.memorama

import androidx.compose.ui.graphics.ImageBitmap

data class Carta(
    val id: Int,
    val pais: String,
    val capital: String,
    val imagen: ImageBitmap? = null,
    val imagenVolteada: ImageBitmap? = null,
    val volteada: Boolean = false

)
