package com.example.disneylandattractions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.disneylandattractions.data.QueueTimes
import com.example.disneylandattractions.ui.theme.DisneylandAttractionsTheme


// Attraction class
data class Attraction(
    val name: String,
    val park : String,  // Disneyland Park or Disney California Adventure
    val season : String, // Halloween or Christmas
    val image : String
)


// List of attractions

val attractions = listOf(
    Attraction(
        "Space Mountain",
        "Disneyland Park",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/1600/900/75/dam/wdpro-assets/dlr/parks-and-tickets/attractions/disneyland/space-mountain/space-mountain-00.jpg?1747061483717"),

    Attraction(
        "Haunted Mansion",
        "Disneyland Park",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/dam/disneyland/attractions/haunted-mansion/hanted-mansion-exterior-16x9.jpg?1699631836595"),

    Attraction(
        "Haunted Mansion Holiday",
        "Disneyland Park",
        "Halloween",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/vision-dam/digital/parks-platform/parks-global-assets/disneyland/events/halloween/Spellbinding_Thrills-Haunted_Mansion_Holiday-16x9.jpg?2024-06-17T18:34:56+00:00"),

    Attraction(
        "It's a Small World",
        "Disneyland Park",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/1600/900/75/dam/disneyland/attractions/disneyland/its-a-small-world/small-world-exterior-16x9.jpg?1747179581005"),

    Attraction(
        "It's a Small World Holiday",
        "Disneyland Park",
        "Christmas",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/dam/wdpro-assets/dlr/parks-and-tickets/attractions/disneyland/its-a-small-world-holiday/its-a-small-world-holiday-bright-16x9.jpg?1699631876871"),

    Attraction(
        "Radiator Springs Racers",
        "Disney California Adventure",
        "All Year",
        "https://d23.com/app/uploads/2022/01/radiator-springs-racers.jpg"),


    Attraction(
        "Pirates of the Caribbean",
        "Disneyland Park",
        "All Year",
        "https://d23.com/app/uploads/2019/03/1180w-600h_031819_pirates-of-the-caribbean-fact-listicle.jpg"),

    Attraction(
        "Incredicoaster",
        "Disney California Adventure",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/dam/disneyland/destinations/disney-california-adventure/pixar-pier/incredicoaster-guests-16x9.jpg?1699631823973"),

    Attraction(
        "Goofy's Sky School",
        "Disney California Adventure",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/1600/900/75/dam/wdpro-assets/dlr/parks-and-tickets/attractions/disney-california-adventure/goofys-sky-school/goofys-sky-school-00.jpg?1721799816597"),


    Attraction(
        "Pixar Pal-A-Round",
        "Disney California Adventure",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/dam/disneyland/destinations/disney-california-adventure/pixar-pier/pixar-pal-around-night-16x9.jpg?1758287816671"),

    Attraction(
        "Toy Story Midway Mania!",
        "Disney California Adventure",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/vision-dam/digital/parks-platform/parks-global-assets/disneyland/attractions/toy-story-midway-mania/20240920_RH_069-16x9.jpg?2025-08-19T00:52:49+00:00"),

    Attraction(
        "Big Thunder Mountain Railroad",
        "Disneyland Park",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/480/1280/90/media/disneyparksjapan-prod/disneyparksjapan_v0001/1/media/dlr/attractions/big-thunder-mountain-railroad-00.jpg"),

    Attraction(
        "Snow White's Enchanted Wish",
        "Disneyland Park",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/630/354/75/dam/disneyland/attractions/disneyland/snow-whites-enchanted-wish/snow-whites-echanted-wish-dopey-16x9.jpg?1699631846547"),

    Attraction(
        "The Little Mermaid - Ariel's Undersea Adventure",
        "Disney California Adventure",
        "All Year",
        "https://cdn1.parksmedia.wdprapps.disney.com/resize/mwImage/1/480/1280/90/media/disneyparksjapan-prod/disneyparksjapan_v0001/1/media/dlr/attractions/little-mermaid-ariels-undersea-adventure-00.jpg")
)

interface QueueTimesApi {
    @GET("parks/16/queue_times.json")
    suspend fun getDisneylandTimes(): QueueTimes

    @GET("parks/17/queue_times.json")
    suspend fun getCaliforniaAdventureTimes(): QueueTimes
}

/* Helps map attraction names to holiday overlays or other
weird naming schemes */

fun getApiName(attraction: Attraction): String {
    return when (attraction.name) {
        "Pixar Pal-A-Round" -> "Pixar Pal-A-Round – Swinging"
        "It's a Small World", "It's a Small World Holiday" -> "\"it's a small world\""
        else -> attraction.name
    }
}

// Creates the display cards for each attraction
@Composable
fun AttractionCard(attraction: Attraction, waitTimes: Map<String, Int>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(attraction.image)
                    .build(),
                contentDescription = attraction.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Text(
                text = attraction.name,
                modifier = Modifier
                    .padding(12.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val apiName = getApiName(attraction)
            val time = waitTimes[apiName.lowercase()]

            Text(
                text = buildAnnotatedString {
                    append("Wait Time: ")

                    if (time != null) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("$time minutes")
                        }
                    } else {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Closed")
                        }
                    }
                },
                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

// Top bar with Sleeping Beauty castle icon
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier : Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.disneyland_sleeping_beauty_castle),
                    contentDescription = "Top Bar Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(75.dp)
                )
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4F83C4)
        )
    )
}


// Dropdown filter menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterMenu(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = {},
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.displayMedium
                ) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            textStyle = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            options.forEach { option: String ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.displayMedium
                        ) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    }
                )
            }
        }
    }
}

