package game;


import exception.GameIdCreationException;

import java.util.Objects;
import java.util.Optional;

public class GameIdBuilder {
    public GameId build(String teamOne, String teamTwo) throws GameIdCreationException {
        String teamOneNormalized = normalize(teamOne);
        String teamTwoNormalized = normalize(teamTwo);
        if (teamOne == null || teamOne.isEmpty() || teamTwo == null || teamTwo.isEmpty()) {
            var errMsg = String.format("Failed to create a game ID for %s vs %s game, names are malformed!", teamOne, teamTwo);
            throw new GameIdCreationException(errMsg);
        }
        if (Objects.equals(teamOneNormalized, teamTwoNormalized)) {
            var errMsg = String.format("Failed to create a game ID for %s vs %s game, names are duplicates!", teamOne, teamTwo);
            throw new GameIdCreationException(errMsg);
        }
        return GameId.builder().teamOne(teamOneNormalized).teamTwo(teamTwoNormalized).build();
    }

    private String normalize(String str) {
        return Optional.ofNullable(str).map(s -> s.trim().toLowerCase()).orElse(str);
    }
}
