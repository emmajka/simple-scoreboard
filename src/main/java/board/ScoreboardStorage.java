package board;

import game.Game;
import game.GameId;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ScoreboardStorage {

    private final Map<GameId, ScoreboardEntry> entries;

    public boolean gameExists(GameId gameId) {
        return entries.containsKey(gameId);
    }

    public void addGame(Game game) {
        var sbe = ScoreboardEntry.builder().game(game).insertionTime(System.currentTimeMillis()).build();
        entries.put(game.getGameId(), sbe);
    }

    public void removeGame(GameId gameId) {
        entries.remove(gameId);
    }

    public List<ScoreboardEntry> getAllEntries() {
        return entries.values().stream().toList();
    }

    public ScoreboardEntry getEntry(GameId gameId) {
        return entries.get(gameId);
    }
}
