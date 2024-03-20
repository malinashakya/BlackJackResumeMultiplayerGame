package blackjackresumemultiplayergame;

/**
 *
 * @author malin
 */
import blackjackresumemultiplayergame.game_type.GameTypeAJQKare10;
import blackjackresumemultiplayergame.game_type.GameType;
import blackjackresumemultiplayergame.game_type.GameTypeJQKare10;
import blackjackresumemultiplayergame.game_type.GameTypeSumIsLessThan17;
import blackjackresumemultiplayergame.game_type.GameTypeSumIsLessThan21;
import blackjackresumemultiplayergame.GameDataReader;
import java.io.*;
import java.util.*;

public class BlackJackResumeMultiplayerGame {

    public static Random random = new Random();

    //Card displays like the playing card type A,J,Q,K
    public static String display(int card) {
        char[] temp_card = new char[3];
        temp_card[2] = '\0';
        if (card == 11) {
            temp_card[0] = 'J';
        } else if (card == 12) {
            temp_card[0] = 'Q';
        } else if (card == 13) {
            temp_card[0] = 'K';
        } else if (card == 1) {
            temp_card[0] = 'A';
        } else if (card >= 2 && card < 10) {
            temp_card[0] = (char) (card + 48);
        } else if (card == 10) {
            temp_card[0] = '1';
            temp_card[1] = '0';
        }
        return new String(temp_card);
    }

    //Euta player le khelne single record
    static int playTurn(int betPoint, int card1, int card2, Player player, List<GameRecord> records, int round,
            GameType gametype) {
        int card3, sum;
        card3 = random.nextInt(13) + 1;
        sum = gametype.calculate(card1, card2, card3);
        System.out.println(round);
        //adding new GameRecord in Existing GameRecord
        records.add(new GameRecord());
        records.get(round).card1 = card1;
        records.get(round).card2 = card2;
        records.get(round).card3 = card3;
        records.get(round).betPoint = betPoint;

        System.out.println("Your third card is :" + display(card3));
        System.out.println("Your sum is:" + sum);
        if (gametype.evaluate(card1, card2, card3) == 0) {
            player.balance -= betPoint;
            records.get(round).result = -betPoint;
            System.out.println("You lose " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        } else {
            player.balance += betPoint;
            records.get(round).result = betPoint;
            System.out.println("You win " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        }
        records.get(round).balance = player.balance;
        return player.balance;
    }

    //History of game display garne
    static void displayHistory(List<GameRecord> records, List<Player> players) {
        try (Writer writer = new FileWriter("game_history.txt")) {
            writer.write("Name,Round,Card1,Card2,Card3,Bet,Result,Balance\n");
            System.out.println("Name,Round,Card1,Card2,Card3,Bet,Result,Balance");
            int numPlayers = players.size();
            int rounds = records.size();
            int round = 0;
            for (int turns = 0; turns < rounds; turns++) {
                if (turns % numPlayers == 0) {
                    round++;
                }
                int playerIndex = turns % numPlayers;
                Player currentPlayer = players.get(playerIndex);

                String message = currentPlayer.name + "," + round + ","
                        + records.get(turns).card1 + ","
                        + records.get(turns).card2 + ","
                        + records.get(turns).card3 + ","
                        + records.get(turns).betPoint + ","
                        + records.get(turns).result + ","
                        + records.get(turns).balance;

                System.out.println(message);
                writer.write(message + "\n");
            }

            System.out.println("Game history has been saved to game_history.txt");
        } catch (IOException e) {
            System.err.println("Error writing game history to file: " + e.getMessage());
        }
    }
    //Actual game play rules

    static void playGame(List<Player> players, List<GameRecord> records, GameType gametype) {
        Scanner scanner = new Scanner(System.in);
        int rounds = records.size();
        while (true) {
            int betPoint, card1, card2;
            // Loop through each player
            int numPlayers = players.size();
            for (int player = 0; player < numPlayers; player++) {
                if (players.get(player).balance <= 0) {
                    System.out.println("Player " + players.get(player).name + " is out of the game.");
                    continue;
                }

                card1 = random.nextInt(13) + 1;
                card2 = random.nextInt(13) + 1;

                System.out.println("\nPlayer " + players.get(player).name + ", your cards are " + display(card1)
                        + " and " + display(card2));
                System.out.println("How many points do you want to bet?");

                betPoint = scanner.nextInt();
                if (betPoint > 0 && betPoint <= players.get(player).balance) {
                    playTurn(betPoint, card1, card2, players.get(player), records, rounds, gametype);
                    rounds++;
                } else if (betPoint <= 0) {
                    System.out.println("Bet point cannot be negative or zero");
                } else {
                    System.out.println("Player " + players.get(player).name + ", you don't have enough points");
                }
            }

            System.out.print("\nDo you want to continue to the next round? (y/n): ");
            char response = scanner.next().charAt(0);
            if (response == 'n') {
                break; // Exit the game if the user does not want to continue
            } else if (response != 'y') {
                System.out.println("Wrong input");
            }

        }
        System.out.println("Rounds:"
                + rounds);
        displayHistory(records, players);

        System.out.println("Game over!!!");
    }

    //Records of Players
    static List<Player> getPlayer(Scanner scanner) {
        int numPlayers;
        System.out.print("Enter the number of players (1-17): ");
        numPlayers = scanner.nextInt();

        if (numPlayers < 1 || numPlayers > 17) {
            System.out.println("Invalid number of players. Exiting...");
            return null; // Return null indicating failure
        }

        int startingPoints = 100;
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name of Player " + (i + 1) + ": ");
            String playerName = scanner.next();
            players.add(new Player(playerName, startingPoints));
        }

        System.out.println("Names of players:");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + ": " + players.get(i).name);
        }

