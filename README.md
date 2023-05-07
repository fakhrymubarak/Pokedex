# Pokedex

Apps for technical assesment Mobile Developer PhinCon.

# Use Cases
1. Pokemon List Page
    - Primary Scenario
        - Execute “Get List Pokemon” from PokeAPI
        - Use Paging to load items
        - Show Loading using shimmer while waiting response from the API
        - Show list Pokemon
    - Negative Scenario
        - Invalid Data or  (4xx - 5xx)
            - Show toast error
        - No Connectivity
            - Show toast error

2. Pokemon Detail
    - Primary Scenario
        - Execute "Get Pokemon Details" from PokeAPI
        - Show button catch pokemon
    - Negative Scenario
        - Invalid Data or  (4xx - 5xx)
            - Show toast error
        - No Connectivity
            - Show toast error

3. Catch Pokemon
    - Primary Scenario (Success Catch)
        - User pressed button catch pokemon
        - System try to catch pokemon with probability 50%
        - System delay 3000 while animating the pokeball (optional)
        - Show bottom sheet success and text input nickname pokemon.
        - Put the pokemon to Local Database.
    - Negative Scenario (Failed Catch)
        - Show toast failed catch
        - Show failed to catch pokemon animation (optional)

4. My Pokemon List (Catches Pokemon)
    - Primary Scenario
        - Execute "Get Pokemon Details" from PokeAPI
        - Use Paging to load items
        - Show list Pokemon
    - Negative Scenario
        - Empty Pokemon
            - Display Empty List Placeholder
            - Show toast error

5. Release pokemon
    - Primary Scenario
        - User pressed release pokemon
        - Delete pokemon from Local Database
        - Update my pokemon list.
    - Negative Scenario
        - Empty Pokemon
        - Invalid Data or  (4xx - 5xx)
            - Show toast error


# Tech Stack
- Minimum Android SDK level 24
- [Kotlin](https://kotlinlang.org/)
  based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
   + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
     for asynchronous.
- Architecture
   - MVVM Design Pattern
   - Repository Pattern Implementation
   - Android Architecture Components (Data - Domain - Presentation)
- Jetpack
   - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle): Observe
     Android lifecycles and handle UI states upon the lifecycle changes.
   - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): Manages
     UI-related data holder and lifecycle aware. Allows data to survive
     configuration changes such as screen rotations.
   - [ViewBinding](https://developer.android.com/topic/libraries/view-binding): Binds UI components
     in your layouts to data sources in your app using a declarative format rather than
     programmatically.
     database access.
   - [Dagger-Hilt](https://dagger.dev/hilt/): for dependency injection.)
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs, and interface
  to handle network payload for Android.
- [Timber](https://github.com/JakeWharton/timber): A logger with a small, extensible API.