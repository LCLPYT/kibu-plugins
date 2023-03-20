# kibu-plugins
Bridge between [mplugins](https://github.com/LCLPYT/mplugins) and [kibu](https://github.com/LCLPYT/kibu) for FabricMC.

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
