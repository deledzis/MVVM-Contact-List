package ru.deledzis.skbkonurtestapp.data.model

data class Contact(
        val id: String,
        val name: String,
        val phone: String,
        val biography: String,
        val height: Double,
        val temperament: String,
        val educationPeriod: EducationPeriod
)