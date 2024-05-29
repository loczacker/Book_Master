package com.zacker.bookmaster.network

import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.model.UsersModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/all-books")
    fun getAllBook(): Call<List<BooksModel>>
    fun getRandomBook()
    @GET("/all-books")
    suspend fun getAllBookWithCoroutine(): List<BooksModel>

    @GET("/user/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UsersModel>

    @POST("/new-user")
    suspend fun postNewUser(
        @Body usersModel: UsersModel
    ): UsersModel

//    @GET("/users")
//    fun getAllUser(): Call<List<UsersModel>>
//    @PUT("/update-user/{id}")
//    fun updateUser(
//        @Path("id") userId: String,
//        @Body user: UsersModel
//    ): Call<UsersModel>

}