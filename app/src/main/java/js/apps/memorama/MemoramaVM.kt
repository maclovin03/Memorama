package js.apps.memorama

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemoramaState(
    val cartas: List<Carta> = emptyList(),
    val scorePlayer1: Int = 0,
    val scorePlayer2: Int = 0
)
class MemoramaVM : ViewModel() {

    private val _memoramaState = MutableStateFlow(MemoramaState())
    val memoramaState = _memoramaState

    private val _turn = MutableStateFlow(0.0)
    val turn = _turn.asStateFlow()

    private val _jugador = MutableStateFlow("Jugador 1")
    val jugador = _jugador.asStateFlow()

    private val _currentIndex = MutableStateFlow<Int?>(null)

    init {
        val cartas = mutableListOf<Carta>()
        val randomNumberList = (0..11).shuffled().take(8)
        randomNumberList.forEach {
            cartas.add(MemoramaProvider().cartas[it])
        }
        val duplicateCartas = cartas.map {
            it.copy(id = it.id + 12)
        }
        _memoramaState.update {

            it.copy(cartas = cartas + duplicateCartas)
        }
        Log.d("CARTAS", memoramaState.value.cartas.toString())
    }

    fun voltearCarta(carta: Carta) {


        if (_turn.value <= 1) {
            _jugador.update { "Jugador 1" }
            _turn.update {
                it + 0.5
            }
            if (_turn.value == 1.0) {
                _turn.update {
                    2.0
                }
            }
        } else {
            _jugador.update { "Jugador 2" }
            _turn.update {
                it - 0.5
            }
            if (_turn.value == 1.0) {
                _turn.update {
                    0.0
                }

            }
        }
            val newCard = carta.copy(volteada = !carta.volteada)
            val cartas = _memoramaState.value.cartas.toMutableList()
            val index = cartas.indexOf(carta)
            cartas[index] = newCard
            _memoramaState.update {
                it.copy(cartas = cartas)
            }
            if (_currentIndex.value != null) {
                if (carta.pais == cartas[_currentIndex.value!!].pais) {
                    _memoramaState.update {
                        it.copy(scorePlayer1 = it.scorePlayer1 + 1)
                    }
                    _currentIndex.update {
                        null
                    }
                } else {


                    Log.d("INDEX", _currentIndex.value.toString())
                    viewModelScope.launch {
                        delay(2000)
                        cartas[index] = carta.copy(volteada = false)
                        cartas[_currentIndex.value!!] =
                            cartas[_currentIndex.value!!].copy(volteada = false)
                        _memoramaState.update {
                            it.copy(cartas = cartas)
                        }

                        _currentIndex.update {
                            null
                        }
                    }

                }
            } else {
                val i = cartas.indexOf(carta)
                Log.d("INDEX", i.toString())
                _currentIndex.update {

                    index
                }

            }

        }


    }
