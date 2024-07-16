import board.Scoreboard
import board.ScoreboardEntry
import board.ScoreboardStorage
import exception.GameStartException
import game.GameFactory
import game.GameId
import game.GameIdFactory
import spock.lang.Specification

class ScoreboardFlowBDD extends Specification {
    def "scoreboard scenario nr 1"() {
        given: "a new, empty scoreboard"
        def storageMap = new HashMap<GameId, ScoreboardEntry>()
        def sbs = new ScoreboardStorage(storageMap)
        def sb = new Scoreboard(sbs)

        when: "adding a new, unique game"
        def game1Team1 = "CroAtiA"
        def game1Team2 = "BraziL"
        def game1id = new GameIdFactory().create(game1Team1, game1Team2)
        def game1 = new GameFactory().create(game1Team1, game1Team2, game1id)
        sb.addGame(game1)

        then: "a new game should be added successfully to the storage"
        storageMap.get(game1id).getGame() == game1
        storageMap.size() == 1

        when: "adding new, unique games"
        def game2Team1 = "Poland"
        def game2Team2 = "GermMany"
        def game2id = new GameIdFactory().create(game2Team1, game2Team2)
        def game2 = new GameFactory().create(game2Team1, game2Team2, game2id)
        sb.addGame(game2)
        def game3Team1 = "France"
        def game3Team2 = "britain"
        def game3id = new GameIdFactory().create(game3Team1, game3Team2)
        def game3 = new GameFactory().create(game3Team1, game3Team2, game3id)
        sb.addGame(game3)

        then: "new games should be added successfully to the storage"
        storageMap.get(game2id).getGame() == game2
        storageMap.get(game3id).getGame() == game3
        storageMap.size() == 3

        when: "finishing an in-progress game"
        sb.finishGame(game2id)

        then: "game should be removed from storage"
        storageMap.size() == 2
        storageMap.get(game2id) == null

        when: "finishing a finished game"
        sb.finishGame(game2id)

        then: "storage state should not change"
        storageMap.size() == 2
        storageMap.get(game2id) == null

        when: "updating a score on an in-progress game"
        game1.updateScore(1, 2)

        then: "games score should be updated in the storage"
        game1.getTeamOneScore() == 1
        game1.getTeamTwoScore() == 2

        and: "other game states should not be modified"
        game3.getTeamOneScore() == 0
        game3.getTeamTwoScore() == 0

        when: "adding removed game again"
        sb.addGame(game2)

        then: "game should be added successfully to the storage"
        storageMap.get(game2id).getGame() == game2
        storageMap.size() == 3

        when: "trying to add same game twice"
        sb.addGame(game2)

        then: "an error should be thrown"
        thrown(GameStartException)
    }
}
