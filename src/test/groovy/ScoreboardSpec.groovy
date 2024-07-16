import exception.GameStartException
import spock.lang.Specification
import spock.lang.Unroll

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
    
    def "when adding same game more then once with names switched then it should throw an exception - only one game in progress for teams, team position should not matter"(){
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
}
