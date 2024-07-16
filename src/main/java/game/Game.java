package game;

import exception.GameScoreUpdateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Game {
    private GameId gameId;
    private String teamOne;
    private String teamTwo;
    private int teamOneScore;
    private int teamTwoScore;

    public void updateScore(int newTeamOneScore, int newTeamTwoScore) throws GameScoreUpdateException {
        if (newTeamOneScore < 0 || newTeamTwoScore < 0) {
            var errMsg = String.format("%s vs %s game score could not be updated! Setting negative values is prohibited!", newTeamOneScore, newTeamTwoScore);
            throw new GameScoreUpdateException(errMsg);
        }
        if (newTeamOneScore < teamOneScore || newTeamTwoScore < teamTwoScore) {
            var errMsg = String.format("%s vs %s game score could not be updated! Subtracting score values is prohibited!", newTeamOneScore, newTeamTwoScore);
            throw new GameScoreUpdateException(errMsg);
        }
        this.teamOneScore = newTeamOneScore;
        this.teamTwoScore = newTeamTwoScore;
    }
}
