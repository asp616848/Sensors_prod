package io.sensor_prod.sensor.ui.pages.home

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddChart
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import io.sensify.sensor.R
import io.sensor_prod.sensor.ui.composables.isScrollingUp
import io.sensor_prod.sensor.ui.navigation.NavDirectionsApp
import io.sensor_prod.sensor.ui.pages.home.items.HomeSensorItem
import io.sensor_prod.sensor.ui.resource.values.JlResDimens
import io.sensor_prod.sensor.ui.resource.values.JlResShapes
import io.sensor_prod.sensor.ui.resource.values.JlResTxtStyles
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalTextApi::class,
    ExperimentalPagerApi::class, ExperimentalAnimationApi::class
)
@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun HomePage(
    modifier: Modifier = Modifier, navController: NavController? = null,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory()
    )
) {
    val context = LocalContext.current
    val camVM: CameraViewModel = viewModel(
        factory = CameraViewModel.Factory()
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

    LaunchedEffect(Unit) {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.fromOrderedList(
                listOf(Quality.FHD, Quality.HD, Quality.HIGHEST)
            ))
            .build()
        val videoCapture = VideoCapture.withOutput(recorder)

        // Initialize the ViewModel
        camVM.initialize(context, videoCapture)
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, videoCapture)
    }

    val lazyListState = rememberLazyListState()
//    val sensorsProvider = SensorsProviderComposable()
//    val sensors = remember { sensorsProvider }

    val sensorUiState = viewModel.mUiState.collectAsState()
//    var sensorUiState = viewModel.mUiCurrentSensorState.collectAsState()

    val isAtTop = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    val pagerState = rememberPagerState(
//        pageCount = 3,
    )


//    Log.d("HomePage", "sensor ${sensorsUiState.value.sensors}");

    Scaffold(topBar = {

        TopAppBar(

//            backgroundColor = Color.Transparent,
            colors = if (!isAtTop.value) TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), //Add your own color here, just to clarify.
            ) else TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent //Add your own color here, just to clarify.
            ),


            navigationIcon = {
                Box(Modifier.padding(horizontal = JlResDimens.dp20)) {
                    Image(
                        painterResource(id = R.drawable.pic_sensify_logo),
                        modifier = Modifier
                            .width(JlResDimens.dp32)
                            .height(JlResDimens.dp36),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            },
            title = {
                Text(
                    text = "ILGC",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = JlResTxtStyles.h4,
                    fontWeight = FontWeight(400),
                    modifier = modifier.fillMaxWidth(),
                )
            },
            actions = {
                Box(Modifier.padding(horizontal = JlResDimens.dp20)) {
                    Image(

                        painterResource(id = R.drawable.pic_sensify_logo),
                        modifier = Modifier
                            .alpha(0f)
                            .width(JlResDimens.dp32)
                            .height(JlResDimens.dp36),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            },

            )
    },
        floatingActionButton = {

            AnimatedVisibility(
                visible = lazyListState.isScrollingUp(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Column {
                    ToggleableFAB(viewModel)
                    // Video Recording Button
                    FloatingActionButton(
                        onClick = { camVM.startRecordingClips() },  // instead call toggleVideo recording for toggle functinoalities
                        shape = RoundedCornerShape(50),
                        containerColor = Color.Red,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Red.copy(alpha = 0.8f), Color.Red.copy(alpha = 0.5f))
                                ),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .border(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.3f))
                                ),
                                width = JlResDimens.dp1,
                                shape = RoundedCornerShape(50.dp)
                            )
                    ) {
                        Icon(Icons.Rounded.Videocam,"Record Video", tint = Color.White)
                    }
                }
            }
        }

    ) {

        LazyColumn(

            modifier = Modifier
                .consumeWindowInsets(it)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),

                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                        )
                    )
                ),
