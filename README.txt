Okay, quite a bit has changed!

I added the Algorithm and TextIO classes into the app.

I had to comment out some parts of TextIO because they didn't import properly, but those shouldn't be necessary for this program, so don't worry about that. All we really need is the readFile() method because we aren't actually using standard input/output. 

Algorithm.java has been changed a lot. The main method signature has been overhauled completely. Algorithm.java actually does not have a main method now. I renamed it check() and it takes three parameters: lang1, lang2, and originalText. All are of type String, and I think the names of the variables should make it more or less clear what they are. The check() method returns a String, result. Right now, result is just a bunch of arrays and junk. That will have to be prettified.

Within MainActivity.java, clicking the CHECK button now calls Algorithm's check() method and will output the String that it returns. The current selected items on the Spinner objects (the dropdown menus) are put in as lang1 and lang2, and the text from the EditText field is put in as originalText.

The dictionaries are inside res in a directory called "dictionaries". The big problem right now is that I can't get TextIO to see those files.