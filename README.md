# BigText

Part 1 :
An Android Application to convert long texts in threaded format.
If the button is clicked, the text in the edittext is shown in threaded format.
If the button is long pressed, the text size keeps on increasing till the button is no longer pressed. On releasing the button, the text is shown in threaded
format with the specified font size.
The data persists even when the app is closed.

Part 2 :

ConcurrentModificationError is thrown when two threads try to modify the same data structure. It generally occurs in the Collections of Java. For example, in the situation presented here, an ArrayList is throwing the error. An attempt to modify the arraylist while iterating over it using an iterator might have been made.

The problem has two solutions :
1. If the case is of a remove operation, the iterator has a function to remove the element. When that function is used, there won't be an error.
2. We can iterate over the arraylist by using a for loop upto the size of the arraylist. Accessing the list that way, won't throw an error. Eg. for(int i = 0; i < ar.size(); i++) {
ar.set(i, 1);
}
