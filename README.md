# kibu-plugins
Bridge between [mplugins](https://github.com/LCLPYT/mplugins) and [kibu](https://github.com/LCLPYT/kibu) for FabricMC.

## About kibu-plugins
Kibu plugins are similar in concept to Bukkit/Spigot/Paper-plugins but for a Fabric environment.
You can dynamically load or unload kibu-plugins, which may be useful in some situations.
If you don't want to add any new content in your mod, such as new items, blocks etc., you may use kibu-plugins instead!

---

**Please note**: Kibu plugins are **NOT COMPATIBLE** with Bukkit/Spigot/Paper and will not attempt to be.
Kibu plugins are an alternative to those plugin loaders.
The project started, because existing plugin loaders do not support modding.
An alternative to kibu plugins is the Sponge project, which has seen some development recently but does not seem to run stable on modern versions yet.

---

Since kibu-plugins can be loaded and unloaded at any time, it is not possible to register new content as that can only be done during startup. 
In such cases, mods should be used instead.

However, anything which is already present at runtime, is available.
For example, kibu-plugins have first-class support for hooks, commands and scheduler tasks.
Hooks being a modified version of fabric's events, which allow un-registration.

That means, you can easily listen to events (hooks), register new Minecraft commands and submit scheduler tasks.
Kibu-plugins will take care of unregistering those, when the plugin is unloaded.

**PLEASE NOTE**: 
Anything from your plugin that is registered / stored / referenced in code outside your plugin will not be unloaded automatically; you are responsible for cleanup yourself!
Code breaking this convention may remain in the JVM, even after plugin unload.
However, if you load the plugin again, classes that are "duplicate" due to poor cleanup, will be treated as different classes.
Nonetheless, expect unexpected side effects in those situations.

## Development of custom plugins
Kibu-plugins are perfectly suited to develop mini-games or other small behaviour changes using Fabric.
In combination with the [kibu-plugin-dev](https://github.com/LCLPYT/kibu-plugin-dev) Gradle plugin, 
you can even deploy your plugin to a running Fabric server, directly from your IDE.

To get started quickly, you can use [kibu-plugin-boilerplate](https://github.com/LCLPYT/kibu-plugin-boilerplate).

## Gradle Dependency
You can install kibu-plugins via Gradle.

To use kibu-plugins in your project, modify your `project.gradle`:
```groovy
repositories {
    mavenCentral()
    
    maven {
        url "https://repo.lclpnet.work/repository/internal"
    }
}

dependencies {
    implementation 'work.lclpnet.mods.kibu:kibu-plugins:0.3.0'  // replace with your version
}
```
All available versions can be found [here](https://repo.lclpnet.work/#artifact/work.lclpnet.mods.kibu/kibu-plugins).
