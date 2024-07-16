import spock.lang.Specification

class ScoreboardSpec extends Specification {

    def "when adding a new game it should update games collection correctly"() {
        given:
        var sb = new Scoreboard()
        var game = Game.builder().teamOne("Team 1").teamTwo("Team 2").build()

        when:
        sb.addGame(game)

        then:
        sb.getGames().size() == 1
        sb.getGames().get("Team 1 vs Team 2") == game
    }
}
