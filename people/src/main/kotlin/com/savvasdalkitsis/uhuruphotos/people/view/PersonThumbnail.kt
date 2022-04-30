package com.savvasdalkitsis.uhuruphotos.people.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.savvasdalkitsis.uhuruphotos.image.api.view.Image
import com.savvasdalkitsis.uhuruphotos.people.view.state.Person

@Composable
fun PersonThumbnail(
    modifier: Modifier = Modifier,
    person: Person,
    onPersonSelected: () -> Unit = {},
    shape: RoundedCornerShape = CircleShape,
) {
    Column(
        modifier = modifier.clickable { onPersonSelected() },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .clip(shape),
            url = person.imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "person image"
        )
        Text(
            text = person.name,
            maxLines = 2,
            textAlign = TextAlign.Center,
        )
    }

}