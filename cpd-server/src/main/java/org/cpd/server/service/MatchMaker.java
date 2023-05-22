package org.cpd.server.service;

import org.cpd.shared.Config;
import org.cpd.shared.Pair;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//TODO concurrency control
public class MatchMaker {
    private static Config config;
    private static final List<Pair<Pair<Integer, SocketChannel>, Integer>> normalMatch = new ArrayList<>();

    static class PairComparable implements java.util.Comparator<Pair<Pair<Integer, SocketChannel>, Integer>> {

        @Override
        public int compare(Pair<Pair<Integer, SocketChannel>, Integer> o1, Pair<Pair<Integer, SocketChannel>, Integer> o2) {
            return -(o1.second - o2.second);
        }
    }

    private static final List<Pair<Pair<Integer, SocketChannel>, Integer>> rankedMatch = new ArrayList<>();

    public static void config(Config configuration) {
        config = configuration;
    }

    public static Game newNormalMatch(List<Pair<Pair<Integer, SocketChannel>, Integer>> users) {
        if (users.size() == (config.maxPlayers)) {
            System.out.println("[LOG] Creating a game");
            Game game = new Game(justIdSocket(users));
            users.clear();
            return game;
        }
        return null;
    }

    public static List<Pair<Integer, SocketChannel>> justIdSocket(List<Pair<Pair<Integer, SocketChannel>, Integer>> u) {
        List<Pair<Integer, SocketChannel>> m = new LinkedList<>();
        for (var pair : u) {
            m.add(pair.first);
        }
        return m;
    }

    public static Game newRankedMatch(List<Pair<Pair<Integer, SocketChannel>, Integer>> users) {
        reOrder(users);
        if (users.size() < (config.maxPlayers)) {
            return null;
        }
        for (int i = 0; i < users.size() + 1 - config.maxPlayers; i++) {
            var first = users.get(i);
            var otherIndex = i + config.maxPlayers - 1;
            var other = users.get(otherIndex);
            if (first.second - other.second < 10 * (users.size() + 1 - config.maxPlayers)) {
                System.out.println("[LOG] Creating a game");
                Game game = new Game(justIdSocket(users.subList(i, otherIndex + 1)));
                users.subList(i, otherIndex + 1).clear();
                return game;
            }
        }
        return null;
    }

    public static void reOrder(List<Pair<Pair<Integer, SocketChannel>, Integer>> users) {
        users.sort(new PairComparable());
    }

    public static Pair<Pair<Integer, SocketChannel>, Integer> userPair(int userId, SocketChannel socket) {
        var rank = UserRepository.get().getById(userId).getRank().getValue();
        var userSocket = new Pair<>(userId, socket);
        return new Pair<>(userSocket, rank);

    }

    public static Game findNormalMatch(int userId, SocketChannel socket) {
        normalMatch.add(userPair(userId, socket));
        return newNormalMatch(normalMatch);
    }

    public static Game findRankedMatch(int userId, SocketChannel socket) {
        rankedMatch.add(userPair(userId, socket));
        return newRankedMatch(rankedMatch);
    }
}
