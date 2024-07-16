package game;


import exception.GameIdCreationException;

import java.util.Objects;

public class GameIdBuilder {
    public GameId build(String teamOne, String teamTwo) throws GameIdCreationException {
        String teamOneNormalized = normalize(teamOne);
        String teamTwoNormalized = normalize(teamTwo);
        if (Objects.equals(teamOneNormalized, teamTwoNormalized)) {
            var errMsg = String.format("Failed to create a game ID for %s vs %s game, names are duplicates!", teamOne, teamTwo);
            throw new GameIdCreationException(errMsg);
        }
        return GameId.builder().teamOne(teamOneNormalized).teamTwo(teamTwoNormalized).build();
    }

    private String normalize(String str) {
        return str.trim().toLowerCase();
    }
}
