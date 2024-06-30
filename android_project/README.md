# Rango

## About this app

This is a digital menu that you implement in your restaurant (or something like that) to make some
manual processes more digital.
In this case, this app can be used to send orders to kitchen, calculate tables payment and show tables historic.

My idea is to automate as much of all restaurant process as possible.

___

### Features:

 -> Menu: you can see all menu sections and items.
 -> Order: You can see all items in your order, choose a table and submit to kitchen.
 -> Tables: You can see all orders of all tables, and close their accounts.
 -> Sign in, Sign up and Sign out.

___

### Future features:

* Multi usages:
At this moment this app can be used for only one restaurant.
I want to make this app usable for as many restaurants as possible. For this, i need to create a restaurant register.
Each restaurant can be create your own menu, table, employees account, administrator account...

* Dishes register:
At this moment i need to access firebase realtime database to register any new item to menu, i want to create a screen to insert new items to menu.

* Category register:
Each dishe need at least one category (drinks, dessert...), and i need to create a feature to register new dishes categories.

* Search dishes:
Creation of a feature to search menu items by name, category...

* Search tables:
A feature to search tables.

* Dark Mode
___

## About this software

This is a native Android software with kotlin language and MVVM architecture. I use firebase to all my external needs, like: authentication service, online database, crash reports...

### Technologies
* [Architecture](https://developer.android.com/topic/architecture)
* [Kotlin language](https://kotlinlang.org/)
* [View Binding](https://developer.android.com/topic/libraries/view-binding)
* [Navigation](https://developer.android.com/guide/navigation)
* [Hilt - dependency injection](https://developer.android.com/training/dependency-injection/hilt-android)
* [Preferences data store - local data store](https://developer.android.com/topic/libraries/architecture/datastore)
* [Firebase - Realtime database, crashlytics, analytics, authentication and distribution](https://firebase.google.com/)
* [Material - Official Android design](https://m3.material.io/)
* [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)

___

### Data flow
I'm using MVVM architecture, so my app basically contains 3 layers:

**View >> ViewModel >> Repository**
**View << ViewModel << Repository**

**View:** UI elements that render the data on the screen..
**ViewModel:** State holders (that hold data, expose it to the UI, and handle logic).
**Repository:** Data Layer (call external or internal services to return any data).


Also, i'm using a **single activity architecture**, i's means that my software has only one activity.
This activity contains a fragment container view where i inflate all my fragments.

I prefer to use this methodology because it's most simple to control app navigation, global events and
it's avoids to much bureaucracy (activity declaration on manifest, for example)

___

Last update: 06/30/2024