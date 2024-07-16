import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Value
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GameId {
    String teamOne;
    String teamTwo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId = (GameId) o;
        return (Objects.equals(teamOne, gameId.teamOne) && Objects.equals(teamTwo, gameId.teamTwo))
                || (Objects.equals(teamOne, gameId.teamTwo) && Objects.equals(teamTwo, gameId.teamOne));
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamOne) + Objects.hash(teamTwo);
    }
}
