package game

import spock.lang.Specification

class GameFactorySpec extends Specification {
    def sut = new GameFactory()
    def dummyGameId = GameId.builder().teamOne("dummy team 1").teamTwo("dummy team 2").build()

    def "when creating a new valid game it should properly create a new game"() {
        given:
        def teamOne = "team 1"
        def teamTwo = "team 2"
        def expected = Game.builder()
                .gameId(dummyGameId)
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneScore(0)
                .teamTwoScore(0)
                .build()
        when:
        def actual = sut.create(teamOne, teamTwo, dummyGameId)

        then:
        actual == expected
    }
}
