/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjackresumemultiplayergame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameDataReader {

    public static List<GameRecord> readGameRecords(String filename) {
        List<GameRecord> gameRecords = new ArrayList<>();
        Set<String> uniquePlayerNames = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean startReading = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name,Round,Card1,Card2,Card3,Bet,Result,Balance")) {
                    startReading = true;
                    continue;
                }
                if (startReading) {
                    String[] parts = line.split(",");
                    if (parts.length == 8) {
                        GameRecord record = new GameRecord();
                        record.name = parts[0];
                        uniquePlayerNames.add(record.name); // Add the name to the set
                        record.round = Integer.parseInt(parts[1]);
                        record.betPoint = Integer.parseInt(parts[5]);
                        record.result = Integer.parseInt(parts[6]);
                        record.balance = Integer.parseInt(parts[7]);
                        gameRecords.add(record);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading game history: " + e.getMessage());
        }

        // Print unique player names
        System.out.println("Unique player names:");
        for (String playerName : uniquePlayerNames) {
            System.out.println(playerName);
        }

        return gameRecords;
    }

    public static List<Player> createPlayers(List<GameRecord> gameRecords) {
        List<Player> players = new ArrayList<>();
        for (GameRecord record : gameRecords) {
            Player player = new Player(record.name, record.balance);
            players.add(player);
        }
        return players;
    }
}
