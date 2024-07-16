package game;


public class GameIdBuilder {
    public GameId build(String teamOne, String teamTwo) {
        String teamOneNormalized = normalize(teamOne);
        String teamTwoNormalized = normalize(teamTwo);
        return GameId.builder().teamOne(teamOneNormalized).teamTwo(teamTwoNormalized).build();
    }

    private String normalize(String str) {
        return str.trim().toLowerCase();
    }
}
