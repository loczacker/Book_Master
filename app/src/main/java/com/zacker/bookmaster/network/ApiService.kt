package com.zacker.bookmaster.network

import com.zacker.bookmaster.model.BooksModel
import com.zacker.bookmaster.model.CartsModel
import com.zacker.bookmaster.model.FavouriteModel
import com.zacker.bookmaster.model.PaymentIntentRequest
import com.zacker.bookmaster.model.PaymentIntentResponse
import com.zacker.bookmaster.model.PaymentsModel
import com.zacker.bookmaster.model.UsersModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    suspend fun getCartByEmailWithCoroutine(@Path("email") email: String): List<BooksModel>

    @GET("/favourite/{email}")
    suspend fun getFavouriteByEmailWithCoroutine(@Path("email") email: String): List<BooksModel>

    @GET("/payment-history/{email}")
    suspend fun getPaymentHistoryEmailWithCoroutine(@Path("email") email: String): List<PaymentsModel>

    @POST("/new-user")
    fun postNewUser(
        @Body usersModel: UsersModel
    ): Call<UsersModel>

    @POST("/add-to-favourite")
    suspend fun postNewFavourite(
        @Body favouriteModel: FavouriteModel
    ): FavouriteModel

    @POST("/add-to-cart")
    suspend fun postNewCart(
        @Body cartsModel: CartsModel
    ): CartsModel

    @POST("/create-payment-intent")
    fun createPaymentIntent(@Body request: PaymentIntentRequest): Call<PaymentIntentResponse>

    @POST("/payment-info")
    fun savePaymentInfo(@Body paymentInfo: PaymentsModel): Call<Void>

    @PATCH("/update-user/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: UsersModel
    ): Call<UsersModel>

    @DELETE("/delete-favourite-item/{id}")
    suspend fun deleteFavouriteItem(@Path("id") id: String): BooksModel
    @DELETE("/delete-cart-item/{id}")
    suspend fun deleteCartItem(@Path("id") id: String): BooksModel
}