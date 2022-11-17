package com.ash.plugins

import com.ash.authenticate
import com.ash.data.user.UserDataSource
import com.ash.getSecretInfo
import com.ash.security.hashing.HashingService
import com.ash.security.token.TokenConfig
import com.ash.security.token.TokenService
import com.ash.signIn
import com.ash.signUp
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
