package com.paskauskyte.dogbreeddirectory.dog_breeds

data class DogBreed(
    val id: Int,
    val name: String,
    val bred_for: String,
    val life_span: String,
    val temperament: String,
    val origin: String,
    val reference_image_id: String,
)