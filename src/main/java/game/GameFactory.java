package game;


public class GameFactory {
    public Game create(String teamOne, String teamTwo, GameId gameId) {
        return Game.builder()
                .gameId(gameId)
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneScore(0)
                .teamTwoScore(0)
                .build();
    }
}
