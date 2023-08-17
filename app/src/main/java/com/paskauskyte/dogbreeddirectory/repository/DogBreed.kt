package com.paskauskyte.dogbreeddirectory.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogBreed(
    val id: Int,
    val name: String,
    val bred_for: String?,
    val life_span: String?,
    val temperament: String?,
    val origin: String?,
    val reference_image_id: String?,
) : Parcelable {
    val bredFor: String?
        get() = bred_for

    val lifeSpan: String?
        get() = life_span

    val imageId: String?
        get() = reference_image_id
}

data class Image(
    val url: String?,
)