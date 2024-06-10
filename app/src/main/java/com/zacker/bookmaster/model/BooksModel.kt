package com.zacker.bookmaster.model

import java.io.Serializable

data class BooksModel(
    var _id: String ?= null,
    var authorName: String ?= null,
    var bookDescription: String ?= null,
    var bookPDFURL: String ?= null,
    var bookTitle: String ?= null,
    var category: String ?= null,
    var imageURL: String ?= null,
    var price: String ?= null
) : Serializable