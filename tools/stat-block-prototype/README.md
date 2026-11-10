# Beholder stat-block image prototype

This standalone Java tool takes one cropped JPEG or PNG, asks Amazon Bedrock to transcribe it, and prints a validated creature draft and review warnings. The draft is an intermediate format. Actions use a list until a later import step assigns IDs and converts them to Beholder's action map.

## Run it

From the repository root, use the Gradle wrapper with JDK 17. On Windows, use `gradlew.bat` in place of `./gradlew`. Run the commands from a shell with AWS CLI v2 on PATH and an authenticated AWS profile with Bedrock inference access. Use an image-capable Bedrock model or inference profile that supports Converse structured output in the selected region.

```sh
./gradlew -p tools/stat-block-prototype test
./gradlew -q -p tools/stat-block-prototype run --args='--validate fixtures/draugr-reference.json'
./gradlew -q -p tools/stat-block-prototype run --args='"/absolute/path/to/cropped-stat-block.jpg" us-east-2 MODEL_OR_PROFILE_ID'
```

Crop to a single stat block before running. The tool accepts files up to 3.75 MB and 8000 pixels on either side. A live run sends the image to Bedrock and can incur inference charges. The tool uses the AWS CLI credential chain, writes its request and response in a temporary directory, and removes those files after the command finishes.

## Initial evaluation

The checked-in Draugr fixture is a manually annotated reference with abbreviated trait descriptions. The original image remains outside the repository. The fixture covers AC 13, HP 52, a 25 ft. walk speed, CR 2, distinct damage and condition immunities, and seven traits and actions. The tests check Claw's attack and damage, the two Battleaxe formulas, and Hateful Glare's recharge. They also cover missing HP, invalid output, a truncated model response, and request construction for both image formats.

These checks measure schema and pipeline behavior. Live extraction accuracy, latency, and cost are pending. For the next evaluation, compare live output with the Draugr reference and add a clean screenshot, a phone photo, a spellcaster, and a legendary creature. Record whether each field is correct, missing, or wrong, and check whether review warnings catch the errors.
