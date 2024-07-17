package board;

import exception.GameScoreUpdateException;
import exception.GameStartException;
import game.Game;
import game.GameId;
import game.GameSummary;

import java.util.List;
import java.util.stream.Collectors;

public class Scoreboard {

    private final ScoreboardStorage scoreboardStorage;

    public Scoreboard(ScoreboardStorage scoreboardStorage) {
        this.scoreboardStorage = scoreboardStorage;
    }

    public void addGame(Game game) throws GameStartException {
        var gameId = game.getGameId();
        if (scoreboardStorage.gameExists(gameId)) {
            var errMsg = String.format(
                    "%s vs %s game is already in progress! Unable to start new game if there is an existing game in-progress!",
                    game.getTeamOne(), game.getTeamTwo()
            );
            throw new GameStartException(errMsg);
        }
        scoreboardStorage.addGame(game);
    }

    public void finishGame(GameId gameId) {
        scoreboardStorage.removeGame(gameId);
    }

    public List<String> getGamesSummary() {
        return scoreboardStorage.getAllEntries().stream()
                .sorted((sbe1, sbe2) -> {
                    if (sbe1.getGame().getTotalScore() == sbe2.getGame().getTotalScore()) {
                        return Long.compare(sbe2.getInsertionTime(), sbe1.getInsertionTime());
                    } else if (sbe1.getGame().getTotalScore() < sbe2.getGame().getTotalScore()) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
                .map(ScoreboardEntry::getGame)
                .map(game -> GameSummary.builder()
                        .teamOne(game.getTeamOne())
                        .teamTwo(game.getTeamTwo())
                        .teamOneScore(game.getTeamOneScore())
                        .teamTwoScore(game.getTeamTwoScore())
                        .build())
                .map(GameSummary::display)
                .collect(Collectors.toList());
    }

    public boolean updateGameScore(GameId gameId, int teamOneScore, int teamTwoScore) throws GameScoreUpdateException {
        ScoreboardEntry existingEntry = scoreboardStorage.getEntry(gameId);
        if (existingEntry == null) {
            return false;
        }
        var game = existingEntry.getGame();
        if (teamOneScore < game.getTeamOneScore() || teamTwoScore < game.getTeamTwoScore()) {
            var errMsg = String.format("%s vs %s game score could not be updated! New score values can't be lesser then existing values!", teamOneScore, teamTwoScore);
            throw new GameScoreUpdateException(errMsg);
        }
        game.setTeamOneScore(teamOneScore);
        game.setTeamTwoScore(teamTwoScore);
        return true;
    }
}