//                .fillMaxSize()
//                .background(JLThemeBase.colorPrimary10)
//                .consumedWindowInsets ,
            contentPadding = it,
            state = lazyListState
        ) {

            item {
                Spacer(modifier = JlResShapes.Space.H24)

            }
            // Header  GRAPH ON HOME REMOVED FOR SPEED
//            item {
//                Box(
//                    modifier = Modifier.padding(
//                        start = JlResDimens.dp32,
//                        end = JlResDimens.dp32
//                    ),
//                ) {
//                    HomeHeader(
//                        sensorUiState.value.currentSensor,
//                        totalActive = sensorUiState.value.activeSensorCounts,
//                        onClickArrow = { isLeft ->
//
//
//                            var currentPage = pagerState.currentPage
//                            var totalPage = pagerState.pageCount
//
//                            if (!isLeft && currentPage + 1 < totalPage) {
//                                coroutineScope.launch {
//                                    pagerState.animateScrollToPage(currentPage + 1)
//                                }
//                            } else if (isLeft && currentPage > 0 && totalPage > 0) {
//                                coroutineScope.launch {
//                                    pagerState.animateScrollToPage(currentPage - 1)
//                                }
//                            }
//                        }
//                    )
//                }
//            }
//            // Plotting area
//            item {
////                Spacer(modifier = Modifier.height(JlResDimens.dp350))
//
//                HomeSensorGraphPager(viewModel = viewModel, pagerState = pagerState)
//
//            }

            // Available Sensors
            item {
                Box(
                    modifier = Modifier
                        .padding(
                            start = JlResDimens.dp40, end = JlResDimens.dp32,
                            top = JlResDimens.dp12, bottom = JlResDimens.dp16
                        ),
                ) {
                    Text(
                        text = "Available Sensors",
                        fontSize = JlResDimens.sp16,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }


            items(viewModel.mSensorsList.windowed(2, 2, true)) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = JlResDimens.dp32)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,


                        ) {

                        for (i in item.indices) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
//                                    .fillParentMaxWidth(0.5f)
//                                    .padding(bottom = JlResDimens.dp8)
                                /*.clickable(
                                    enabled = true,
                                    onClickLabel = "Card Click",
                                    onClick = {
                                        navController?.navigate(NavDirectionsLabs.DetailPage.route)
                                    }
                                )*/

                            ) {
                                HomeSensorItem(
                                    modelSensor = item[i],
                                    /* se = item[i].sensorName,
                                     sensorValue = item[i].sensorValue,
                                     sensorUnit = item[i].sensorUnit,
                                     sensorIcon = item[i].sensorIcon*/

                                    onCheckChange = { type: Int, isChecked: Boolean ->
                                        viewModel.onSensorChecked(type, isChecked)
                                    },
                                    onClick = {
                                        navController?.navigate("${NavDirectionsApp.SensorDetailPage.route}/${it}")
                                    }
                                )
                            }

                            if (i < item.size - 1) {
                                Spacer(modifier = Modifier.width(JlResDimens.dp8))
                            }
                        }
                        if (item.size % 2 != 0) {
                            Spacer(modifier = Modifier.width(JlResDimens.dp8))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(JlResDimens.dp8))

            }

            item { Spacer(modifier = Modifier.height(JlResDimens.dp16)) }
//            }
        }

    }
}

@Composable
fun Factory() {
    TODO("Not yet implemented")
}

@Composable
fun ToggleableFAB(viewModel: HomeViewModel) {
    var isLogging by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            isLogging = !isLogging  // Toggle state
            viewModel.toggleCsvLogging()
        },
        shape = RoundedCornerShape(50),
        containerColor = if (isLogging) Color(0xFF800080) else Color.Yellow, // Purple when active
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        if (isLogging) Color.Magenta.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.8f),
                        if (isLogging) Color.Magenta.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                brush = Brush.verticalGradient(
                    listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.3f))
                ),
                width = JlResDimens.dp1,
                shape = RoundedCornerShape(50.dp)
            )
    ) {
        Icon(Icons.Rounded.AddChart, "Record CSV", tint = Color.White)
    }
}
private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }