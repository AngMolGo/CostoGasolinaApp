package com.mexiti.costogasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mexiti.costogasolina.ui.theme.CostoGasolinaTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CostoGasolinaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CostGasLayout()
                }
            }
        }
    }
}

@Composable
fun CostGasLayout() {
    var precioLitroEntrada by remember { mutableStateOf("") }
    var cantLitrosEntrada by remember { mutableStateOf("") }
    var propinaEntrada by remember { mutableStateOf("") }
    var darPropina by remember { mutableStateOf(false) }

    val precioLitro = precioLitroEntrada.toDoubleOrNull() ?: 0.0
    val cantLitros = cantLitrosEntrada.toDoubleOrNull() ?: 0.0
    val propina = propinaEntrada.toDoubleOrNull() ?: 0.0
    val total = calcularMonto(precioLitro, cantLitros, darPropina, propina)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(15.dp)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calcular_monto),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.ingresar_campos),
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Justify
        )

        EditNumberField(
            label = R.string.ingresa_gasolina,
            leadingIcon = R.drawable.money_gas,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = precioLitroEntrada,
            onValueChanged = { precioLitroEntrada = it }
        )

        EditNumberField(
            label = R.string.litros,
            leadingIcon = R.drawable.gas_station,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = cantLitrosEntrada,
            onValueChanged = { cantLitrosEntrada = it }
        )

        EditNumberField(
            label = R.string.propina,
            leadingIcon = R.drawable.wallet,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = propinaEntrada,
            onValueChanged = { propinaEntrada = it }
        )

        AddTip(
            darPropina = darPropina,
            onTipCheckedChange = { darPropina = it }
        )

        Text(
            text = stringResource(R.string.monto_total, total),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardsOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(text = stringResource(id = label)) },
        value = value,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null
            )
        },
        keyboardOptions = keyboardsOptions,
        modifier = modifier.fillMaxWidth(),
        onValueChange = onValueChanged
    )
}

@Composable
fun AddTip(
    darPropina: Boolean,
    onTipCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.agregar_propina),
            modifier = Modifier.padding(20.dp)
        )
        Switch(
            checked = darPropina,
            onCheckedChange = onTipCheckedChange
        )
    }
}

private fun calcularMonto(
    precio: Double,
    cantLitros: Double,
    darPropina: Boolean,
    propina: Double
): String {
    var monto = precio * cantLitros
    if (darPropina) {
        monto += propina
    }
    return NumberFormat.getCurrencyInstance().format(monto)
}

@Preview(showBackground = true)
@Composable
fun CostGasLayoutPreview() {
    CostoGasolinaTheme {
        CostGasLayout()
    }
}
