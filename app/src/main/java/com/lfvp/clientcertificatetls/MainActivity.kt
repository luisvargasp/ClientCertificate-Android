package com.lfvp.clientcertificatetls

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.security.KeyChain
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lfvp.clientcertificatetls.ui.theme.ClientCertificateTLSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClientCertificateTLSTheme {
                val viewmodel: MainViewmodel = hiltViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val coroutineScope = rememberCoroutineScope()
                    val showMessage = viewmodel.showMessage

                    LaunchedEffect(showMessage) {
                        showMessage?.let { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            viewmodel.resetOneTimeEvents()

                        }
                    }
                    Screen(viewmodel.loading, viewmodel.responseCode, {
                        viewmodel.testUnauthenticated()

                    }, {

                        viewmodel.testWithAssetsCertificate()


                    }, {
                        KeyChain.choosePrivateKeyAlias(
                            context as Activity,
                            { p0 ->
                                if (p0 == null) {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "No certificates in system  or no certificate chosen ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewmodel.cleanResult()
                                    }
                                }
                                    viewmodel.testWithSystemCertificate(p0)


                            },
                            null,
                            null,
                            null,
                            -1,
                            null
                        )

                    })


                }
            }
        }
    }
}


@Composable
fun Screen(
    loading: Boolean = false, responseCode: Int? = null,

    firstButton: () -> Unit, secondButton: () -> Unit,
    thirdButton: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            TEST_URL, textDecoration = TextDecoration.Underline, style = TextStyle(
                color = Color.Blue,
                fontSize = 24.sp
            )
        )
        Spacer(modifier = Modifier.size(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(72.dp))

            } else {
                Button(onClick = {
                    firstButton()


                }) {
                    Text("Test with no certificate", textAlign = TextAlign.Center)
                }
                Button(onClick = {
                    secondButton()
                }) {
                    Text(
                        "Test with  certificate file  in  assets folder",
                        textAlign = TextAlign.Center
                    )
                }
                Button(onClick = {
                    thirdButton()
                }) {
                    Text(
                        "Choose and test  with a certificate from system ",
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        Spacer(modifier = Modifier.size(32.dp))
        AnimatedContent(targetState = responseCode, label = "value", transitionSpec = {
            (fadeIn(animationSpec = tween(500, delayMillis = 100)) +
                    scaleIn(initialScale = 0.5f, animationSpec = tween(500, delayMillis = 100)))
                .togetherWith(fadeOut(animationSpec = tween(1000)))
        }) {
            it?.let {
                Text(
                    "Response StatusCode : $it  ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    fontSize = 24.sp
                )
            }
        }


    }

}

@Preview(showSystemUi = true)
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ScreenPreview() {
    ClientCertificateTLSTheme {
        Screen(false, 200, {}, {}, {})
    }


}

