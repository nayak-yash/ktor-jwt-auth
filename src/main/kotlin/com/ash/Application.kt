package com.ash

import com.ash.data.user.MongoUserDataSource
import com.ash.plugins.*
import com.ash.security.hashing.SHA256HashingService
import com.ash.security.token.JwtTokenService
import com.ash.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://yash:$mongoPw@cluster0.irqro0i.mongodb.net/ktor-auth?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource= MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig =TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(userDataSource,hashingService,tokenService,tokenConfig)
}
