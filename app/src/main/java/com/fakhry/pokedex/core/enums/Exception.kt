package com.fakhry.pokedex.core.enums

class NetworkException(message: String = "Oops, you seem to be offline. Please check your connection to internet.") : Exception(message)