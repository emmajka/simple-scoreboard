package game

import spock.lang.Specification

class GameFactorySpec extends Specification {
    def sut = new GameFactory()

    def "when creating a new valid game it should properly create a new game"() {
        given:
        def teamOne = "team 1"
        def teamTwo = "team 2"
        def expectedGameId = GameId.builder().teamOne(teamOne).teamTwo(teamTwo).build()
        def expected = Game.builder()
                .gameId(expectedGameId)
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneScore(0)
                .teamTwoScore(0)
                .build();
        when:
        def actual = sut.create(teamTwo, teamTwo)

        then:
        actual == expected
    }
}
