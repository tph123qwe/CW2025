GitHub Link: https://github.com/tph123qwe/CW2025

Compilation Instructions: Open Maven Panel --> CW2025 --> Plugins --> javafx --> javafx:run

Implemented and Working Properly: 
- Added a Restart Button after failing the level.
- Added Hard Drop with Space Bar
- Added Soft Drop by holding down S or the Down Arrow Key
- Changed Block Spawning Position Higher
- Added a Pause Menu after pressing ESC
- Added Hold Function by pressing the C Key
- Added Ghost Blocks
- Added HUD to show next 2 blocks, show block that is being held and score
- CSS Styling
- Scoring System
- Better Random Brick Generation (Get each piece exactly once before seeing repeats)
- Added a Speed System (Speed increases every 10 pieces placed)

Implemented but Not Working Properly: None

Features Not Implemented:
- Music (took way too long)
- Multiplayer (way too difficult and not worth it)

New Java Classes:
PausePanel.java (for the Pause Menu to be implemented)

Modified Java Classes:
RandomBrickGenerator.java 
EventType.java
GameController.java
GameOverPanel.java
GuiController.java
InputEventListener.kava
Main.java
SimpleBoard.java
ViewData.java

Unexpected Problems: 
- Tried to add Ghost Blocks to show the location of a block before it is placed and it messed up my entire game. Blocks would spawn and place themselves in the air even after i resetted the changes. Had to delete my progress and start from scratch.
- Could not get the HUD to show the next 3rd block, so i scrapped it.
