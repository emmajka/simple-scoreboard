package board;

import game.Game;
import game.GameId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ScoreboardEntry {
    private Game game;
    private long insertionTime;

}
