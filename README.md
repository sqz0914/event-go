# Event Go

## Introduction
**Event Go** is a comprehensive mobile application designed to help users discover, organize, and attend various entertainment events. By integrating advanced APIs and mobile device sensors, Event Go provides a seamless and interactive experience, allowing users to customize their event schedules, search for nearby events, and access detailed venue information, including real-time maps.

## Problem Statement
The main problem Event Go addresses is the difficulty users face in discovering and managing entertainment events. The target audience includes anyone interested in attending events such as concerts, sports games, theater performances, and movies. Event Go provides an easy-to-use platform that aggregates event information from various sources, enables users to create personalized schedules, and offers real-time location-based services for better event planning.

## Features
1. **Event Schedule Management**
- Users can view their upcoming events in a scrollable list, each with details such as event name, venue, and time, along with the option to delete events.
2. **Event Discovery**
- The search form allows users to enter keywords, select event genres, specify distance, and choose location settings to find events.
3. **Event Selection**
- Search results are displayed in a list where users can select multiple events to add to their schedule using checkboxes and an add button.
4. **Detailed Event Information**
- Each event can be tapped to view more details with tabs for event specifics and venue information, including a dynamic Google Map showing the location.

## Integration with APIs
- **Ticketmaster API**: Fetches real-time data on events based on user queries.
- **Google Maps API**: Powers the mapping features in the venue details tab.
- **Geolocation API**: Works alongside the GPS sensor to provide location services for event searches and mapping.

## Design and Implementation

### Design and Architecture
![screens](/Screens.png)
- Event Go is designed with a clean and intuitive interface, ensuring a smooth user experience. The application consists of four main screens: the event schedule, search form, search results, and event details. Each screen is interconnected to provide a seamless navigation flow.
- [Figma Prototype](https://www.figma.com/design/1TEx1xWQ53WTRAW45sPkmX/Final-Project-Design?node-id=0-1&t=E4xRnjC8RZXozBZ0-1)

### Challenges and Solutions
- **Smooth Navigation**: Implemented using a combination of Android’s Fragment and ViewModel to manage UI components and data efficiently.
- **GPS Integration**: Optimized location fetching by combining fine and coarse location strategies and using Google Play services for reliable location updates.
- **Data Consistency**: Ensured data consistency across different activities using Room as a local database, which provides a robust mechanism for managing and synchronizing data.

## Additional Features
- **Hyperlink Integration**: Allows users to directly access event ticket pages.
- **Dynamic Google Maps**: Provides real-time location services and mapping.

## References
- [Ticketmaster API Documentation](https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/)
- [Google Maps API Documentation](https://developers.google.com/maps/documentation/android-sdk/start)
- [Android Developer Documentation](https://developer.android.com/develop)
- [Retrofit API Documentation](https://square.github.io/retrofit/)