package no.hiof.danieljr.drawerly.data.model

data class Item(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val link: String? = null,
    val price: Double? = null,
    val tags: List<String> = emptyList(),
    val notes: String = ""
)
