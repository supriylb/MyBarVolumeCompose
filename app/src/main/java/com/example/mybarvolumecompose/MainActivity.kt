package com.example.mybarvolumecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybarvolumecompose.ui.theme.MyBarVolumeComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyBarVolumeComposeTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var lengthInput by rememberSaveable { mutableStateOf("") }
    var widthInput by rememberSaveable { mutableStateOf("") }
    var heightInput by rememberSaveable { mutableStateOf("") }
    var lengthError by rememberSaveable { mutableStateOf(false) }
    var widthError by rememberSaveable { mutableStateOf(false) }
    var heightError by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf("0") }

    val calculateVolume = {
        val length = lengthInput.toDoubleOrNull()
        val width = widthInput.toDoubleOrNull()
        val height = heightInput.toDoubleOrNull()

        lengthError = length == null
        widthError = width == null
        heightError = height == null

        if (length != null && width != null && height != null) {
            result = (length * width * height).toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = modifier,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VolumeInputField(
                label = R.string.label_length,
                value = lengthInput,
                onValueChange = { lengthInput = it; lengthError = false },
                isError = lengthError,
                imeAction = ImeAction.Next,
            )

            VolumeInputField(
                label = R.string.label_width,
                value = widthInput,
                onValueChange = { widthInput = it; widthError = false },
                isError = widthError,
                imeAction = ImeAction.Next,
            )

            VolumeInputField(
                label = R.string.label_height,
                value = heightInput,
                onValueChange = { heightInput = it; heightError = false },
                isError = heightError,
                imeAction = ImeAction.Done,
                onDone = { calculateVolume() },
            )

            Button(
                onClick = { calculateVolume() },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text(
                    text = stringResource(R.string.btn_calculate),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Card(
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.label_result),
                        style = MaterialTheme.typography.labelLarge,
                    )

                    Text(
                        text = result,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
fun VolumeInputField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        isError = isError,
        modifier = modifier
            .widthIn(max = 488.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
        singleLine = true,
        supportingText = {
            if (isError) {
                Text(
                    text = stringResource(R.string.error_field_required, stringResource(label)),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    MyBarVolumeComposeTheme {
        MainScreen()
    }
}
