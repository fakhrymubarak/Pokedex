package com.fakhry.pokedex.data.infrastructure

import com.fakhry.pokedex.core.enums.BadRequest
import com.fakhry.pokedex.core.enums.Connectivity
import com.fakhry.pokedex.core.enums.Forbidden
import com.fakhry.pokedex.core.enums.InvalidData
import com.fakhry.pokedex.core.enums.ServerError
import com.fakhry.pokedex.core.enums.Unauthorized
import com.fakhry.pokedex.core.enums.UnexpectedValuesRepresentation
import com.fakhry.pokedex.state.HttpClientResult
import okio.IOException
import retrofit2.HttpException

suspend fun <T> execute(block: suspend () -> T): HttpClientResult<T> {
    return try {
        HttpClientResult.Success(block.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 -> HttpClientResult.Failure(Unauthorized())
                    403 -> HttpClientResult.Failure(Forbidden())
                    422 -> HttpClientResult.Failure(InvalidData())
                    in 400..499 -> HttpClientResult.Failure(BadRequest())
                    in 500..599 -> HttpClientResult.Failure(ServerError())
                    else -> HttpClientResult.Failure(ServerError())
                }
            }

            is IOException -> HttpClientResult.Failure(Connectivity())
            else -> HttpClientResult.Failure(UnexpectedValuesRepresentation())
        }
    }
}
