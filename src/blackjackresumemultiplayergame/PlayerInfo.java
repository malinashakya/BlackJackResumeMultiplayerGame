/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjackresumemultiplayergame;

/**
 *
 * @author malin
 */
import java.util.List;

public class PlayerInfo {
    private List<Player> players;
    private int numPlayers;

    public PlayerInfo(List<Player> players, int numPlayers) {
        this.players = players;
        this.numPlayers = numPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
