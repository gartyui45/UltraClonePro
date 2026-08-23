package com.ultraclone.pro.data.model

enum class AppCategory(val displayName: String) {
    ALL("Todos"), GAMES("Jogos"), SOCIAL("Redes Sociais"), TOOLS("Ferramentas"), BANKING("Bancos"), OTHER("Outros");

    companion object {
        fun fromPackageName(pkg: String): AppCategory = when {
            pkg.contains("game") || pkg.contains("freefire") || pkg.contains("genshin") || pkg.contains("pubg") -> GAMES
            pkg.contains("whatsapp") || pkg.contains("instagram") || pkg.contains("facebook") || pkg.contains("telegram") || pkg.contains("tiktok") -> SOCIAL
            pkg.contains("bank") || pkg.contains("banco") || pkg.contains("nubank") -> BANKING
            pkg.contains("tool") || pkg.contains("browser") || pkg.contains("player") -> TOOLS
            else -> OTHER
        }
    }
}
