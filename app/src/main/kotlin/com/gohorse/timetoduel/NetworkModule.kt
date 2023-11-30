package com.gohorse.timetoduel

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @ExperimentalSerializationApi
    fun provideRetrofit(
        networkJson: Json,
        okhttpCallFactory: Call.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://db.ygoprodeck.com/api/v7/cardinfo.php")
            .callFactory(okhttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            ).build()
    }

    @Singleton
    @Provides
    fun provideCardsService(retrofit: Retrofit): CardsService {
        return retrofit.create(CardsService::class.java)
    }
}

interface CardsService {

    @GET
    suspend fun fetchAllCards(): CardList

}

@Serializable
data class CardList(
    @SerialName("data")
    val data: List<Data>
)

@Serializable
data class Data(
    @SerialName("atk")
    val atk: Int,
    @SerialName("attribute")
    val attribute: String,
    @SerialName("card_images")
    val cardImages: List<CardImage>,
    @SerialName("card_prices")
    val cardPrices: List<CardPrice>,
    @SerialName("card_sets")
    val cardSets: List<CardSet>,
    @SerialName("def")
    val def: Int,
    @SerialName("desc")
    val desc: String,
    @SerialName("frameType")
    val frameType: String,
    @SerialName("id")
    val id: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("name")
    val name: String,
    @SerialName("race")
    val race: String,
    @SerialName("type")
    val type: String
)

@Serializable
data class CardImage(
    @SerialName("id")
    val id: Int,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("image_url_cropped")
    val imageUrlCropped: String,
    @SerialName("image_url_small")
    val imageUrlSmall: String
)

@Serializable
data class CardPrice(
    @SerialName("amazon_price")
    val amazonPrice: String,
    @SerialName("cardmarket_price")
    val cardmarketPrice: String,
    @SerialName("coolstuffinc_price")
    val coolstuffincPrice: String,
    @SerialName("ebay_price")
    val ebayPrice: String,
    @SerialName("tcgplayer_price")
    val tcgplayerPrice: String
)

@Serializable
data class CardSet(
    @SerialName("set_code")
    val setCode: String,
    @SerialName("set_name")
    val setName: String,
    @SerialName("set_price")
    val setPrice: String,
    @SerialName("set_rarity")
    val setRarity: String,
    @SerialName("set_rarity_code")
    val setRarityCode: String
)
