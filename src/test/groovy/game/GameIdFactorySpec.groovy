package game

import exception.GameIdCreationException
import spock.lang.Specification
import spock.lang.Unroll

class GameIdFactorySpec extends Specification {
    def sut = new GameIdFactory()

    @Unroll
    def "when creating a game ID for #inputTeamOne vs #inputTeamTwo game then it should build a normalized game ID"() {
        when:
        def actual = sut.create(inputTeamOne, inputTeamTwo)
        def expected = GameId.builder().teamOne(expectedTeamOne).teamTwo(expectedTeamTwo).build()

        then:
        actual == expected

        where:
        inputTeamOne | inputTeamTwo | expectedTeamOne | expectedTeamTwo
        "team1"      | "team2"      | "team1"         | "team2"
        "team 1"     | "team 2"     | "team 1"        | "team 2"
        " team 1 "   | " team 2"    | "team 1"        | "team 2"
        "TeaM 1"     | "TEAM 2"     | "team 1"        | "team 2"
    }

    @Unroll
    def "when creating a game ID with duplicated normalized team names then it should throw an exception"() {
        when:
        sut.create(team1, team2)

        then:
        thrown(GameIdCreationException)

        where:
        team1     | team2
        "team1"   | "team1"
        "Team1  " | "Team1"
        "  tEAm1" | "    TEAM1 "
    }

    @Unroll
    def "when creating a game ID with malformed team names then it should throw an exception"(){
        when:
        sut.create(team1, team2)

        then:
        thrown(GameIdCreationException)

        where:
        team1 | team2
        null  | null
        ""    | null
        null  | ""
        ""    | ""
        "   "    | ""
        ""    | "   "
        "  "    | "   "
    }
}
