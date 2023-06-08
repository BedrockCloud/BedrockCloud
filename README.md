# Requirements
- Java 17 or higher
- Debian 10 or higher
- At least 4 vCores and 8 gigabytes ram

# Install
- Create a folder for the Cloud
- Download the PHP binary (PM5 binary) from https://github.com/pmmp/PHP-Binaries/releases.
- Put the "bin" directory in the Cloud folder
- Make a start file with this content: `java -jar BedrockCloud.jar`
- Give the start file permissions and execute the script
- Start the cloud for the first time
- Download the CloudBridge for PocketMine (https://github.com/BedrockCloud/CloudBridge-PM) and for WaterdogPE (https://github.com/BedrockCloud/CloudBridge-WD).
- Put the PocketMine Cloudbridge into `local/plugins/pocketmine/` and the WaterdogPE CloudBridge into `local/plugins/waterdogpe/`.
- Now you can use the cloud.

# Extra
- If you want a SignSystem for your lobby, download https://github.com/BedrockCloud/CloudSignSystem and put it on your lobby template in the plugin folder.

# Important
- **We now only support PM5 (PocketMine API5). Support for older PocketMine versions has been discontinued.**