package game

import spock.lang.Specification

class GameSpec extends Specification {

    def "when updating game score with valid values then it should update score properly"() {
        given:
        def game = Game.builder().teamOneScore(0).teamTwoScore(0).build()
        def expectedGame = Game.builder().teamOneScore(1).teamTwoScore(1).build()

        when:
        game.updateScore(1, 1)

        then:
        game == expectedGame
    }
}
