package br.com.bonaldi.currency.conversion.utils.customcomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.bonaldi.currency.conversion.utils.R
import br.com.bonaldi.currency.conversion.utils.composable.RoundedNumber

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
    CardCurrency(
        modifier = modifier,
        roundedNumber = "2",
        descriptionText = "Brasil",
        selectedCountryImageRes = R.drawable.flag_brl
    )
}

@Composable
fun CardCurrency(
    modifier: Modifier = Modifier,
    roundedNumber: String,
    descriptionText: String? = null,
    selectedCountryImageRes: Int? = null
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.transparent_blue))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RoundedNumber(modifier, roundedNumber)
        Row(
            Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp),
                text = descriptionText ?: stringResource(id = R.string.select_another_currency),
                color = Color.White,
                style = baseTypography.bodyMedium,
            )
            Image(
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .clip(CircleShape),
                painter = painterResource(id = selectedCountryImageRes ?: R.drawable.globe),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}