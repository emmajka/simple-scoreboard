# Simple scoreboard project

## Assumptions
1. It is only possible to update score of a non-finished game
2. Finishing game equals to its removal from the scoreboard's internal collection
3. Game's summary return type is a plain formatted String
4. 
## How to

### Create a new scoreboard
In order to create a new scoreboard from scratch one needs to provide an internal storage which, for simplicity, is defined as a map internally:

        var scoreboardStorage = new ScoreboardStorage(new HashMap<>());
        var scoreboard = new ScoreBoard(scoreboardStorage)

Scoreboard can be pre-filled by providing a non-empty map instance:
        
        var initialMap = new HashMap<>()
        initialMap.put(..., ...) // put Your values however You see fit
        var scoreboardStorage = new ScoreboardStorage(initialMap);
        var scoreboard = new ScoreBoard(scoreboardStorage)
        
Note: it does not validate on duplicates and other rules, using a pre-initialized map can cause unwanted behavior.

### Add a new game to scoreboard
In order to add a new game to an existing scoreboard a `addGame` method can be used, it allows to safely add a new game to scoreboard.

        // create a new game ID, each game is identified by its unique game ID 
        var gameIdFactory = new GameIdFactory() // default implementation of game ID factory
        var gameId = gameIdFactory.create(${teamOne}, ${teamTwo}) // useteam names as input parameters
        
        // create new game 
        var gameFactory = new GameFactory() // default implementation of game factory
        var game = gameFactory.create("team1", "team2", gameId)

        // create a new scoreboard or re-use existing one, provide necessary dependences
        var scoreboard = new Scoreboard(${dependencies})
        scoreboard.addGame(game)

Game instance requires a unique GameId instance, in ensures that a game will meet requirements for storage.
Creation of game and its game ID is de-coupled, it gives control to user on how he would like to create a game ID. 
It is recommended to use team names for it or some unique string values for input string pair.

### Finish a game
Finishing a game equals to its removal of scoreboard storage. If game does not exist in the storage nothing will happen.

        // create a new scoreboard or re-use existing one, provide necessary dependences
        var scoreboard = new Scoreboard(${dependencies})
        scoreboard.finishGame(${gameId}) // use Your game ID as input

### Update game's score
Game instance are mutable by definition, in order to update their score they need to be accessed and modified.

        // get a game instance, run updateScore on it
        var scoreboard = new Scoreboard(${dependencies})
        var gameId = scoreboard.getEntry(${gameId}).getGame().getGameId()
        scoreboard.updateGameScore(gameId, ${teamOneScore}, ${teamTwoScore}) // use Your game ID as input

### Get games summary
Game summary can be retrieved for all in-progress games

        var scoreboard = new Scoreboard(${dependencies})
        var gameId = scoreboard.getGamesSummary()
