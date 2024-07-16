package board;

import exception.GameStartException;
import game.Game;
import game.GameId;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scoreboard {
    @Getter
    private final Map<GameId, Game> games;

    public Scoreboard() {
        games = new HashMap<>();
    }

    public void addGame(Game game) throws Exception {
        var gameId = game.getGameId();
        if (games.get(gameId) != null) {
            var errMsg = String.format(
                    "%s vs %s game is already in progress! Unable to start new game if there is an existing game in-progress!",
                    game.getTeamOne(), game.getTeamTwo()
            );
            throw new GameStartException(errMsg);
        }

        games.put(gameId, game);
    }

    public void finishGame(GameId gameId) {
        var existingGame = games.get(gameId);
        if (existingGame != null) {
            games.remove(gameId);
        }
    }

    public List<Game> getScores() {
        return games.values().stream()
                .sorted(Comparator.comparingInt(Game::getTotalScore).reversed())
                .collect(Collectors.toList());
    }
}
