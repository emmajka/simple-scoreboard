package game;


public class GameFactory {
    public Game create(String teamOne, String teamTwo) {
        return Game.builder().build();
    }
}
