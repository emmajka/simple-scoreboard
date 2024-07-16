package game

import exception.GameIdCreationException
import spock.lang.Specification
import spock.lang.Unroll

class GameIdBuilderSpec extends Specification {
    def sut = new GameIdBuilder()

    @Unroll
    def "when creating a game ID for #inputTeamOne vs #inputTeamTwo game then it should build a normalized game ID"() {
        when:
        def actual = sut.build(inputTeamOne, inputTeamTwo)
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

    def "when creating a game ID with duplicated normalized team names then it should throw an exception"() {
        when:
        sut.build(team1, team2)

        then:
        thrown(GameIdCreationException)

        where:
        team1     | team2
        "team1"   | "team1"
        "Team1  " | "Team1"
        "  tEAm1" | "    TEAM1 "
    }

    def "when creating a game ID with malformed team names then it should throw an exception"(){
        when:
        sut.build(team1, team2)

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
