package com.zacker.bookmaster.network

import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.model.UsersModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/all-books")
    fun getAllBook(): Call<List<BooksModel>>
    fun getRandomBook()
    @GET("/all-books")
    suspend fun getAllBookWithCoroutine(): List<BooksModel>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: String): Call<UsersModel>

    @GET("/users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UsersModel>


//    @GET("/users")
//    fun getAllUser(): Call<List<UsersModel>>
//    @PUT("/update-user/{id}")
//    fun updateUser(
//        @Path("id") userId: String,
//        @Body user: UsersModel
//    ): Call<UsersModel>


}