package board

import game.Game
import game.GameId
import spock.lang.Specification
import spock.lang.Unroll

class ScoreboardStorageSpec extends Specification {

    @Unroll
    def "game existence data set tests"() {
        given:
        def initialMap = new HashMap()
        def initialGameId = GameId.builder().teamOne("team1").teamTwo("team2").build()
        initialMap.put(initialGameId, null)
        def sut = new ScoreboardStorage(initialMap)

        when:
        def actual = sut.gameExists(gameId)

        then:
        actual == expected

        where:
        gameId                                                           | expected
        GameId.builder().teamOne("team1").teamTwo("team2").build()       | true
        GameId.builder().teamOne("team2").teamTwo("team1").build()       | true
        GameId.builder().teamOne("team 2").teamTwo("team1").build()      | false
        GameId.builder().teamOne("ewwweew").teamTwo("randndnnd").build() | false

    }

    def "when adding a new game it should call entries insertion method"() {
        given:
        def entriesMock = Mock(HashMap)
        def sut = new ScoreboardStorage(entriesMock)
        def gameId = GameId.builder().build()
        def game = Game.builder().gameId(gameId).build()

        when:
        sut.addGame(game)

        then:
        1 * entriesMock.put(gameId, _) >> {
            def sbe = it[1]
            assert sbe instanceof ScoreboardEntry
            assert sbe.getInsertionTime() > 1
        }
    }

    def "when removing a game it should call entries removal method"() {
        given:
        def entriesMock = Mock(HashMap)
        def sut = new ScoreboardStorage(entriesMock)
        def gameId = GameId.builder().build()

        when:
        sut.removeGame(gameId)

        then:
        1 * entriesMock.remove(gameId)
    }

    def "when getting empty entries it should map it to an empty collection"() {
        given:
        def entriesMock = Mock(HashMap)
        def sut = new ScoreboardStorage(entriesMock)

        when:
        def actual = sut.getAllEntries()

        then:
        1 * entriesMock.values() >> new ArrayList<>()
        actual.isEmpty()
    }

    def "when getting non-empty entries it should map it to a non-empty collection"() {
        given:
        def entriesMock = Mock(HashMap)
        def sut = new ScoreboardStorage(entriesMock)
        def expected = Arrays.asList(ScoreboardEntry.builder().build(), ScoreboardEntry.builder().build(), ScoreboardEntry.builder().build())

        when:
        def actual = sut.getAllEntries()

        then:
        1 * entriesMock.values() >> expected
        actual == expected
    }
}
