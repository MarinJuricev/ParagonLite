### ParagonLite

Native Android receipt printing app written in Kotlin, this project was done as an experiment on how to implement MVVM,
it was never meant to be a production app, more as a learning curve that turned out into a fully functional/tested app.

#NOTE: 

The printing process ( printer width ) is only optimized for a little Bluetooth printer I had available,
I printed various joke receipts like a parking ticket for my friends / tried to imitate store receipts
for fun, this shouldn't be used in any production / real-world use-cases!

### Architecture

The overall architecture heavily borrows from Uncle Bob’s Clean Architecture, modules separated by “layer”.

Presentation module consists of the MVVM design pattern

Domian module is a pure Kotlin module which utilizes Kotlin coroutines/flow for asynchronous flow

Data module consists of android room library which is used for local persistence


### TESTS

Most of the viewmodels in the presentation moduel are fully unit tested,

All of the usecases that live in domain module are fully unit tested,

Most of the repositories inside the data layer are fully unit tested.

#Libs used: 

- Jetpack Navigation
- Coroutines
- Flow
- Koin
- Room
- Mockk
- Junit5
- Dexter
- LiveData

# Screenshots

### Checkout

<img width="350" alt="creation_page" src="/screenshots/checkout.jpg">

### Articles

<img width="350" alt="creation_page" src="/screenshots/articles.jpg">


### Bluetooth Initial

<img width="350" alt="creation_page" src="/screenshots/bluetooth_initial.jpg">


### Request Bluetooth

<img width="350" alt="creation_page" src="/screenshots/request_bluetooth.jpg">


### History

<img width="350" alt="creation_page" src="/screenshots/history.jpg">


### History Calendar

<img width="350" alt="creation_page" src="/screenshots/history_calendar.jpg">


### Settings

<img width="350" alt="creation_page" src="/screenshots/settings.jpg">
