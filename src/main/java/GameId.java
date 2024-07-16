import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GameId {
    String teamOne;
    String teamTwo;
}
