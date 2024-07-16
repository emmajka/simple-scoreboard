import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Scoreboard {
    @Getter
    private final Map<String, Game> games;

    public Scoreboard() {
        games = new HashMap<>();
    }

    public void addGame(Game game) {

    }
}
