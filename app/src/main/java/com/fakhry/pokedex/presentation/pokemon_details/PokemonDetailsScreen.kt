package com.fakhry.pokedex.presentation.pokemon_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fakhry.pokedex.R
import com.fakhry.pokedex.core.enums.PokemonType
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.theme.ColorGray2
import com.fakhry.pokedex.theme.ColorPrimaryApps

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeaderSection(images: List<String>, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState()
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageSlider(images = images, pagerState = pagerState)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentSize()
        ) {
            DotsIndicator(
                totalDots = images.size,
                selectedIndex = pagerState.currentPage,
                selectedColor = ColorPrimaryApps,
                unSelectedColor = ColorGray2
            )
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(align = Alignment.TopStart)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null // decorative element
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BodySection(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val context = LocalContext.current

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = pokemon.name, style = MaterialTheme.typography.titleLarge)
        }
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = context.getString(R.string.text_weight_value, pokemon.weight.toString()),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = context.getString(R.string.text_pokemon_type),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            FlowRow {
                pokemon.types.forEach { type ->
                    val pokemonType = PokemonType.from(type.name)
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(
                                color = colorResource(id = pokemonType.backgroundResId),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp),
                            painter = painterResource(id = pokemonType.resId),
                            contentDescription = pokemonType.name,
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                        Text(
                            text = type.name,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(pagerState: PagerState = rememberPagerState(), images: List<String>) {

    HorizontalPager(
        pageCount = images.size, state = pagerState, key = { images[it] }, pageSize = PageSize.Fill
    ) { index ->
        AsyncImage(
            model = images[index],
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(
            )
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
) {

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

/// todo Button Catch Pokemon