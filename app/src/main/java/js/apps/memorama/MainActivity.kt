package js.apps.memorama


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import js.apps.memorama.ui.theme.MemoramaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val memoramaVM by viewModels<MemoramaVM>()
            MemoramaTheme {
                GameScreen(memoramaVM)
            }
        }
    }

    @Composable
    fun GameScreen(memoramaVM: MemoramaVM) {

        val listaCartas by memoramaVM.memoramaState.collectAsState()
        var waitingTurn by remember { mutableStateOf(false) }

        val jugador by memoramaVM.jugador.collectAsState()
        Log.d("CARTAS", listaCartas.cartas.toString())

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (board, score, timer, restart, turno) = createRefs()
            Image(painter = painterResource(id = R.drawable.again), contentDescription = "",
                modifier = Modifier.constrainAs(restart){

                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)

                })

            Text(text = "Turno: $jugador", modifier = Modifier.constrainAs(turno){
                top.linkTo(parent.top, margin = 24.dp)
                start.linkTo(parent.start, margin = 16.dp)

            }, fontSize = 20.sp)
            Score(modifier = Modifier.constrainAs(score){
                top.linkTo(turno.bottom, margin = 36.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)

                width = Dimension.fillToConstraints
            }, scorePlayer1 = listaCartas.scorePlayer1, scorePlayer2 = listaCartas.scorePlayer2)

            Board(cartas = listaCartas.cartas, modifier = Modifier.constrainAs(board){
                top.linkTo(score.bottom, margin = 36.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }){
                memoramaVM.voltearCarta(it)
            }








        }
    }
}

@Composable
fun Board(cartas: List<Carta>, modifier: Modifier, onClick: (Carta) -> Unit){
    LazyVerticalGrid(columns = GridCells.Adaptive(80.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        items(cartas){ carta ->
            MemoramaCard(carta){

                if(carta.volteada) return@MemoramaCard

                onClick(carta)
            }
        }

    }
}

@Composable
fun MemoramaCard(carta: Carta, onClick: () -> Unit){
    Card(modifier = Modifier
        .height(120.dp)
        .width(60.dp), elevation = CardDefaults.cardElevation(8.dp), shape = RoundedCornerShape(8.dp),
        onClick = {
            onClick()
        }
    ) {
        if(carta.volteada){
            Text(text = carta.pais, modifier = Modifier.padding(8.dp))
        }

    }
}

@Composable
fun Score(modifier: Modifier, scorePlayer1: Int, scorePlayer2: Int){
    Card(modifier){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.Center){
            Text(text = "Jugador 1 $scorePlayer1 : $scorePlayer2 Jugador 2", fontSize = 20.sp)
        }
    }
}



