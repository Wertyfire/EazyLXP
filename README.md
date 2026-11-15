# EazyLXP

Lightweight, zero-dependency Java library for parsing .lxc (**L**ocalization e**X**tension **C**ontainer) and .lxp (**L**ocalization e**X**tension **P**ackage).

# Features

* **All translations in a single file.** Unlike standard Java `.properties` bundles, `.lxc` keeps all translations for a key on one line, making it easy to maintain context and spot missing translations
* **Zero dependencies.** Built purely with standard Java APIs - no third party libraries needed
* **Built-in format versioning.** Every file starts with a `!version` directive to ensure backward compatibility and clear error reporting when format specifications evolve
* **Crash-proof wrapper & strict mode.** Use `LocalizationFile` in production to log errors and fall back to returning translation keys (`EmptyLXParser`) or use `LXCParser` directly to catch exceptions during validation
* **Custom parser extensibility.** Implement the `ILXParser` interface to plug your own custom localization formats

# Installation

## Gradle (Groovy)

```groovy

repositories {
    mavenCentral()
    maven {
        name = 'wertyfireMaven'
        url = 'https://raw.githubusercontent.com/Wertyfire/Versions/main/maven/'
    }
}

dependencies {
    var eazylxpVersion = "1.2"
    
    implementation("ru.wertyfiregames:eazylxp:$eazylxpVersion:runtime")
}
```

## Maven

```xml
<repositories>
    <repository>
        <id>wertyfireMaven</id>
        <url>https://raw.githubusercontent.com/Wertyfire/Versions/main/maven/</url>
    </repository>
</repositories>

<dependency>
    <groupId>ru.wertyfiregames</groupId>
    <artifactId>eazylxp</artifactId>
    <version>1.2</version>
    <classifier>runtime</classifier>
</dependency>
```

# File Format

## Localization Extension Container (`.lxc` / `.lexc`)

Both `.lxc` and `.lexc` extensions are supported and treated identically.

### Example `translations.lexc`

```text
!version 3
#Supported languages header
key|english|russian|chinese

hello|Hello!|Привет!|<some hieroglyphs> #Inline comment
item_picked|You picked up an item|Вы подобрали предмет|<some hieroglyphs>
```

### Syntax Rules

1. During parsing, each comment or empty line is ignored
2. Each file must start with `!version ` directive followed by a space and a number representing the format version
3. The second line defines the language order. Start with `key` and add language names separated by `|`
4. Each subsequent line must start with a translation key followed by translations in the exact order defined in the header, separated by `|`. Use `#` for single-line comments.

## Localization Extension Package (`.lxp` / `.lexp`)

Currently in development.

# Quick Start

## LXC Format

### Using `LocalizationFile` (Recommended)
`LocalizationFile` automatically detects the file extension, loads resources from the classpath if the path starts with `/`, and suppresses all exceptions.

```java
import ru.wertyfiregames.eazylxp.LocalizationFile;

public class TranslationExample {
    public static void main(String... args) {
        LocalizationFile lxc = new LocalizationFile("translations.lexc", "english");
        System.out.println(lxc.get("hello")); //output: Hello!
        
        lxc.setLanguage("russian");
        System.out.println(lxc.get("hello")); //output: Привет!
    }
}
```

### Using `LXCParser` Directly
If you need strict error handling during development or validation:

```java
import ru.wertyfiregames.eazylxp.LXCParser;
import ru.wertyfiregames.eazylxp.LXCSyntaxException;

import java.io.File;
import java.io.IOException;

public class StrictExample {
    public static void main(String[] args) {
        try {
            LXCParser parser = new LXCParser(new File("translations.lexc"), "russian");
            System.out.println(parser.get("item_picked")); //output: Вы подобрали предмет
        } catch (IOException | LXCSyntaxException e) {
            e.printStackTrace();
        }
    }
}
```

# Roadmap

* [ ] Create JetBrains IDE's plugin for highlighting syntax

## LXC

* [x] Table-based format support (`.lxc` / `.lexc`)  
* [x] Safe fallback wrapper (`LocalizationFile` + `EmptyLXParser`)
* [ ] Add multi-line comments support
* [ ] Add escape sequences for special characters (`\|`, `\#`)

## LXP

* [ ] Finalize format specification and syntax rules (nested blocks, gender, and pluralization support)
* [ ] Implement LXP parser

# License

This project is licensed under the MIT license. See [LICENSE](LICENSE) file for details.