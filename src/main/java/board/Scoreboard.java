package board;

import exception.GameStartException;
import game.Game;
import game.GameId;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scoreboard {
    @Getter
    private final Map<GameId, Game> games;
    @Getter
    private final Map<GameId, ScoreboardEntry> entries;

    public Scoreboard() {
        games = new HashMap<>();
        entries = new HashMap<>();
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
        return entries.values().stream()
                .sorted((sbe1, sbe2) -> {
                    if (sbe1.getGame().getTotalScore() == sbe2.getGame().getTotalScore()) {
                        return Long.compare(sbe1.getInsertionTime(), sbe2.getInsertionTime());
                    } else if (sbe1.getGame().getTotalScore() < sbe2.getGame().getTotalScore()) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
                .map(ScoreboardEntry::getGame)
                .collect(Collectors.toList());
    }
}
