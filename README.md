# minecraft-various-food-mod

This is a Minecraft (Fabric) mod that adds new food items and craftable recipes, as well as a debuff system based on the player's food intake.

## Features
- **Weight/Intake Management System**: Adds an NBT tag named "body_weight" to the player.
- **Weight Fluctuation via Diet**: Consuming food alters the `body_weight` value.
- **Debuff Effects**: Exceeding a certain `body_weight` threshold triggers effects such as hallucinations and damage.
- **New Foods and Crops**: Adds a wide variety of new food items and cultivable crops.


## Operating environment
- **Minecraft**: 1.20.1
- **Mod Loader**: Fabric
- **Java**: 17

## License
MIT License

## Development Setup
1. git clone https://github.com/minghongsangu10-star/minecraft-various-food-mod.git
2. ./gradlew genSources
3. ./gradlew build