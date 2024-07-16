package game

import exception.IncorrectGameScoreException
import spock.lang.Specification
import spock.lang.Unroll

class GameSpec extends Specification {

    @Unroll
    def "when updating game score with valid values then it should update score properly"() {
        given:
        def game = Game.builder().teamOneScore(0).teamTwoScore(0).build()
        def expectedGame = Game.builder().teamOneScore(expectedTeamOneScore).teamTwoScore(expectedTeamTwoScore).build()

        when:
        game.updateScore(newTeamOneScore, newTeamTwoScore)

        then:
        game == expectedGame

        where:
        newTeamOneScore | newTeamTwoScore | expectedTeamOneScore | expectedTeamTwoScore
        0               | 0               | 0                    | 0
        1               | 1               | 1                    | 1
        0               | 1               | 0                    | 1
        123             | 23232           | 123                  | 23232
    }

    @Unroll
    def "when updating game score with negative values then it should throw an exception"() {
        given:
        def game = Game.builder().teamOneScore(0).teamTwoScore(0).build()

        when:
        game.updateScore(newTeamOneScore, newTeamTwoScore)

        then:
        thrown(IncorrectGameScoreException)

        where:
        newTeamOneScore | newTeamTwoScore
        -1              | 0
        1               | -1
        -1              | -1
        -123            | -23232
    }
}
