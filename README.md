Development:
  week1:
  - Game plan
  - Role distribution
  - determining the libraries that will be used

  week2:
  - No Meeting
  - Stealth Game: Tiled map created, box2d player droid generated, animationHandler class added, game atlas has been created, tiled object parser added
  - Main Game: Tiled map designed and imported into the project.
  - Basic Box2D-based player character implemented.
    Created AnimationHandler class to manage character animations dynamically.
  - Light Puzzle Game:
  - Detective Game: Initial game idea discussed (crime scene + interrogation phase).
    Core game flow planned and divided into two phases.
    Evidence and Suspect classes designed.
  - Menu Frames:

  week3:
  - discussion on game physics (stealth and main game)
  - Stealth Game: LightCones added, droids are chasing a fixed point, player movement fixed, input listener added
  - Main Game:
  - Enhanced Tiled map system: added collision objects and metadata for interactive tiles.
  - Improved character controller and refined Box2D body properties.
  - Added support for directional movement and animation transitions.
  - Light Puzzle Game:
  - Detective Game:CrimeSceneScreen created, background added, evidence item design initiated.
    InputProcessor logic designed for evidence detection.
    Timer-based gameplay logic planned.
  - Menu Frames:

  week4:
  - No Meeting
  - Stealth Game: cctv class added, ray casting algorithm added for droid and cctv, game atlas updated, ray renderer added
  - Main Game:Built main menu, pause screen, and game over screen using Scene2D.
  - Implemented navigation logic between scenes with transition effects.
  - Refactored screen management to use a central ScreenController.
  - Light Puzzle Game:
  - Detective Game:Evidence detection mechanics implemented.
    EvidencePanel designed to display found clues.
    Timer integrated into UI (90 seconds limit).
    Evidence and CrimeSceneScreen interaction completed.
  - Menu Frames:
  
  week5:
  - collisionListener discussed, pause menu is added to other games, MyContactListener discussed
  - Stealth Game: collision system added, death system added, input processor multiplier added, doors added
  - Main Game:Developed JSON-based serialization system for player progress, position, and collected items.
  - Implemented basic file I/O system using LibGDX's Json class.
  - Created player state object and tested save/load across game sessions.
  - Light Puzzle Game:
  - Detective Game:InterrogationScreen created with suspect portraits.
    BlameButton implemented under each suspect.
    Player can select suspects for accusation.
    Correct/Incorrect accusation logic added.
  - Menu Frames:

  week6:
  - Games are merged together
  - Major asset handling problems solved
  - Stealth Game: droid attack animation added, droid movement fixed, droid animation fixed, player detection problems solved( cctv, droid), furnaces added, furnace functionality added, droids are multiplied
  - Main Game: Integrated two mini-games into the main game loop with condition-based unlocking.
  - Expanded save/load system to support multiple save slots and game flags.
  - Addressed edge cases in loading mid-animation or during scene transitions.
  - Light Puzzle Game:
  - Detective Game:GameEnd logic completed — transitions to Win/Loss screen based on player's choice.
    VictoryScreen and GameOverScreen created.
    Visual polish applied to InterrogationScreen and final flow fully integrated.
  - Menu Frames:
