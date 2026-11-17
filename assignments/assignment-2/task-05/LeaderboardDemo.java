import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Q5: Generic leaderboard with reusable Comparator-based ranking. */
public class LeaderboardDemo {
    static final class Team {
        final String name;
        final int points;
        final int goalDiff;

        Team(String name, int points, int goalDiff) {
            this.name = name;
            this.points = points;
            this.goalDiff = goalDiff;
        }

        @Override
        public String toString() {
            return name + "(" + points + "," + goalDiff + ")";
        }
    }

    static final class Leaderboard<T> {
        private final List<T> items = new ArrayList<>();
        private final Comparator<? super T> order;

        Leaderboard(Comparator<? super T> order) {
            this.order = order;
        }

        void add(T item) {
            items.add(item);
        }

        List<T> ranking() {
            List<T> sorted = new ArrayList<>(items);
            sorted.sort(order);
            return sorted;
        }

        void addAll(List<? extends T> incoming) {
            items.addAll(incoming);
        }
    }

    public static void main(String[] args) {
        Comparator<Team> byRank = Comparator
                .comparingInt((Team team) -> team.points).reversed()
                .thenComparing(Comparator.comparingInt(
                        (Team team) -> team.goalDiff).reversed())
                .thenComparing(team -> team.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());
    }
}
