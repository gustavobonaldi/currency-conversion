package br.com.bonaldi.currency.conversion.utils.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.bonaldi.currency.conversion.utils.R

@Composable
fun RoundedNumber(
    modifier: Modifier = Modifier,
    text: String = "1",
) {
    val borderColor = colorResource(id = R.color.roundedNumberBorderColor)
    Column(modifier) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = borderColor,
                        radius = this.size.maxDimension,
                        style = Stroke(
                            width = 2.dp.toPx(),
                        )
                    )
                },
            text = text,
            fontSize = 14.sp
        )
    }
}