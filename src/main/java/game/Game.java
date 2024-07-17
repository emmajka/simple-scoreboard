package game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Game {
    private GameId gameId;
    private String teamOne;
    private String teamTwo;
    private int teamOneScore;
    private int teamTwoScore;

    public int getTotalScore() {
        return teamOneScore + teamTwoScore;
    }

    public void updateScore(int newTeamOneScore, int newTeamTwoScore) {
        this.teamOneScore = newTeamOneScore;
        this.teamTwoScore = newTeamTwoScore;
    }
}
