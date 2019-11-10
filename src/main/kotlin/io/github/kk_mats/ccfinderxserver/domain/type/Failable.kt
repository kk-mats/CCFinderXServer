package io.github.kk_mats.ccfinderxserver.domain.type

import com.fasterxml.jackson.annotation.JsonInclude

data class Failure(val code: String, val message: String)

@JsonInclude(JsonInclude.Include.NON_NULL)
class Failable<T : Any> private constructor(val failure: Failure?, val value: T?) {
    internal constructor(failure: Failure) : this(failure, null)
    internal constructor(value: T) : this(null, value)

    fun <R : Any> delegate(): Failable<R> {
        check(this.failure != null) { "Failable.delegate called when it is not failed" }

        return fail(this.failure)
    }
}

fun <T : Any> succeed(value: T) = Failable(value)
fun <T : Any> fail(failure: Failure) = Failable<T>(failure)