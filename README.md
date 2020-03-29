### ParagonLite

Native Android receipt printing app written in Kotlin, this project was done as an experiment on how to implement MVVM,
and my first “real” Kotlin application, it was never meant to be a production app,
more as a learning curve that turned out ok.

#NOTE: 

The printing process ( printer width ) is only optimized for a little Bluetooth printer I had available,
I printed various joke receipts like a parking ticket for my friends / tried to imitate store receipts
for fun, this shouldn't be used in any production / real-world use-cases!

### Architecture

The overall architecture heavily borrows from Uncle Bob’s Clean Architecture, modules separated by “layer”.

Presentation module consists of the MVVM design pattern
Doman module is a pure Kotlin module which utilizes Kotlin coroutines/flow for asynchronous flow
Domain module consists of android room library which is used for local persistence