        return players; // Return the list of players
    }

    //Game Condition Selection
    static GameType selectGameCondition(Scanner scanner) {
        System.out.println("Select the game condition:");
        System.out.println("1. Win if sum is less than 21");
        System.out.println("2. Win if sum is less than 17");
        System.out.println("3. Win if J, K, Q, A and sum is less than 21");
        System.out.println("4. Win if J, K, Q are equal to 10, A is equal to 1 and sum is less than 21");

        int condition = scanner.nextInt();
        GameType gametype;

        switch (condition) {
            case 1:
                gametype = new GameTypeSumIsLessThan21();
                break;
            case 2:
                gametype = new GameTypeSumIsLessThan17();
                break;
            case 3:
                gametype = new GameTypeAJQKare10();
                break;
            case 4:
                gametype = new GameTypeJQKare10();
                break;
            default:
                System.out.println("Wrong selection of game");
                throw new IllegalArgumentException("Invalid game selection");
        }

        return gametype;
    }

    //Main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to resume the previous game? (y/n): ");
        char resumeResponse = scanner.next().charAt(0);
        if (resumeResponse == 'y') {
            GameDataReader gameDataReader = new GameDataReader();
            List<GameRecord> gameRecords = gameDataReader.readGameRecords("game_history.txt");
            GameType gametype = selectGameCondition(scanner);
            //key:String type, value: Player type
            HashMap<String, Player> players = new HashMap<>();
            // Now, you should have the gameRecords and the game type selected
            for (GameRecord record : gameRecords) {
                players.put(record.name, new Player(record.name, record.balance));
            }
            playGame(new ArrayList(players.values()), gameRecords, gametype);

        } else if (resumeResponse == 'n') {
            System.out.println("Starting a new game.");
            System.out.println("Welcome to the game of Blackjack!");
            GameType gametype = selectGameCondition(scanner);

            //Changed to List
            List<GameRecord> records = new ArrayList<>();
            List<Player> players = getPlayer(scanner);
            System.out.println("\nYou each have 100 points in your accounts.");

            playGame(players, records, gametype);

        }
    }
}
