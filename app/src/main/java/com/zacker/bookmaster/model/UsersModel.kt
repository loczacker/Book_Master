package com.zacker.bookmaster.model


data class UsersModel(
    var _id: String ?= null,
    var email: String ?= null,
    var name: String ?= null,
    var photoURL: String ?= null,
    var role: String ?= null,
    var address: String ?= null,
    var phone: String ?= null,
    var about: String ?= null,
)