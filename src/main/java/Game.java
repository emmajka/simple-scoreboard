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
    @Builder.Default
    private int teamOneScore = 0;
    @Builder.Default
    private int teamTwoScore = 0;
}
