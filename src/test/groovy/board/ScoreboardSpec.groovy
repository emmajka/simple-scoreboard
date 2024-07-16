package board

import exception.GameStartException
import game.Game
import game.GameId
import spock.lang.Specification

class ScoreboardSpec extends Specification {

    def "when adding a new game then it should update games collection correctly"() {
        given:
        def sb = new Scoreboard()
        def game = Game.builder().teamOne("Team 1").teamTwo("Team 2").build()

        when:
        sb.addGame(game)

        then:
        sb.getGames().size() == 1
        sb.getGames().get(game.getGameId()) == game
    }

    def "when adding same game more then once then it should throw an exception - only one game in-progress for teams"() {
        given:
        def sb = new Scoreboard()
        def team1 = "Team 1"
        def team2 = "Team 2"
        def gameId = GameId.builder().teamOne(team1).teamTwo(team2).build()
        def game = Game.builder().gameId(gameId).teamOne(team1).teamTwo(team2).build()

        when:
        sb.addGame(game)
        sb.addGame(game)

        then:
        thrown(GameStartException)
    }

    def "when adding same game more then once with names switched then it should throw an exception - only one game in progress for teams, team position should not matter"() {
        given:
        def sb = new Scoreboard()
        def team1 = "Team 1"
        def team2 = "Team 2"

        def gameId = GameId.builder().teamOne(team1).teamTwo(team2).build()
        def game = Game.builder().gameId(gameId).teamOne(team1).teamTwo(team2).build()

        def gameIdAlt = GameId.builder().teamOne(team2).teamTwo(team1).build()
        def gameAlt = Game.builder().gameId(gameIdAlt).teamOne(team1).teamTwo(team2).build()

        when:
        sb.addGame(game)
        sb.addGame(gameAlt)

        then:
        thrown(GameStartException)
    }

    def "when finishing a game then it should be removed from the games score board"() {
        given:
        def sb = new Scoreboard()
        def team1 = "Team 1"
        def team2 = "Team 2"
        def gameId = GameId.builder().teamOne(team1).teamTwo(team2).build()
        def game = Game.builder().gameId(gameId).teamOne(team1).teamTwo(team2).build()

        when:
        sb.getGames().put(gameId, game)

        then:
        sb.getGames().size() == 1

        when:
        sb.finishGame(gameId)

        then:
        sb.getGames().isEmpty()
    }

    def "when finishing a non-existent game then it scoreboard games state should not change"() {
        given:
        def sb = new Scoreboard()
        def team1 = "Team 1"
        def team2 = "Team 2"
        def gameId = GameId.builder().teamOne(team1).teamTwo(team2).build()
        def game = Game.builder().gameId(gameId).teamOne(team1).teamTwo(team2).build()
        def fakeGameId = GameId.builder().teamOne("some fake value").teamTwo("other fake value").build()

        when:
        sb.getGames().put(gameId, game)

        then:
        sb.getGames().size() == 1

        when:
        sb.finishGame(fakeGameId)

        then:
        sb.getGames().size() == 1
        sb.getGames().get(gameId) == game
    }

    def "when getting an empty scoreboard games then it should return an empty collection"() {
        given:
        def sb = new Scoreboard()
        def expected = Collections.emptyList()

        when:
        def actual = sb.getScores()

        then:
        actual == expected
    }

    def "when getting scoreboard games with different scores then it should return a collection sorted by total score"() {
        given:
        def game1 = Game.builder().gameId(GameId.builder().teamOne("1").teamTwo("2").build()).teamOneScore(2).teamTwoScore(1).build()
        def game2 = Game.builder().gameId(GameId.builder().teamOne("3").teamTwo("4").build()).teamOneScore(3).teamTwoScore(1).build()
        def game3 = Game.builder().gameId(GameId.builder().teamOne("5").teamTwo("6").build()).teamOneScore(0).teamTwoScore(1).build()
        def inputGames = new HashMap()
        inputGames.put(game1.getGameId(), game1)
        inputGames.put(game2.getGameId(), game2)
        inputGames.put(game3.getGameId(), game3)

        def expected = Arrays.asList(game2, game1, game3)

        def sb = new Scoreboard()
        sb.games.putAll(inputGames)

        when:
        def actual = sb.getScores()

        then:
        actual == expected
    }
}
