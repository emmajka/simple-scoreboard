package game;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class GameSummary {
    String teamOne;
    String teamTwo;
    int teamOneScore;
    int teamTwoScore;

    public String display() {
        return String.format("%s %d - %s %d", teamOne, teamOneScore, teamTwo, teamTwoScore);
    }
}
