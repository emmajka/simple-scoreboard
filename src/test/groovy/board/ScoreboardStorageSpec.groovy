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
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def sut = new ScoreboardStorage(initialMap)
        def gameId = GameId.builder().build()
        def game = Game.builder().gameId(gameId).build()

        when:
        sut.addGame(game)

        then:
        initialMap.size() == 1
        initialMap.get(gameId).getGame() == game
    }

    def "when removing a game it should call entries removal method"() {
        given:
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def gameId = GameId.builder().build()
        initialMap.put(gameId, null)
        def sut = new ScoreboardStorage(initialMap)

        when:
        sut.removeGame(gameId)

        then:
        initialMap.isEmpty()
    }

    def "when getting empty entries it should map it to an empty collection"() {
        given:
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def sut = new ScoreboardStorage(initialMap)

        when:
        def actual = sut.getAllEntries()

        then:
        actual.isEmpty()
    }

    def "when getting non-empty entries it should map it to a non-empty collection"() {
        given:
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def game1 = Game.builder().gameId(GameId.builder().teamOne("1").build()).build()
        def game2 = Game.builder().gameId(GameId.builder().teamOne("2").build()).build()
        def game3 = Game.builder().gameId(GameId.builder().teamOne("3").build()).build()
        def sbe1 = ScoreboardEntry.builder().game(game1).build()
        def sbe2 = ScoreboardEntry.builder().game(game2).build()
        def sbe3 = ScoreboardEntry.builder().game(game3).build()
        initialMap.put(game1.getGameId(), sbe1)
        initialMap.put(game2.getGameId(), sbe2)
        initialMap.put(game3.getGameId(), sbe3)
        def sut = new ScoreboardStorage(initialMap)

        def expected = Arrays.asList(sbe1, sbe2, sbe3)

        when:
        def actual = sut.getAllEntries()

        then:
        actual.containsAll(expected)
    }

    def "when getting a non-existing entry from storage then it should return a null value"() {
        given:
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def sut = new ScoreboardStorage(initialMap)

        when:
        def actual = sut.getEntry(GameId.builder().build())

        then:
        actual == null
    }

    def "when getting an existing entry from storage then it should return a an entry"() {
        given:
        def initialMap = new HashMap<GameId, ScoreboardEntry>()
        def sbe = buildSbe("team 1", "team 2")
        initialMap.put(sbe.getGame().getGameId(), sbe)
        def sut = new ScoreboardStorage(initialMap)

        when:
        def actual = sut.getEntry(sbe.getGame().getGameId())

        then:
        actual == sbe
    }

    private static def buildSbe(String teamOne, String teamTwo) {
        def gameId = GameId.builder().teamOne("1").teamTwo("2").build()
        def game = Game.builder().gameId(gameId).teamOne(teamOne).teamTwo(teamTwo).build()
        def sbe = ScoreboardEntry.builder().game(game).build()
        return sbe
    }
}
