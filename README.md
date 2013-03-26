A Rogue Dream
====================

My failed 7DRL entry that used the web to autogenerate a game. In its current state the code is not really runnable - instead it's here to offer an insight into some of the tech used in making the experimental game stuff.

The music in the game is by Thompost, used with kind permission. Sorry it didn't get made into a full game ! https://soundcloud.com/orbyss-1/abyss

How To "Play"
====================

At the time of writing it's unclear whether it's possible (or easy) to get this running on another machine, but you don't really need to run it to benefit from the code. If you can get it running, then it works as follows:

Press space at the menu and enter a noun. More general nouns are better, but part of the fun is putting in something and seeing what game you get out.

Once the game loads, you will have a bland game in front of you with placeholder abilities and items (press i to toggle between inventory and abilities on the bottom bar). As time passes, the game will fill in content, starting with the player's icon, then enemies, abilities and items. 

Arrow keys move. Clicking on abilities uses them, but currently all abilities just heal the player, regardless of what their description is. This really is an unfinished game - only the background tech is of interest which is why I'm uploading it.

Tech That Might Be Useful
====================

There are two bits of tech inside that you might be interested in.

Mining Google Autocomplete For Facts
---------------------

University College Dublin's Tony Veale noticed that if you ask Google a question, the autocomplete results often give you factual (or at least, interesting/common knowledge) information. You can read more about the technique in his ICCC paper here: http://computationalcreativity.net/iccc2012/wp-content/uploads/2012/05/001-Veale.pdf

This means that if you put into Google "why do doctors", the autocomplete results tell you that doctors *wear white coats*, *say stat* and *prescribe steroids*, among other things. I used this technique in A Rogue Dream, with modifications, to extract information that might help one design a roguelike. This includes asking extended questions such as "why do 'noun's wear" or "why do 'noun's hate" to get information on clothing and enemies respectively. 

You can find the code relating to this in net.cutgar.ard.coldread. I use the Google GSON library I believe, along with evo-inflector (https://github.com/atteo/evo-inflector) to pluralise words. 

Automatically Generating Sprites From The Web
---------------------

A Rogue Dream generates its own game icons and sprites automatically. It does this using Spritely, a tool I'm building that can mine the web for images and process them into a particular size and format, including recolouring according to a theme. 

Spritely is bundled here somewhere as a jar, and you can see the code that calls Spritely in the package net.cutgar.ard.concurrent. Spritely is used in a separate thread so that the sprite generation can be done online in realtime. Of course you can also use spritely during development to generate placeholder art for your games while fast prototyping. 

More on Spritely later when I release it as a standalone tool. It's really only included here because it's part of the game - I'm not going to great lengths to explain much about it right now.

More
====================

This code is licensed under the Do What The Fudge You Want license, meaning it is effectively public domain. The license is purely there for people who like/need licenses - it allows you to use it for whatever you want. Commercial. Non-commercial. Other. Go nuts. You don't need to attribute, but I really appreciate it when people let me know, so drop me a line - mike@gamesnbyangelina.org. It also helps me justify working on public releases of my code too, because it's benefitting people!

The license strictly applies to the code in the net.cutgar package only. Libs are under their own licenses, and artwork downloaded/created using Spritely is a bit of a grey area right now. Libs are uploaded here only to provide support for people trying to get code snippets working - if you believe these libs should not be here, please contact me and I'll resolve the issue.

During my day job I research techniques for automating the design of videogames using artificial intelligence. If you want to learn more: http://www.gamesbyangelina.org

In between, I try and make games myself. A Rogue Dream is one such attempt; most are unsuccessful but I put the results online anyway. More info at: http://www.cutgar.net

Please get in touch if you want to ask questions or learn more! I am @mtrc on Twitter.