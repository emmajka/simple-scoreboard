package board

import exception.GameStartException
import game.Game
import game.GameId
import spock.lang.Specification

class ScoreboardSpec extends Specification {

    def scoreboardStorageMock = Mock(ScoreboardStorage)
    def sut = new Scoreboard(scoreboardStorageMock)

    def "when adding a new game then it should update games collection correctly"() {
        given:
        def gameId = GameId.builder().build()
        def game = Game.builder().gameId(gameId).build()

        when:
        sut.addGame(game)

        then:
        1 * scoreboardStorageMock.gameExists(game.getGameId()) >> false
        1 * scoreboardStorageMock.addGame(game)
    }

    def "when game already exists in storage then it should throw an exception - only one game in-progress for teams"() {
        given:
        def gameId = GameId.builder().build()
        def game = Game.builder().gameId(gameId).build()

        when:
        sut.addGame(game)

        then:
        1 * scoreboardStorageMock.gameExists(game.getGameId()) >> true
        thrown(GameStartException)
    }


    def "when finishing a game then it should be call storages removal function"() {
        given:
        def gameId = GameId.builder().build()

        when:
        sut.finishGame(gameId)

        then:
        1 * scoreboardStorageMock.removeGame(gameId)
    }

    def "when getting an empty scoreboard games then it should return an empty collection"() {
        given:
        def expected = Collections.emptyList()

        when:
        def actual = sut.getGames()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> expected
        actual == expected
    }

    def "when getting scoreboard games with different scores then it should return a collection sorted by total score descending"() {
        given:
        def game1 = Game.builder().gameId(GameId.builder().teamOne("1").teamTwo("2").build()).teamOneScore(2).teamTwoScore(1).build()
        def game2 = Game.builder().gameId(GameId.builder().teamOne("3").teamTwo("4").build()).teamOneScore(3).teamTwoScore(1).build()
        def game3 = Game.builder().gameId(GameId.builder().teamOne("5").teamTwo("6").build()).teamOneScore(0).teamTwoScore(1).build()

        def inputEntries = Arrays.asList(
                ScoreboardEntry.builder().game(game1).build(),
                ScoreboardEntry.builder().game(game2).build(),
                ScoreboardEntry.builder().game(game3).build()
        )
        def expected = Arrays.asList(game2, game1, game3)

        when:
        def actual = sut.getGames()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> inputEntries
        actual == expected
    }

    def "when getting scoreboard games with same scores then it should return a collection sorted by total score descending and creation date ascending"() {
        given:
        def game1 = Game.builder().gameId(GameId.builder().teamOne("1").teamTwo("2").build()).teamOneScore(2).teamTwoScore(1).build()
        def game2 = Game.builder().gameId(GameId.builder().teamOne("3").teamTwo("4").build()).teamOneScore(3).teamTwoScore(1).build()
        def game3 = Game.builder().gameId(GameId.builder().teamOne("5").teamTwo("6").build()).teamOneScore(0).teamTwoScore(1).build()
        def inputEntries = Arrays.asList(
                ScoreboardEntry.builder().game(game1).build(),
                ScoreboardEntry.builder().game(game2).build(),
                ScoreboardEntry.builder().game(game3).build()
        )

        def expected = Arrays.asList(game2, game1, game3)

        when:
        def actual = sut.getGames()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> inputEntries
        actual == expected
    }

    def "when getting scoreboard games with same total score values then it should return games by insertion descending order"() {
        given:
        def game1 = Game.builder().gameId(GameId.builder().teamOne("1").teamTwo("2").build()).teamOneScore(1).teamTwoScore(2).build()
        def game2 = Game.builder().gameId(GameId.builder().teamOne("3").teamTwo("4").build()).teamOneScore(2).teamTwoScore(1).build()
        def game3 = Game.builder().gameId(GameId.builder().teamOne("5").teamTwo("6").build()).teamOneScore(0).teamTwoScore(3).build()
        def inputEntries = Arrays.asList(
                ScoreboardEntry.builder().game(game1).insertionTime(1L).build(),
                ScoreboardEntry.builder().game(game3).insertionTime(2L).build(),
                ScoreboardEntry.builder().game(game2).insertionTime(3L).build()
        )
        def expected = Arrays.asList(game2, game3, game1)

        when:
        def actual = sut.getGames()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> inputEntries
        actual == expected
    }
}
