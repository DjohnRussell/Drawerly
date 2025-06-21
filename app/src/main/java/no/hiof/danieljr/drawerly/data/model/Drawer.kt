package no.hiof.danieljr.drawerly.data.model

data class Drawer(
    val id: String = "",
    val name: String = "",
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)