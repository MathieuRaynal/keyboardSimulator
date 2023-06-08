# Utilisation des eye trackers Tobii en Java

Pré-requis :
* Une machine Windows (les drivers ne sont pas compatibles avec d'autres plateformes)
* Un eye tracker Tobii 4C ou 5

**NB :** la librairie s'occupe seulement de la récolte des données de suivi de regard, pour le calibrage et la création de profils, il faut utiliser l'[application fournie par Tobii](https://gaming.tobii.com/getstarted/)

## Installation

On utilise la librairie [TobiiStreamEngineForJava](https://github.com/GazePlay/TobiiStreamEngineForJava) créée par l'auteur de GazePlay pour récupérer les données des eye trackers. 

### Maven

Dans le fichier `pom.xml` ajouter les lignes suivantes :

````xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
````

```xml
<dependencies>
    <dependency>
        <groupId>com.github.gazeplay</groupId>
        <artifactId>TobiiStreamEngineForJava</artifactId>
        <version>5.0</version>
    </dependency>
</dependencies>
```

### Gradle

Dans le fichier `build.gradle` ajouter les lignes suivantes :
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.gazeplay:TobiiStreamEngineForJava:5.0'
}
```

## Utilisation

Pour récupérer les données de suivi de regard, appeler :

```java
float[] position = Tobii.gazePosition();
float rawGazeX = position[0];
float rawGazeY = position[1]
```

Les chiffres renvoyés sont entre 0 et 1, `(0, 0)` étant en haut à gauche, et `(1, 1)` étant en bas à droite.
Par défaut, le Tobii 4C retourne `(0.5, 0.5)` lorsque l'on ne regarde pas l'écran, et le Tobii 5 renvoie `(-1, -1)`.
Pour passer en pixels sur l'écran, utiliser : 

```java
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
int gazeX = (int) (rawGazeX * screenSize.getWidth());
int gazeY = (int) (rawGazeY * scrennSize.getHeight());
```