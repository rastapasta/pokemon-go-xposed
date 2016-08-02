# Pokemon Go Xposed - Let it trust, let it trust!

This Xposed module takes care of always letting the Pokemon Go app think it's in its trusted surrounding.
Whatever trust chain is actually established when connecting to the API, it will be replaced with the one expected by the app.

This makes MITM thingies like [pokemon-go-mitm](https://github.com/rastapasta/pokemon-go-mitm-node) work again :)

## How to use it?
* Your phone must be *rooted*
* Install the [Xposed Framework](http://repo.xposed.info/module/de.robv.android.xposed.installer)
* To install
  * install [Pokemon Go Trust Certificate](http://repo.xposed.info/module/de.rastapasta.android.xposed.pokemongo) via the Xposed Installer
  * or manually download and install the [PokemonGoXposed module](https://github.com/rastapasta/pokemon-go-xposed/releases/download/v1.1/PokemonGoXposed.apk)
* Activate the module in the Xposed Installer
* Restart your phone!
