package com.zacker.bookmaster.model

data class BooksModel(
    var _id: String,
    var authorName: String,
    var bookDescription: String,
    var bookPDFURL: String,
    var bookTitle: String,
    var category: String,
    var imageURL: String,
    var price: String
)