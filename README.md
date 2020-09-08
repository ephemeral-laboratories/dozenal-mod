Dozenal Mod
===========

A Minecraft mod implementing an alternative approach to dozenal formatting.


What is Dozenal?
----------------

Dozenal is one of the popularised names for a positional number system
using twelve as the base. Proponents of the dozenal system claim that it
is superior to decimal because many common fractions are more neatly
represented in dozenal.

For example:

* 1/3 = 0.4
* 1/4 = 0.3
* 1/5 = 0.24972497...
* 1/6 = 0.2

For this project I intend to use the Unicode ↊ for ten/dec and ↋ for
eleven/el/leven, though with slightly modified glyphs to fit the
artwork I use elsewhere.


History
-------

Replacing the `NumberFormatProvider` implementation to provide custom
`NumberFormat` implementations which implement dozenal formatting is a
great _pure_ solution to the problem.

However, it has its drawbacks. In order to make all number formatting
go via `NumberFormat` factory methods, you end up having to fix:

* Minecraft itself
* Forge
* Any other mod you want to include in a pack 

Maintaining changes to Minecraft itself from version to version is a
burden. Patches on Minecraft break and have to be reimplemented.
Code moves around from class to class and has to be tracked down again
every major update.

Changes to Forge itself most likely don't get accepted by Forge devs.
(Disclaimer: I haven't actually tried.)

And of course, any decent pack has a lot of mods, some of which deal
with number formatting. Sometimes _a lot_ of number formatting.


Design Outline
--------------

The approach taken in this mod is simple but fiddly:

* Intercept all calls to draw strings
* Find numbers in the string
* Parse the number to get numeric values out
* Reformat the numeric values in dozenal
* A built-in resource pack deals with adding any glyphs missing from
  Minecraft's default resources.

Each time Minecraft updates, the signature of the method rendering strings
may change, which requires an update -- but only _one_ update. The rest of
the mod code remains unchanged.


Building this Mod
-----------------

Prerequisites for running the build:

* `JAVA_HOME` is set and points to a JDK 8 installation. The build will
  not work on newer versions!
* Nothing else? Not to my knowledge, anyway. Gradle should set up the rest.

To build:

```
gradlew build
```

This will produce a distribution jar in `build/libs` which is the mod jar.
