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

    static int playTurn(int betPoint, int card1, int card2, Player player, GameRecord[] records, int round,
            GameType gametype) {
        int card3, sum;
        card3 = random.nextInt(13) + 1;
        sum = gametype.calculate(card1, card2, card3);
        System.out.println(round);
        records[round] = new GameRecord();
        records[round].card1 = card1;
        records[round].card2 = card2;
        records[round].card3 = card3;
        records[round].betPoint = betPoint;

        System.out.println("Your third card is :" + display(card3));
        System.out.println("Your sum is:" + sum);
        if (gametype.evaluate(card1, card2, card3) == 0) {
            player.balance -= betPoint;
            records[round].result = -betPoint;
            System.out.println("You lose " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        } else {
            player.balance += betPoint;
            records[round].result = betPoint;
            System.out.println("You win " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        }
        records[round].balance = player.balance;
        return player.balance;
    }

    static void displayHistory(GameRecord[] records, int rounds, Player[] players, int numPlayers) {
        try (Writer writer = new FileWriter("game_history.txt")) {
            writer.write("Name,Round,Card1,Card2,Card3,Bet,Result,Balance\n");
            System.out.println("Name,Round,Card1,Card2,Card3,Bet,Result,Balance");

            int count = 0;
            for (int variable = 0; variable < rounds; variable++) {
                if (variable % numPlayers == 0) {
                    count++;
                }
                int playerIndex = variable % numPlayers;
                Player currentPlayer = players[playerIndex];

                String message = currentPlayer.name + "," + count + ","
                        + records[variable].card1 + ","
                        + records[variable].card2 + ","
                        + records[variable].card3 + ","
                        + records[variable].betPoint + ","
                        + records[variable].result + ","
                        + records[variable].balance;

                System.out.println(message);
                writer.write(message + "\n");
            }

            System.out.println("Game history has been saved to game_history.txt");
        } catch (IOException e) {
            System.err.println("Error writing game history to file: " + e.getMessage());
        }
    }

    static void playGame(List<Integer> playerBalances,int numPlayers,Player[] players,int rounds, GameRecord[] records,GameType gametype )
    {
             while (true) {
                 Scanner scanner=new Scanner(System.in);
                 int betPoint,card1,card2;
                // Loop through each player
                for (int player = 0; player < numPlayers; player++) {
                    if (playerBalances.get(player) <= 0) {
                        System.out.println("Player " + players[player].name + " is out of the game.");
                        continue;
                    }

                    card1 = random.nextInt(13) + 1;
                    card2 = random.nextInt(13) + 1;

                    System.out.println("\nPlayer " + players[player].name + ", your cards are " + display(card1)
                            + " and " + display(card2));
                    System.out.println("How many points do you want to bet?");

                    betPoint = scanner.nextInt();
                    if (betPoint > 0 && betPoint <= playerBalances.get(player)) {
                        playerBalances.set(player, playTurn(betPoint, card1, card2, players[player], records, rounds, gametype));
                        rounds++;
                    } else if (betPoint <= 0) {
                        System.out.println("Bet point cannot be negative or zero");
                    } else {
                        System.out.println("Player " + players[player].name + ", you don't have enough points");
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
             displayHistory(records, rounds, players, numPlayers);

            System.out.println("Game over!!!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to resume the previous game? (y/n): ");
        char resumeResponse = scanner.next().charAt(0);
      if (resumeResponse == 'y') {
    GameDataReader gameDataReader = new GameDataReader();
    List<GameRecord> gameRecords = gameDataReader.readGameRecords("game_history.txt");

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
int rounds=0;
  Set<String> uniquePlayerNames = new HashSet<>();
    // Now, you should have the gameRecords and the game type selected
    for (GameRecord record : gameRecords) {
         uniquePlayerNames.add(record.name); 
        rounds = record.round;
    }
    List<Player> players=new ArrayList();
    for(String playerNames:uniquePlayerNames)
    {
        players.add(new Player(playerNames,0));
    }
    for(GameRecord record:gameRecords)
    {
        for(Player player:players)
        {
            if(player.name.equals(record.name))
            {
                player.balance=record.balance;
            }
        }
    }
    int numPlayers=players.size();
    List<Integer> playerBalances=new ArrayList();
    for(int i=0; i<numPlayers;i++)
    {
        playerBalances.add(players.get(i).balance);
    }
    
    
     playGame(playerBalances,numPlayers,(Player [])players.toArray(new Player[numPlayers]),rounds, (GameRecord[])gameRecords.toArray(new GameRecord[100]),gametype );
}


         else if (resumeResponse == 'n') {
            System.out.println("Starting a new game.");
            System.out.println("Welcome to the game of Blackjack!");

            int condition;
            GameType gametype = null;

            System.out.println("Select the game condition:");
            System.out.println("1. Win if sum is less than 21");
            System.out.println("2. Win if sum is less than 17");
            System.out.println("3. Win if J, K, Q, A and sum is less than 21");
            System.out.println("4. Win if J, K, Q are equal to 10, A is equal to 1 and sum is less than 21");
            condition = scanner.nextInt();

            gametype = switch (condition) {
                case 1 ->
                    new GameTypeSumIsLessThan21();
                case 2 ->
                    new GameTypeSumIsLessThan17();
                case 3 ->
                    new GameTypeAJQKare10();
                case 4 ->
                    new GameTypeJQKare10();
                default -> {
                    System.out.println("Wrong selection of game");
                    throw new IllegalArgumentException("Invalid game selection");
                }
            };

            int numPlayers;
            System.out.print("Enter the number of players (1-17): ");
            numPlayers = scanner.nextInt();

            if (numPlayers < 1 || numPlayers > 17) {
                System.out.println("Invalid number of players. Exiting...");
                return;
            }
            int startingPoints = 100;
            Player[] players = new Player[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                System.out.print("Enter name of Player " + (i + 1) + ": ");
                String playerName = scanner.next();
                players[i] = new Player(playerName, startingPoints);
            }

            System.out.println("Names of players:");
            for (int i = 0; i < numPlayers; i++) {
                System.out.println("Player " + (i + 1) + ": " + players[i].name);
            }

            int rounds = 0;
            GameRecord[] records = new GameRecord[100];

            // Array to store player balances
            System.out.println("\nYou each have 100 points in your accounts.");

            // Main game loop
            List<Integer> playerBalances = new ArrayList<>(numPlayers);
            for (int i = 0; i < numPlayers; i++) {
                playerBalances.add(startingPoints);
            }

       playGame(playerBalances,numPlayers,players,rounds, records,gametype );

            
        }
    }
}
