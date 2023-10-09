# Design

I want to use the MVC paradigm for this project. What's been bothering me though is how to facilitate allowing the 
different components to communicate with one another. The current idea I'm going with is just an explicit reference
to each in their logic. So, we'll instantiate the Controller. It has a method to add a view and add a model. Then we
create the model and the view and pass the controller into the constructor. Then we add the model and the view back into
the controller. I can't think of another way of doing this without a message broker, but that seemed like a bit too much.
That said, I don't like how tightly coupled my logic is.

## Structure

As mentioned, we're going to follow the MVC paradigm. The model holds the underlying data that the UI represents. The view
represents the data contained in the model in the GUI. The view also receives user input and then calls the controller
to update the model. The controller refers user input to the model, then refers the updated model information to the view.

Do we want to have abstact classes for each of these concepts, or should I just have one class. I think it makes sense 
for the view. It's completely feasible that we could have different UI's, but the underlying File system isn't liable
to change, and how the interactions affect the file system won't change either. So we'll have those just be single classes.

## Division of work

* Model
  * Representation of state
    * Files in directory
    * Current Directory path
  * Methods to update the state
  * Method to return the state
* Controller
  * Methods to add model and views
  * Methods to refer UI updates to model
  * Methods to refer model updates to the UI
* View
  * Method to represent a state through Java swing entities
  * Listeners on the various entities that call the controller

## Testing

### Model

We need to test the various bits of business logic for the model. We should create a small test filesystem for the test.
We can do that in the test/resources directory. So, first, we enter that directory and then make sure that it returns the
correct current directory and files.