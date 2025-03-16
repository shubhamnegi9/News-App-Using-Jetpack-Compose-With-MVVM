package com.shubham.newsappwithmvvm.data.models

enum class ArticleCategory(val categoryName: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports")
}

fun getAllArticleCategories(): List<ArticleCategory> {
    return listOf(
        ArticleCategory.BUSINESS,
        ArticleCategory.ENTERTAINMENT,
        ArticleCategory.GENERAL,
        ArticleCategory.HEALTH,
        ArticleCategory.SCIENCE,
        ArticleCategory.SPORTS
    )
}

// Returns the ArticleCategory for the given categoryName
fun getArticleCategory(categoryName: String): ArticleCategory? {
    val map = ArticleCategory.entries.associateBy(ArticleCategory::categoryName)
    return map[categoryName]
}