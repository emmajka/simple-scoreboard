package game;


public class GameIdBuilder {
    public GameId build(String teamOne, String teamTwo) {
        return GameId.builder().teamOne(teamOne).teamTwo(teamTwo).build();
    }
}
