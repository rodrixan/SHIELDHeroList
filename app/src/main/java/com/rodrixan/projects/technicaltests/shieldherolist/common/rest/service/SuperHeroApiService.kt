package com.rodrixan.projects.technicaltests.shieldherolist.common.rest.service

import arrow.core.Either
import com.example.rodri.projectlist.common.rest.api.SuperHeroApi
import com.google.gson.GsonBuilder
import com.rodrixan.projects.technicaltests.shieldherolist.common.RestConstants
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.ErrorResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.HttpURLConnection

object SuperHeroApiService {

    private val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = RestConstants.HTTP_LOGGING_LEVEL
            }).build()

    //need to be internal cause of the extension function (see toSuccessOrError)
    internal val serviceRetrofit = Retrofit.Builder().client(client).addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().create())).baseUrl(RestConstants.BASE_URL).build()

    val serviceApi = serviceRetrofit.create(SuperHeroApi::class.java)
}

fun <T : Any> Response<T>.toSuccessOrError(): Either<T, ErrorResponse> {
    return try {
        when (code()) {
            HttpURLConnection.HTTP_OK -> body()?.run { Either.Left(this) } ?: Either.Right(
                    ErrorResponse(-1, "Received empty body from request"))
            else -> {
                val converter = SuperHeroApiService.serviceRetrofit.responseBodyConverter<ErrorResponse>(
                        ErrorResponse::class.java,
                        arrayOfNulls<Annotation>(0))
                errorBody()?.run {
                    val serviceError = converter.convert(this)
                    Either.Right(ErrorResponse(serviceError?.code ?: code(),
                            serviceError?.message ?: "Null error message"))

                } ?: Either.right(ErrorResponse(-1, "Empty error body"))
            }
        }
    } catch (e: Exception) {
        Timber.e("Exception in call: $e")
        Either.right(ErrorResponse(-1, "Unexpected Error: ${e.message}"))
    }
}

