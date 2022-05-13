package com.practice.composetest.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.practice.composetest.data.models.SuperHero
import timber.log.Timber

@Composable
fun SuperHeroScreen() {

    val viewModel = viewModel(modelClass = SuperHeroViewModel::class.java)
    val superHeroes by viewModel.superHeroesFlow.collectAsState()

    LazyColumn {
        if (superHeroes.isEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
        itemsIndexed(superHeroes) { index: Int, superHero: SuperHero ->
            SuperHeroCard(superHero = superHero) {
                viewModel.updateSuperHero(!superHero.isSelected, index)
            }
        }
    }
}

@Composable
fun SuperHeroCard(superHero: SuperHero, onItemClicked: () -> Unit) {
    val imagerPainter = rememberAsyncImagePainter(model = superHero.imageurl)

    Card(
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box {
            Image(
                painter = imagerPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable(onClick = {
                        Timber.d("Super Hero Card Clicked ${superHero.realname} ${superHero.isSelected}")
                        onItemClicked.invoke()
                    }),
                contentScale = ContentScale.FillBounds
            )
            Surface(
                color = MaterialTheme.colors.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(),
                        fontFamily = FontFamily.Serif,
                        text = "${superHero.realname}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (superHero.isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = Color.Green,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterVertically),
                            contentDescription = "Selected"
                        )
                    }
                }
            }
        }
    }
}

