# Pokemon Go Xposed - Disable SSL pinpointing

This xposed module takes care of always letting the Pokemon Go app think it's in its trusted surrounding. Whatever trust chain is currently established, it will be replaced by the expected one.

This makes MITM thingies like [pokemon-go-mitm](https://github.com/rastapasta/pokemon-go-mitm-node) work again :)

## How to use it?
* Your phone must be *rooted*
* Install the [Xposed Framework](http://repo.xposed.info/module/de.robv.android.xposed.installer)
* Install the [PokemonGoXposed module](https://github.com/rastapasta/pokemon-go-xposed/raw/master/PokemonGoXposed.apk)
* Activate it in the Xposed Installer
* Restart your phone!
