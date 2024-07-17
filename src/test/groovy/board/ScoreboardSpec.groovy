package board

import exception.GameStartException
import game.Game
import game.GameId
import spock.lang.Specification
import spock.lang.Unroll

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
        def actual = sut.getGamesSummary()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> expected
        actual == expected
    }

    def "when getting scoreboard games with different scores then it should return a collection sorted by total score descending"() {
        given:
        def sbe1 = buildSbe("poland", "brazil", 2, 1, 0)
        def sbe2 = buildSbe("Croatia", "Serbia", 3, 1, 0)
        def sbe3 = buildSbe("USA", "NRD", 0, 2, 0)
        def inputEntries = Arrays.asList(sbe2, sbe1, sbe3)

        def expected = Arrays.asList("Croatia 3 - Serbia 1", "poland 2 - brazil 1", "USA 0 - NRD 2")

        when:
        def actual = sut.getGamesSummary()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> inputEntries
        actual == expected
    }

    def "when getting scoreboard games with same scores then it should return a collection sorted by insertion date ascending order"() {
        given:
        def sbe1 = buildSbe("poland", "brazil", 2, 1, 0)
        def sbe2 = buildSbe("Croatia", "Serbia", 2, 1, 2)
        def sbe3 = buildSbe("USA", "NRD", 0, 3, 1)
        def inputEntries = Arrays.asList(sbe2, sbe1, sbe3)

        def expected = Arrays.asList("Croatia 2 - Serbia 1", "USA 0 - NRD 3", "poland 2 - brazil 1")

        when:
        def actual = sut.getGamesSummary()

        then:
        1 * scoreboardStorageMock.getAllEntries() >> inputEntries
        actual == expected
    }

    def "when updating game's score for game which is not on scoreboard then it should return false indicating failure at score update"() {
        given:
        def gameId = GameId.builder().build()

        when:
        def actual = sut.updateGameScore(gameId, 1, 1)

        then:
        1 * scoreboardStorageMock.getEntry(gameId) >> null
        !actual
    }

    @Unroll
    def "when updating game's score for an existing game on scoreboard then it should return true and game's score should be updated"() {
        given:
        def gameId = GameId.builder().build()
        def game = Game.builder().teamOneScore(0).teamTwoScore(0).build()
        def sbe = ScoreboardEntry.builder().game(game).build()

        when:
        def actual = sut.updateGameScore(gameId, teamOneScore, teamTwoScore)

        then:
        1 * scoreboardStorageMock.getEntry(gameId) >> sbe
        game.getTeamOneScore() == teamOneScore
        game.getTeamTwoScore() == teamTwoScore
        actual

        where:
        teamOneScore | teamTwoScore
        0            | 0
        1            | 1
        2            | 1
    }

    @Unroll
    def "when subtracting game's score of one of the teams then it should thrown an exception indicating an unwanted behavior"() {
        given:
        def gameId = GameId.builder().build()
        def game = Game.builder().teamOneScore(1).teamTwoScore(1).build()
        def sbe = ScoreboardEntry.builder().game(game).build()

        when:
        sut.updateGameScore(gameId, 0, 0)

        then:
        1 * scoreboardStorageMock.getEntry(gameId) >> sbe
        thrown(Exception)

        where:
        teamOneScore | teamTwoScore
        0            | 0
        1            | 0
        0            | 1
        -1           | 1
        1            | -1
    }

    private static def buildSbe(String teamOne, String teamTwo, int teamOneScore, int teamTwoScore, long insertionTime) {
        def gameId = GameId.builder().teamOne("1").teamTwo("2").build()
        def game = Game.builder().gameId(gameId).teamOne(teamOne).teamTwo(teamTwo).teamOneScore(teamOneScore).teamTwoScore(teamTwoScore).build()
        def sbe = ScoreboardEntry.builder().game(game).insertionTime(insertionTime).build()
        return sbe
    }
}