// Generates the dropdown menus and card grid, and also implements the filter logic
@Composable
fun DisneylandAttractionsApp(modifier: Modifier = Modifier) {

    var selectedPark by remember { mutableStateOf("All Parks") }
    var selectedSeason by remember { mutableStateOf("All Year") }

    val parkOptions = listOf(
        "All Parks",
        "Disneyland Park",
        "Disney California Adventure"
    )

    val seasonOptions = listOf(
        "All Year",
        "Halloween",
        "Christmas"
    )

    val filteredAttractions = attractions.filter {
            attraction -> (selectedPark == "All Parks" || attraction.park == selectedPark) &&
            ((selectedSeason == "All Year" && attraction.season == "All Year") ||
                    (selectedSeason != "All Year" && attraction.season == selectedSeason))
    }

    val api = remember {
        Retrofit.Builder()
            .baseUrl("https://queue-times.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QueueTimesApi::class.java)
    }

    var waitTimes by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    LaunchedEffect(Unit) {
        try {
            coroutineScope {
                val dlDeferred = async { api.getDisneylandTimes() }
                val caDeferred = async { api.getCaliforniaAdventureTimes() }
                val results = awaitAll(dlDeferred, caDeferred)

                val allRides = results.flatMap { it.lands.flatMap { land -> land.rides } }

                waitTimes = allRides
                    .filter { it.isOpen }
                    .associate { it.name.lowercase() to it.waitTime }
            }
        } catch (e: Exception) {
        }
    }

    Scaffold(
        topBar = {
            AppTopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(modifier = Modifier.padding(8.dp)) {
                    FilterMenu(
                        selectedValue = selectedPark,
                        options = parkOptions,
                        label = "Select Park",
                        onValueChangedEvent = { selectedPark = it },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterMenu(
                        selectedValue = selectedSeason,
                        options = seasonOptions,
                        label = "Select Season",
                        onValueChangedEvent = { selectedSeason = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(filteredAttractions) { attraction ->
                        AttractionCard(attraction = attraction, waitTimes = waitTimes)
                    }
                }
            }
        }
    }
}


// Main method
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisneylandAttractionsTheme {
                DisneylandAttractionsApp()
            }
        }
    }
}