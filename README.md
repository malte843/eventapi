# eventapi
[![](https://jitpack.io/v/malte843/eventapi.svg)](https://jitpack.io/#malte843/eventapi)

very very simple event api

to add it to your project click on the jitpack badge and follow the instructions

```
EventBus eventBus = new EventBus(); // Initialize an EventBus. Optionally you could add ErrorHandling.THROW if you want to get the Exceptions that occur in a event to get thrown instad of logged
eventBus.register(this); // Register a Listener
eventBus.unregister(this); // Unregister a Listener
eventBus.call(new ExampleEvent()); // Call an event
```

to create a listener do the following in any class you want: 
```
@Subscribe // you could also use @Subscribe(value = Priority.DEFAULT, ignoreCancelled = false) to change the post order or to call also if event gets cancelled 
private void onEvent(ExampleEvent event) {
    // this method gets invoked every time the event passed as a parameter gets called
}
```

for ignoreCancelled to work all your events that are Cancellable have to extend de.malte.event.events.EventCancellable and the isCancelled() method should return the cancelled state
