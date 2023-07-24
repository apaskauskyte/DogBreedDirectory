package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DogBreed(
    val id: Int,
    val name: String,
    val bred_for: String?,
    val life_span: String?,
    val temperament: String?,
    val origin: String?,
    val image: @RawValue Image,
) : Parcelable

data class Image(
    val url: String?,
)