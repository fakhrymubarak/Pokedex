package com.fakhry.pokedex.core.enums

class DatabaseError : Throwable()

class Connectivity : Throwable()
class UnexpectedValuesRepresentation : Throwable()

// HTTP 401 Error
class Unauthorized : Throwable()

// HTTP 402 Error
class Forbidden : Throwable()

// HTTP 422 Error
class InvalidData : Throwable()

// HTTP 400..499 Error
class BadRequest : Throwable()

// HTTP 500..599 Error
class ServerError : Throwable()