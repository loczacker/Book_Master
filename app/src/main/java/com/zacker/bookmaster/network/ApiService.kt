package com.zacker.bookmaster.network

import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.model.CartsModel
import com.zacker.bookmaster.model.UsersModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/all-books")
    fun getAllBook(): Call<List<BooksModel>>
    @GET("/all-books")
    suspend fun getAllBookWithCoroutine(): List<BooksModel>

    @GET("/book/{id}")
    suspend fun getBookWithIdCoroutine(@Path("id") id: String): BooksModel

    @GET("/user/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UsersModel>

    @GET("/cart/{email}")
    fun getCartByEmail(@Path("email") email: String): Call<CartsModel>


    @POST("/new-user")
    fun postNewUser(
        @Body usersModel: UsersModel
    ): Call<UsersModel>

    @PATCH("/update-user/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: UsersModel
    ): Call<UsersModel>

//    @GET("/users")
//    fun getAllUser(): Call<List<UsersModel>>
//    @PUT("/update-user/{id}")
//    fun updateUser(
//        @Path("id") userId: String,
//        @Body user: UsersModel
//    ): Call<UsersModel>

}