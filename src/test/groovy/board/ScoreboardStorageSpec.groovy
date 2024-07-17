package board

import game.Game
import game.GameId
import spock.lang.Specification

class ScoreboardStorageSpec extends Specification {

    def storageMapMock = Mock(HashMap<GameId, ScoreboardEntry>)
    def sut = new ScoreboardStorage(storageMapMock)

    def "when game is not found in the storage then it should return false"() {
        given:
        def gameId = GameId.builder().build()
        when:
        def actual = sut.gameExists(gameId)

        then:
        1 * storageMapMock.containsKey(gameId) >> false
        !actual
    }

    def "when game is found in the storage then it should return true"() {
        given:
        def gameId = GameId.builder().build()

        when:
        def actual = sut.gameExists(gameId)

        then:
        1 * storageMapMock.containsKey(gameId) >> true
        actual
    }

    def "when adding a new game then it should call entries insertion method"() {
        given:
        def gameId = GameId.builder().build()
        def game = Game.builder().gameId(gameId).build()

        when:
        sut.addGame(game)

        then:
        1 * storageMapMock.put(gameId, _) >> {
            def captured = it[1]
            assert captured instanceof ScoreboardEntry
            assert captured.getInsertionTime() > 0
            assert captured.getGame() == game
        }
    }

    def "when removing a game then it should call entries removal method"() {
        given:
        def gameId = GameId.builder().build()

        when:
        sut.removeGame(gameId)

        then:
        1 * storageMapMock.remove(gameId)
    }

    def "when getting empty entries then it should map it to an empty collection"() {
        when:
        def actual = sut.getAllEntries()

        then:
        1 * storageMapMock.values() >> Collections.emptyList()
        actual.isEmpty()
    }

    def "when getting non-empty entries then it should map it to a non-empty collection"() {
        given:
        def sbe1 = buildSbe("team 1", "team 2")
        def sbe2 = buildSbe("team 3", "team 4")
        def sbe3 = buildSbe("team 5", "team 6")
        def expected = Arrays.asList(sbe1, sbe2, sbe3)

        when:
        def actual = sut.getAllEntries()

        then:
        1 * storageMapMock.values() >> expected
        actual.containsAll(expected)
    }

    def "when getting a non-existing entry from storage then it should return a null value"() {
        given:
        def gameId = GameId.builder().build()

        when:
        def actual = sut.getEntry(gameId)

        then:
        1 * storageMapMock.get(gameId) >> null
        actual == null
    }

    def "when getting an existing entry from storage then it should return a an entry"() {
        given:
        def sbe = buildSbe("team 1", "team 2")

        when:
        def actual = sut.getEntry(sbe.getGame().getGameId())

        then:
        1 * storageMapMock.get(sbe.getGame().getGameId()) >> sbe
        actual == sbe
    }

    private static def buildSbe(String teamOne, String teamTwo) {
        def gameId = GameId.builder().teamOne("1").teamTwo("2").build()
        def game = Game.builder().gameId(gameId).teamOne(teamOne).teamTwo(teamTwo).build()
        def sbe = ScoreboardEntry.builder().game(game).build()
        return sbe
    }
}
