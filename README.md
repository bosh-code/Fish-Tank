# Fish-Tank

A fun Java app, written to imitate a fish tank. Compiled builds can be downloaded from the releases.

Click the button to add a 'fish'. Each fish moves based on it's size and speed, with some random noise. Fish that collide will eat each other if possible, and grow by some of the fish that was eaten.
Each fish runs on it's own thread that is held by the FishShoal class.

No external Libraries are used, only imports from java.util, security, swing and awt (for 'graphics').
