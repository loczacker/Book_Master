package com.zacker.bookmaster.model

data class FavouritesModel (
    var _id: String ?= null,
    var bookId: String ?= null,
    var bookTitle: String ?= null,
    var userMail: String ?= null,
    var data: String ?= null,
)